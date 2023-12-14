package net.dancier.dancer;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * This Helper class is supposed to be used in Integration test to check
 * state in the database that could not be checked with production
 * Repository implementations, because they would have to be implemented their
 * sole for testing purposes.
 * This helper is supposed to run in a Transaction that encapsulates the whole test.
 * For this reason we flush the entityManager before checking the database state,
 * because otherwise it could be, that entities would not have been saved/updated by
 * hibernate.
 */
@Component
@RequiredArgsConstructor
public class TestDatabaseHelper {

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    public int getCountOfUsers() {
        entityManager.flush();
        return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM users;", Integer.class);
    }

    public String getEmailValidationCodeForEmail(String email) {
        entityManager.flush();
        try {
            return jdbcTemplate.queryForObject(
                """
                            SELECT vc."code"
                        	  FROM users
                        INNER JOIN validation_codes AS vc
                                ON users.id = vc.user_id
                        	 WHERE users.email = '""" + email  + "'"
                        , String.class
        );}
        catch (Exception e) {return "";}
    }

}
