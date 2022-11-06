package net.dancier.dancer.eventlog.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.Eventlog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "app.s3.active", havingValue = "false")
public class EventlogS3ServiceStubbed implements EventlogS3Service {

    private static final Logger log = LoggerFactory.getLogger(EventlogS3ServiceStubbed.class);

    @Override
    public void storeEventLog(Eventlog entry) {
        log.info("Not sending anything to s3, running in stubbed mode...");
    }

}