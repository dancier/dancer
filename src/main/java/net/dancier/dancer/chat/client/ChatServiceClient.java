package net.dancier.dancer.chat.client;

import net.dancier.dancer.chat.dto.ChatDto;
import net.dancier.dancer.chat.dto.CreateChatDto;
import net.dancier.dancer.chat.dto.ChatsDto;
import net.dancier.dancer.chat.dto.MessagesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatServiceClient {

    private static Logger log = LoggerFactory.getLogger(ChatServiceClient.class);

    @Value("${app.chatDancer.host}")
    private String host;

    private WebClient webClient;
    static final String BASE_URI = "/chats";

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(host)
                .filter(buildRetryExchangeFilterFunction())
                .build();
    }

    public ChatsDto getChats(UUID dancerId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI)
                        .queryParam("dancerId", dancerId)
                        .build()
                )
                .retrieve()
                .bodyToMono(ChatsDto.class)
                .block();
    }

    public ChatDto createChat(CreateChatDto createChatDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI)
                        .build()
                )
                .body(Mono.just(createChatDto), CreateChatDto.class)
                .retrieve()
                .bodyToMono(ChatDto.class)
                .block();
    }

    public ChatDto getChat(UUID chatId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/{chatId}")
                        .build(chatId)
                )
                .retrieve()
                .bodyToMono(ChatDto.class)
                .block();
    }

    public MessagesDto getMessages(UUID chatId, UUID dancerId, Optional<UUID> lastMessageId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/{chatId}/messages")
                        .queryParam("dancer_id", dancerId) // this parameter seems to make no sense
                        .queryParamIfPresent("last_message_id", lastMessageId)
                        .build(chatId)
                )
                .retrieve()
                .bodyToMono(MessagesDto.class)
                .block();
    }

    public Void createMessage(UUID chatId, RemoteCreateMessageDto remoteCreateMessageDto) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/{chatId}/messages")
                        .build(chatId)
                )
                .body(Mono.just(remoteCreateMessageDto), RemoteCreateMessageDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private ExchangeFilterFunction buildRetryExchangeFilterFunction() {
        return (request, next) -> next.exchange(request)
                .flatMap(
                        clientResponse -> Mono.just(clientResponse)
                                .filter(response -> clientResponse.statusCode().is5xxServerError())
                                .flatMap(response -> clientResponse.createException())
                                .flatMap(Mono::error)
                                .thenReturn(clientResponse)
                )
                .retryWhen(Retry.backoff(3, Duration.ofMillis(500)));
    }

}
