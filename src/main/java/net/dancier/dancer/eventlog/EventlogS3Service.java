package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@RequiredArgsConstructor
public class EventlogS3Service {

    private static final Logger log = LoggerFactory.getLogger(EventlogS3Service.class);

    @Value("${app.s3.host}")
    String host;

    private final ObjectMapper objectMapper;
    MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient
                .builder()
                .endpoint(host)
                .build();
    }

    public void storeEventLogEntry(EventlogEntry entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info(String.format("Placing %s in S3.", entry));
        eventlogEntryToFile(entry, "/tmp/minio-" + entry.getId());
        minioClient.
        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("test")
                        .contentType("application/json")
                        .object(entry.getTopic() + "-" + entry.getId())
                        .filename("/tmp/minio-" + entry.getId())
                        .build());
    }

    private void eventlogEntryToFile(EventlogEntry eventlogEntry, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf(objectMapper.writeValueAsString(eventlogEntry));
        printWriter.close();
    }
}
