package net.dancier.dancer.messaging;

import net.dancier.dancer.eventlog.ScheduleMessagePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMessageAdapter implements ScheduleMessagePort {

    public static final Logger log = LoggerFactory.getLogger(ScheduleMessageAdapter.class);


    @Override
    public void schedule(Object data, String key, String type) {
        log.info("sending object: " + data);
    }
}
