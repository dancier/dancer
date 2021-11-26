package net.dancier.dancer.core;

import lombok.Data;
import net.dancier.dancer.core.model.Leading;
import net.dancier.dancer.core.model.Level;

@Data
public class DanceProfileDto {

    private String dance;
    private Level level;
    private Leading leading;

}
