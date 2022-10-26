package net.dancier.dancer.eventlog.service;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.Eventlog;
import net.dancier.dancer.eventlog.repository.EventlogDAO;
import net.dancier.dancer.eventlog.repository.EventlogEntryStatus;
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
        List<Eventlog> eventlogEntries = eventlogDAO.lockAndGet(50);
        for(Eventlog eventlog : eventlogEntries) {
            log.debug("Processing: " + eventlog);
            try {
                storeInS3(eventlog);
                eventlog.setStatus(EventlogEntryStatus.OK);
            } catch (Exception e) {
                eventlog.setStatus(EventlogEntryStatus.FAILED);
                eventlog.setErrorMessage(e.getMessage());
            }
            eventlogDAO.update(eventlog);
        }
    }

    public void storeInS3(Eventlog eventlog) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ServerException, InsufficientDataException, ErrorResponseException, InvalidResponseException, XmlParserException {
        eventlogS3Service.storeEventLogEntry(eventlog);
    }
}
