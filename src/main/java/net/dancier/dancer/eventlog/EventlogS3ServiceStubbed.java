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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty(name = "app.s3.active", havingValue = "false")
public class EventlogS3ServiceStubbed implements EventlogS3Service {

    private static final Logger log = LoggerFactory.getLogger(EventlogS3ServiceStubbed.class);


    @Override
    public void storeEventLogEntry(EventlogEntry entry) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("Not sending anything to s3, running in stubbed mode...");
    }

 }
