package net.dancier.dancer.chat.client;

import io.netty.handler.logging.LogLevel;
import net.dancier.dancer.chat.dto.ChatDto;
import net.dancier.dancer.chat.dto.CreateChatDto;
import net.dancier.dancer.chat.dto.CreatedChatDto;
import net.dancier.dancer.chat.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import reactor.util.retry.Retry;

import jakarta.annotation.PostConstruct;
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
        var httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);

        this.webClient = WebClient.builder()
                .baseUrl(host)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(buildRetryExchangeFilterFunction())
                .build();
    }

    public ChatDto[] getChats(UUID dancerId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI)
                        .queryParam("participantId", dancerId)
                        .build()
                )
                .retrieve()
                .bodyToMono(ChatDto[].class)
                .block();
    }

    public CreatedChatDto createChat(CreateChatDto createChatDto) {
        log.info("now creating");
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI)
                        .build()
                )
                .body(Mono.just(createChatDto), CreateChatDto.class)
                .retrieve()
                .bodyToMono(CreatedChatDto.class)
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

    public MessageDto[] getMessages(UUID chatId, UUID dancerId, Optional<UUID> lastMessageId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/{chatId}/messages")
                        .queryParam("dancer_id", dancerId) // this parameter seems to make no sense
                        .queryParamIfPresent("last_message_id", lastMessageId)
                        .build(chatId)
                )
                .retrieve()
                .bodyToMono(MessageDto[].class)
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

    public Void setReadFlag(UUID messageId, UUID participantId, Boolean read) {
        SetReadFlagRequestDto setReadFlagRequestDto = new SetReadFlagRequestDto();
        setReadFlagRequestDto.setRead(read);

        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/messages/{messagesId}/read-by/{participantId}").build(messageId, participantId)
                ).body(Mono.just(setReadFlagRequestDto), SetReadFlagRequestDto.class)
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

