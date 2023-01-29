package com.myauth.mailservice.services;

import com.myauth.mailservice.Dto.MailRequest;
import com.myauth.mailservice.Dto.MailResponse;
import com.myauth.mailservice.Dto.VerificationRequest;
import com.myauth.mailservice.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    private final MessageSource messageSource;

    public ResponseEntity<MailResponse> sendMail(MailRequest request){
        MailResponse mailResponse = new MailResponse();
        ResponseEntity<MailResponse> response;
        try{
            mailSender.send(createMail(request));
            mailResponse.setMessage("Email Sent Succesfully to: " +request.getTo());
            response = new ResponseEntity<>(mailResponse,HttpStatus.OK);
            log.debug("Email Sent Successfully to: " +request.getTo());
        }catch( MailException e){
            log.error("Cannot Send Email to: " +request.getTo());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    public ResponseEntity<MailResponse> sendVerificationMail(VerificationRequest request){
         MailRequest mailRequest = new MailRequest();
        MailResponse mailResponse = new MailResponse();
        ResponseEntity<MailResponse> response;
        BeanUtils.copyProperties(request,mailRequest);
        try{
            if(request.getToken()!=null){
                mailRequest.setSubject("Verification email");
                mailRequest.setContent("Click on this verification link to confirm"
                        + "http://localhost:8080/confirmUser?token=" + request.getToken());
                mailSender.send(createMail(mailRequest));
                mailResponse.setMessage("Verification Email Sent Succesfully to: " +request.getTo());
                response = new ResponseEntity<>(mailResponse,HttpStatus.OK);
            }else {
                throw new InvalidTokenException("No token provided");
            }
        }catch (MailException e){
            log.error("Cannot Send Verification Email to: " +request.getTo());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.BAD_REQUEST);
        }catch (InvalidTokenException e){
            log.error(e.getMessage());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private SimpleMailMessage createMail(MailRequest request){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(request.getFrom());
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());
        return message;
    }
}
