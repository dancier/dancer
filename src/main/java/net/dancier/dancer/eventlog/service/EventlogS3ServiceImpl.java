package net.dancier.dancer.eventlog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.credentials.ClientGrantsProvider;
import io.minio.credentials.Provider;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.model.Eventlog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.s3.active", havingValue = "true")
public class EventlogS3ServiceImpl implements EventlogS3Service {

    private static final Logger log = LoggerFactory.getLogger(EventlogS3ServiceImpl.class);

    @Value("${app.s3.host}")
    String s3Host;

    @Value("${app.s3.stsEndpoint}")
    String stsEndpoint;

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    MinioClient minioClient;

    @PostConstruct
    public void init() {
        Provider provider = new ClientGrantsProvider
                (
                        () -> jwtProvider.getJwt(),
                        stsEndpoint,
                        86400,
                        null,
                        null
                );
        minioClient = MinioClient
                .builder()
                .endpoint(s3Host)
                .credentialsProvider(provider)
                .build();
    }

    @Override
    public void storeEventLogEntry(Eventlog entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(putObjectArgsFromEventlogEntry(entry));
    }

    private PutObjectArgs putObjectArgsFromEventlogEntry(Eventlog eventlog) throws JsonProcessingException {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectMapper.writeValueAsString(eventlog).getBytes(StandardCharsets.UTF_8));
        return PutObjectArgs.builder()
                .bucket("test")
                .contentType("application/json")
                .object(eventlog.getTopic() + "/" + eventlog.getId())
                .stream(bais, bais.available(), -1).build();
    }
}