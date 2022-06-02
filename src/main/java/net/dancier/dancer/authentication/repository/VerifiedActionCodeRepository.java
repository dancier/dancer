package net.dancier.dancer.authentication.repository;

import net.dancier.dancer.authentication.model.EmailValidationCode;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.model.VerifiedActionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerifiedActionCodeRepository extends CrudRepository<VerifiedActionCode, UUID> {

    Optional<VerifiedActionCode> findByCode(String code);
    Optional<VerifiedActionCode> findByUserId(UUID userId);

}
