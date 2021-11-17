package net.dancier.dancer.model.polls;

import java.util.UUID;

public class ChoiceVoteCount {
    private UUID choiceId;
    private Long voteCount;

    public ChoiceVoteCount(UUID choiceId, Long voteCount) {
        this.choiceId = choiceId;
        this.voteCount = voteCount;
    }

    public UUID getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(UUID choiceId) {
        this.choiceId = choiceId;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
