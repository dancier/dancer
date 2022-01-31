package net.dancier.dancer.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImp implements MailService {

    private static Logger log = LoggerFactory.getLogger(MailServiceImp.class);

    @Override
    public void send(String recipient, String message) {
        log.debug("Sending mail: " + message);
    }
}
