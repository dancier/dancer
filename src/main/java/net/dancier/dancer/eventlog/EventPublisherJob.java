package net.dancier.dancer.eventlog;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
        List<EventlogEntry> eventlogEntries = eventlogDAO.lockAndGet(50);
        for(EventlogEntry eventlogEntry: eventlogEntries) {
            log.debug("Processing: " + eventlogEntry);
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

    public void storeInS3(EventlogEntry eventlogEntry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException {
        eventlogS3Service.storeEventLogEntry(eventlogEntry);
    }
}
