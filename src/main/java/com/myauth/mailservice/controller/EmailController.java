package com.myauth.mailservice.controller;

import com.myauth.mailservice.Dto.MailRequest;
import com.myauth.mailservice.Dto.MailResponse;
import com.myauth.mailservice.Dto.VerificationRequest;
import com.myauth.mailservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @PostMapping("/sendSimpleMail")
    public ResponseEntity<MailResponse> sendSimpleMail(@RequestBody MailRequest mailRequest){

        return emailService.sendMail(mailRequest);
    }

    @PostMapping("/sendVerificationMail")
    public ResponseEntity<MailResponse> sendVerificationMail(@RequestBody VerificationRequest verificationRequest){
        return emailService.sendVerificationMail(verificationRequest);
    }
}
