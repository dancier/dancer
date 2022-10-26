package net.dancier.dancer.eventlog.service;

import io.minio.errors.*;
import net.dancier.dancer.eventlog.model.EventlogEntry;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface EventlogS3Service {
    void storeEventLogEntry(EventlogEntry entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException ;

}
