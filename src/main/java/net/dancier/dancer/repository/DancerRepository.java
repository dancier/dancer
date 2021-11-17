package net.dancier.dancer.repository;

import net.dancier.dancer.model.Dancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DancerRepository extends JpaRepository<Dancer, UUID> {
}
