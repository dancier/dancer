package net.dancier.dancer.repository;

import net.dancier.dancer.model.ValidationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValidationCodeRepository extends JpaRepository<ValidationCode, UUID> {
    Optional<ValidationCode> findByCode(String code);
}
