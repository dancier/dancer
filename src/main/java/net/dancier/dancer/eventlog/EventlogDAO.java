package net.dancier.dancer.eventlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventlogDAO {

    public static Logger log = LoggerFactory.getLogger(EventlogDAO.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    EventlogEntryRowMapper eventlogEntryRowMapper;

    public void update(EventlogEntry eventlogEntry) throws SQLException {
        String sql = """
                    UPDATE eventlog
                       SET topic = :topic,
                           meta_data = :metaData::JSON,
                           payload = :payload::JSON,
                           created = :created,
                           user_id = :userId,
                           roles = :roles,
                           status = :status,
                           error_message = :error_message
                     WHERE id = :id
                """;
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", eventlogEntry.getId())
                    .addValue("topic", eventlogEntry.getTopic())
                    .addValue("metaData", eventlogEntry.getMetaData().toString())
                    .addValue("payload", eventlogEntry.getPayload().toString())
                    .addValue("created", Timestamp.from(eventlogEntry.getCreated()))
                    .addValue("roles", connection.createArrayOf("text",eventlogEntry.getRoles().toArray()))
                    .addValue("userId", eventlogEntry.getUserId())
                    .addValue("status", eventlogEntry.getStatus().toString())
                    .addValue("error_message", eventlogEntry.getErrorMessage());
            namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        } finally {
            if (connection!=null) {
                DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
            }
        }
    }
    public List<EventlogEntry> lockAndGet(Integer size) {
        String sql = """
                UPDATE eventlog\s
                   SET status = 'IN_PROGRESS'\s
                 WHERE "id" in (
                		select id\s
                  		  from eventlog\s
                 	     where status = 'QUEUED'
                    FOR UPDATE LIMIT :limit
                     )
                	 RETURNING\s
                	 	id,
                		topic,
                		meta_data,
                		payload,
                		created,
                		user_id,
                		roles,
                		status,
                		error_message
                	;
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", size);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("limit", size);

        List<EventlogEntry> eventlogEntries = namedParameterJdbcTemplate.query(sql, sqlParameterSource, eventlogEntryRowMapper);
        return eventlogEntries;
    }

    public void schedule(EventlogEntry eventlogEntry) throws SQLException {
        log.info("scheduling: " + eventlogEntry);
        String sql = """
            INSERT 
              INTO eventlog (
                id, 
                topic, 
                meta_data,
                payload,
                created,
                roles,
                user_id
              ) values (
                :id, 
                :topic, 
                :metaData::JSON,
                :payload::JSON,
                :created,
                :roles,
                :userid)
        """;
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", eventlogEntry.getId())
                    .addValue("topic", eventlogEntry.getTopic())
                    .addValue("metaData", eventlogEntry.getMetaData().toString())
                    .addValue("payload", eventlogEntry.getPayload().toString())
                    .addValue("created", Timestamp.from(eventlogEntry.getCreated()))
                    .addValue("roles", connection.createArrayOf("text",eventlogEntry.getRoles().toArray()))
                    .addValue("userid", eventlogEntry.getUserId());
            namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        } finally {
            if (connection!=null)
                DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
        }
    }

    public int getCountOfEventlogEntries() {
        return jdbcTemplate.queryForObject("SELECT count(1) FROM eventlog;", Integer.class);
    }

}
