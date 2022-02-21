package net.dancier.dancer.mail.repository;

import net.dancier.dancer.mail.model.OutgoingMail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface OutgoingMailRepository extends CrudRepository<OutgoingMail, UUID> {
    @Query(
            value = "SELECT * FROM outgoing_mail",
            nativeQuery = true
    )
    Collection<OutgoingMail> selectToProcess();
}
