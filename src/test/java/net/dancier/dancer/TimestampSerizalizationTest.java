package net.dancier.dancer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Java6BDDAssertions.then;

@SpringBootTest
public class TimestampSerizalizationTest extends AbstractPostgreSQLEnabledTest{

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSerialization() throws JsonProcessingException {
       String serzialized = objectMapper.writeValueAsString(OffsetDateTime.now(ZoneId.of("UTC")));
       then(serzialized).isNotNull();
    }

}
