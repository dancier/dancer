package net.dancier.service;

import net.dancier.domain.Message;
import net.dancier.domain.User;

import java.util.List;

public interface MessageService {

    List<User> getCommunicationPartners(User user);

    List<Message> getCommunicationHistory(User userA, User userB);

}