package net.dancier.dancer.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "outbox")
@NoArgsConstructor
public class OutboxJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String type;

    private String source;

    private String key;

    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode data;

    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    public static enum STATUS {
        NEW,
        IN_PROGRESS,
        TEMP_FAILED,
        FINALLY_FAILED,
        DONE
    }

}
