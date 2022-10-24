package net.dancier.dancer.eventlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.credentials.ClientGrantsProvider;
import io.minio.credentials.Provider;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import net.dancier.dancer.eventlog.token.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Service
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
    public void storeEventLogEntry(EventlogEntry entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(putObjectArgsFromEventlogEntry(entry));
    }

    private PutObjectArgs putObjectArgsFromEventlogEntry(EventlogEntry eventlogEntry) throws JsonProcessingException {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectMapper.writeValueAsString(eventlogEntry).getBytes(StandardCharsets.UTF_8));
        return PutObjectArgs.builder()
                .bucket("test")
                .contentType("application/json")
                .object(eventlogEntry.getTopic() + "/" + eventlogEntry.getId())
                .stream(bais, bais.available(), -1).build();
    }
}
