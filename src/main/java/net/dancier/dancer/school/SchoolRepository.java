package net.dancier.dancer.school;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SchoolRepository extends CrudRepository<School, UUID> {

    Optional<School> findByUserId(UUID userid);
}
