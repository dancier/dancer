package net.dancier.dancer.mail.model;

import org.springframework.mail.SimpleMailMessage;

public class DancierMailMessage extends SimpleMailMessage {

    public void setTo(String[] in) {
        super.setTo(in);
    }
}
