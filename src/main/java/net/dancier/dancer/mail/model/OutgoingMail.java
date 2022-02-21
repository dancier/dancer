package net.dancier.dancer.mail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutgoingMail extends EntityWithUUID implements Serializable {

    @Enumerated(EnumType.STRING)
    private OutgoingMailStatus status;
    private Integer retry;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.EAGER)
    private DancierMailMessage mail;
}
