package net.dancier.dancer.authentication.repository;

import net.dancier.dancer.authentication.model.EmailValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailValidationCodeRepository extends JpaRepository<EmailValidationCode, UUID> {
    Optional<EmailValidationCode> findByCode(String code);
}
