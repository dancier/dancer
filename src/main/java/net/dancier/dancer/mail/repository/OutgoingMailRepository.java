package net.dancier.dancer.mail.repository;

import net.dancier.dancer.mail.model.OutgoingMail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface OutgoingMailRepository extends CrudRepository<OutgoingMail, UUID> {
    @Query(
            value = """
                       UPDATE outgoing_mail
                          SET status = 'IN_PROGRESS'
                        WHERE id IN (
                    			SELECT id
                    			  FROM outgoing_mail
                    			 WHERE status = 'QUEUED'
                    			 LIMIT 1
                    		FOR UPDATE
                        )
                    	RETURNING *;
                    """,
            nativeQuery = true
    )
    Collection<OutgoingMail> lockAndList();
}
