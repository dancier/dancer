package net.dancier.dancer.core;

import net.dancier.dancer.core.model.Dancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DancerRepository extends JpaRepository<Dancer, UUID> {

    Optional<Dancer> findByUserId(UUID uuid);

    Boolean existsByDancerName(String dancerName);

}
