package net.dancier.dancer.core.events;

import lombok.Builder;
import lombok.Data;
import net.dancier.dancer.core.model.Dancer;

@Data
@Builder
public class ProfileUpdatedEvent {

    private Dancer dancer;

}
