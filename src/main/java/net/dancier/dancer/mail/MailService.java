package net.dancier.dancer.mail;

public interface MailService {
    void send(String recipient, String message);
}
