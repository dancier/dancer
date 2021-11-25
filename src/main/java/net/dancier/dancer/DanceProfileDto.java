package net.dancier.dancer;

import lombok.Data;
import net.dancier.dancer.model.Leading;
import net.dancier.dancer.model.Level;

@Data
public class DanceProfileDto {

    private String dance;
    private Level level;
    private Leading leading;

}
