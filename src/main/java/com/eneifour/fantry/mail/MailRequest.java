package com.eneifour.fantry.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
    private String to;
    private String subject;
    private String text;
    private String link;
    private String linkTitle;
}
