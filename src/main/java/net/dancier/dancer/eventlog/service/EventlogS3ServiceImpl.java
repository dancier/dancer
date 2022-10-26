package net.dancier.dancer.eventlog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
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
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.s3.active", havingValue = "true")
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
    public void init()  {
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
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                log.info("Bucket " + bucket + " did not exist. Creating it.");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

        } catch (Exception e) {
            log.error("Problem creating needed bucket");
        }
    }

    @Override
    public void storeEventLog(Eventlog entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.putObject(putObjectArgsFromEventlogEntry(entry));
    }

    private PutObjectArgs putObjectArgsFromEventlogEntry(Eventlog eventlog) throws JsonProcessingException {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectMapper.writeValueAsString(eventlog).getBytes(StandardCharsets.UTF_8));
        return PutObjectArgs.builder()
                .bucket("test")
                .contentType("application/json")
                .object(objectNameFromEventlog(eventlog))
                .stream(bais, bais.available(), -1).build();
    }

    private String objectNameFromEventlog(Eventlog eventlog) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(eventlog.getCreated(),ZoneId.systemDefault());
        StringBuilder sb = new StringBuilder();
        sb.append(localDateTime.getYear());
        sb.append("/");
        sb.append(localDateTime.getMonthValue());
        sb.append("/");
        sb.append(localDateTime.getDayOfMonth());
        sb.append("/");
        sb.append(localDateTime.getHour());
        sb.append("/");
        sb.append(eventlog.getTopic());
        sb.append("/");
        sb.append(eventlog.getId());
        return sb.toString();
    }
}
