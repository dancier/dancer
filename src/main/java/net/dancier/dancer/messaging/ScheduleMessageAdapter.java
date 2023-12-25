package net.dancier.dancer.messaging;

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

    private final KafkaTemplate kafkaTemplate;

    @Override
    public void schedule(Object data, String key, String type) {
        log.info("sending object: " + data);
        kafkaTemplate.send("profile-updated", data.toString());
    }
}
