package com.myauth.mailservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {

    private String from;
    private String to;
    private String replyTo;
    private String subject;
    private String content;
}
