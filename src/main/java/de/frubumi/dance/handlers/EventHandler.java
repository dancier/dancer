package de.frubumi.dance.handlers;

import de.frubumi.dance.dancer.Dancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static de.frubumi.dance.config.WebSocketConfig.MESSAGE_PREFIX;

@Component
@RepositoryEventHandler
public class EventHandler {

    private final SimpMessagingTemplate websocket;

    private final EntityLinks entityLinks;

    @Autowired
    public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
        this.websocket = websocket;
        this.entityLinks = entityLinks;
    }

    @HandleAfterCreate
    public void newDancer(Dancer dancer) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/newDancer", getPath(dancer));
    }

    @HandleAfterDelete
    public void deleteDancer(Dancer dancer) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/deleteDancer", getPath(dancer));
    }

    @HandleAfterSave
    public void updateDancer(Dancer dancer) {
        this.websocket.convertAndSend(
                MESSAGE_PREFIX + "/updateDancer", getPath(dancer));
    }

    /**
     * Take an {@link Dancer} and get the URI using Spring Data REST's {@link EntityLinks}.
     *
     * @param dancer
     */
    private String getPath(Dancer dancer) {
        return this.entityLinks.linkForItemResource(dancer.getClass(),
                dancer.getId()).toUri().getPath();
    }

}