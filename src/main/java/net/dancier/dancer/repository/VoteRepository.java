package net.dancier.dancer.repository;

import net.dancier.dancer.model.polls.ChoiceVoteCount;
import net.dancier.dancer.model.polls.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    // Constructor Style: https://docs.oracle.com/cd/E12839_01/apirefs.1111/e13946/ejb3_langref.html#ejb3_langref_constructor
    @Query("SELECT NEW net.dancier.dancer.model.polls.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(@Param("pollIds") List<UUID> pollIds);

    @Query("SELECT NEW net.dancier.dancer.model.polls.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id = :pollId GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPollIdGroupByChoiceId(@Param("pollId") UUID pollId);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id in :pollIds")
    List<Vote> findByUserIdAndPollIdIn(@Param("userId") UUID userId, @Param("pollIds") List<UUID> pollIds);

    @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id = :pollId")
    Vote findByUserIdAndPollId(@Param("userId") UUID userId, @Param("pollId") UUID pollId);

    @Query("SELECT COUNT(v.id) from Vote v where v.user.id = :userId")
    long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
    Page<UUID> findVotedPollIdsByUserId(@Param("userId") UUID userId, Pageable pageable);

}
