package net.dancier.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private User from;
    private User to;
    private String content;
    private Date timeStamp;
}
