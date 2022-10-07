package net.dancier.dancer.eventlog;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class EventPublisherJob {

    private final static Logger log = LoggerFactory.getLogger(EventPublisherJob.class);

    private final EventlogDAO eventlogDAO;

    @Scheduled(fixedRate = 10000)
    public void process() throws SQLException {
        log.debug("Storing eventlog-entries in S3");
        List<EventlogEntry> eventlogEntries = eventlogDAO.lockAndGet(2);
        for(EventlogEntry eventlogEntry: eventlogEntries) {
            log.info("Processing: " + eventlogEntry);
            try {
                storeInS3(eventlogEntry);
                eventlogEntry.setStatus(EventlogEntryStatus.OK);
            } catch (Exception e) {
                eventlogEntry.setStatus(EventlogEntryStatus.FAILED);
                eventlogEntry.setErrorMessage(e.getMessage());
            }
            eventlogDAO.update(eventlogEntry);
        }
    }

    public void storeInS3(EventlogEntry eventlogEntry) {
        Random random = new Random();
        if (0.2d > random.nextDouble() ) {
            throw new RuntimeException();
        }
    }
}
