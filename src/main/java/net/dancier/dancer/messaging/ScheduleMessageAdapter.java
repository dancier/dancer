package net.dancier.dancer.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduleMessageAdapter implements ScheduleMessagePort {

    public static final Logger log = LoggerFactory.getLogger(ScheduleMessageAdapter.class);

    private final OutboxJpaRepository outboxJpaRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void schedule(CloudEvent cloudEvent, String key) {
        log.info("sending object: " + cloudEvent);
        log.info("with key:" + key);
        OutboxJpaEntity outboxJpaEntity = new OutboxJpaEntity();
        outboxJpaEntity.setData(objectMapper.convertValue(cloudEvent, JsonNode.class));
        outboxJpaEntity.setType(cloudEvent.getType());
        outboxJpaEntity.setKey(key);
        outboxJpaEntity.setCreatedAt(cloudEvent.getTime());
        outboxJpaEntity.setStatus(OutboxJpaEntity.STATUS.NEW);
        outboxJpaRepository.save(outboxJpaEntity);
    }
}
