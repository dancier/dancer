package net.dancier.dancer.mail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;
import org.springframework.mail.SimpleMailMessage;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingMail extends EntityWithUUID implements Serializable {

    @Enumerated(EnumType.STRING)
    private OutgoingMailStatus status;
    private Integer retry;

    @JdbcTypeCode(SqlTypes.JSON)
    @Basic(fetch = FetchType.EAGER)
    private SimpleMailMessage mail;
}
