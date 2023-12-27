package net.dancier.dancer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.PojoCloudEventDataMapper;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

import static io.cloudevents.core.CloudEventUtils.mapData;

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
                         String type) throws JsonProcessingException {
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
