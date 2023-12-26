package net.dancier.dancer.eventlog.service;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.ScheduleMessagePort;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import net.dancier.dancer.eventlog.repository.EventlogStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventPublisherJob {

    private final static Logger log = LoggerFactory.getLogger(EventPublisherJob.class);

    private final EventlogDAO eventlogDAO;

    private final EventlogS3Service eventlogS3Service;

    @Scheduled(fixedRate = 2000)
    public void process() throws SQLException {
        log.trace("Storing eventlog-entries in S3");
        List<Eventlog> eventlogEntries = eventlogDAO.lockAndGet(50);
        for(Eventlog eventlog : eventlogEntries) {
            log.debug("Processing: " + eventlog);
            try {
                storeInS3(eventlog);
                eventlog.setStatus(EventlogStatus.OK);
            } catch (Exception e) {
                log.error("Unable to process eventlog: " + eventlog + " With problem: " + e.getStackTrace());
                eventlog.setStatus(EventlogStatus.FAILED);
                eventlog.setErrorMessage(e.getMessage());
            }
            eventlogDAO.update(eventlog);
        }
    }

    public void storeInS3(Eventlog eventlog) {
        eventlogS3Service.storeEventLog(eventlog);
    }
}