package net.dancier.dancer.messaging;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxJpaEntity, UUID> {

    @Query(
            value = """
                       UPDATE outbox
                          SET status = 'IN_PROGRESS'
                        WHERE id IN (
                    			SELECT id
                    			  FROM outbox
                    			 WHERE status = 'NEW'
                    			 LIMIT 1
                    		FOR UPDATE
                        )
                    	RETURNING *;                    """,
            nativeQuery = true
    )
    Collection<OutboxJpaEntity> lockAndList();

}
