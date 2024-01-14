package net.dancier.dancer.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collection;

@RequiredArgsConstructor
@Component
public class SendMessagesJob {

    private final static Logger log = LoggerFactory.getLogger(SendMessagesJob.class);

    private final OutboxJpaRepository outboxJpaRepository;

    private final KafkaTemplate kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedRate = 2000)
    public void sendMessages() throws JsonProcessingException {
        Collection<OutboxJpaEntity> itemsToSend = outboxJpaRepository.lockAndList();
        for (OutboxJpaEntity item: itemsToSend) {
            log.info("Sending: {}", item);
            send(item);
            item.setStatus(OutboxJpaEntity.STATUS.DONE);
        }
        kafkaTemplate.flush();
    }

    private void send(OutboxJpaEntity item) throws JsonProcessingException {
        CloudEvent cloudEvent = CloudEventBuilder.v1()
                .withId(item.getId().toString())
                .withSource(URI.create(item.getSource()))
                .withType(item.getType())
                .withData(objectMapper.writeValueAsBytes(item.getData()))
                .build();

        kafkaTemplate.send(item.getType(), item.getKey(), cloudEvent);

    }
}
