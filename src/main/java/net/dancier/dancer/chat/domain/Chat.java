package net.dancier.dancer.chat.domain;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Chat {

    UUID id;
    String name;

}
