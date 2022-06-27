package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventlogDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static String TEST = """
            {"foo": "bar"}
            """;

    public void bla() {
        String sql = "INSERT INTO eventlog values ()";
        return;
    }

    public int getCountOfEventlogEntries() {
        return jdbcTemplate.queryForObject("SELECT count(1) FROM eventlog;", Integer.class);
    }

}
