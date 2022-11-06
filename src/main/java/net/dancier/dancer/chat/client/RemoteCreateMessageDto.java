package net.dancier.dancer.chat.client;

import lombok.Data;
import net.dancier.dancer.chat.dto.CreateMessageDto;

import java.util.UUID;

@Data
public class RemoteCreateMessageDto {
    private String text;
    private UUID author;

    public static class RemoteCreateMessageDtoBuilder {
        private String text;
        private UUID author;

        public RemoteCreateMessageDtoBuilder() {}

        public RemoteCreateMessageDtoBuilder fromCreateMessageDto(CreateMessageDto createMessageDto) {
            this.text = createMessageDto.getText();
            return this;
        }

        public RemoteCreateMessageDtoBuilder withAuthor(UUID author) {
            this.author = author;
            return this;
        }

        public RemoteCreateMessageDto build() {
            RemoteCreateMessageDto remoteCreateMessageDto = new RemoteCreateMessageDto();
            remoteCreateMessageDto.text = this.text;
            remoteCreateMessageDto.author = this.author;
            return remoteCreateMessageDto;
        }
    }
}
