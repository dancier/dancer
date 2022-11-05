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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RequiredArgsConstructor
@Service
public class EventlogS3ServiceImpl implements EventlogS3Service {

    private static final Logger log = LoggerFactory.getLogger(EventlogS3ServiceImpl.class);

    @Value("${app.s3.host}")
    String s3Host;

    @Value("${app.s3.stsEndpoint}")
    String stsEndpoint;

    @Value("${app.s3.bucket}")
    String bucket;

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
                .bucket(bucket)
                .contentType("application/json")
                .object(objectNameFromEventlogEntry(eventlogEntry))
                .stream(bais, bais.available(), -1).build();
    }

    private String objectNameFromEventlogEntry(EventlogEntry entry) {
        OffsetDateTime offsetDateTime = entry.getCreated().atOffset(ZoneOffset.UTC);
        StringBuilder sb = new StringBuilder();
        sb.append(offsetDateTime.getYear());
        sb.append("/");
        sb.append(offsetDateTime.getMonthValue());
        sb.append("/");
        sb.append(offsetDateTime.getDayOfMonth());
        sb.append("/");
        sb.append(offsetDateTime.getHour());
        sb.append("/");
        sb.append(entry.getTopic());
        sb.append("/");
        sb.append(entry.getId());
        sb.append(".json");
        return sb.toString();
    }
}
