package net.dancier.dancer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Component
public class ScheduleMessageAdapter implements ScheduleMessagePort {

    public static final Logger log = LoggerFactory.getLogger(ScheduleMessageAdapter.class);

    private final OutboxJpaRepository outboxJpaRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void schedule(Object object,
                         String key,
                         URI source,
                         String type) {
        log.info("sending object: " + object);
        log.info("with key:" + key);

        OutboxJpaEntity outboxJpaEntity = new OutboxJpaEntity();
        outboxJpaEntity.setData(objectMapper.convertValue(object, JsonNode.class));
        outboxJpaEntity.setType(type);
        outboxJpaEntity.setSource(source.toString());
        outboxJpaEntity.setKey(key);
        outboxJpaEntity.setCreatedAt(OffsetDateTime.now());
        outboxJpaEntity.setStatus(OutboxJpaEntity.STATUS.NEW);
        outboxJpaRepository.save(outboxJpaEntity);
    }
}
