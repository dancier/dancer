package net.dancier.dancer.eventlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class EventlogDAO {

    public static Logger log = LoggerFactory.getLogger(EventlogDAO.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static String TEST = """
            {"foo": "bar"}
            """;

    public void publish(EventlogEntry eventlogEntry) throws SQLException {
        log.info("publishing: " + eventlogEntry);
        //String sql = "insert into eventlog(id, mail) VALUES (gen_random_uuid(), '{\"f\":\"bar\"}'::JSON);";
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
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", eventlogEntry.getId())
                .addValue("topic", eventlogEntry.getTopic())
                .addValue("metaData", eventlogEntry.getMetaData().toString())
                .addValue("payload", eventlogEntry.getPayload().toString())
                .addValue("created", Timestamp.from(eventlogEntry.getCreated()))
                .addValue("roles", jdbcTemplate.getDataSource().getConnection().createArrayOf("text",eventlogEntry.getRoles().toArray()))
                .addValue("userid", eventlogEntry.getUserId());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public int getCountOfEventlogEntries() {
        return jdbcTemplate.queryForObject("SELECT count(1) FROM eventlog;", Integer.class);
    }

}
