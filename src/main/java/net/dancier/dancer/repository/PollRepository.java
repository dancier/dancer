package net.dancier.dancer.repository;

import net.dancier.dancer.model.polls.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PollRepository extends JpaRepository<Poll, UUID> {

    Optional<Poll> findById(UUID pollId);

    Page<Poll> findByCreatedBy(UUID userId, Pageable pageable);

    long countByCreatedBy(UUID userId);

    List<Poll> findByIdIn(List<UUID> pollIds);

    List<Poll> findByIdIn(List<UUID> pollIds, Sort sort);

}
