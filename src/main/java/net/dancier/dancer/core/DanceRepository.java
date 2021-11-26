package net.dancier.dancer.core;

import net.dancier.dancer.core.model.Dance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DanceRepository extends JpaRepository<Dance, UUID> {
}
