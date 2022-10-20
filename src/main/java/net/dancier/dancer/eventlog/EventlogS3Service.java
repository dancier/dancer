package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
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
        minioClient.putObject(putObjectArgsFromEventlogEntry(entry));
    }

    private PutObjectArgs putObjectArgsFromEventlogEntry(EventlogEntry eventlogEntry) throws UnsupportedEncodingException, JsonProcessingException {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectMapper.writeValueAsString(eventlogEntry).getBytes(StandardCharsets.UTF_8));
        return PutObjectArgs.builder()
                .bucket("test")
                .contentType("application/json")
                .object(eventlogEntry.getTopic() + "-foo")
                .stream(bais, bais.available(), -1).build();
    }
}
