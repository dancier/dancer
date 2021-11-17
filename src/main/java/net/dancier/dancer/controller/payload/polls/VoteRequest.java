package net.dancier.dancer.controller.payload.polls;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class VoteRequest {
    @NotNull
    private UUID choiceId;

    public UUID getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(UUID choiceId) {
        this.choiceId = choiceId;
    }
}
