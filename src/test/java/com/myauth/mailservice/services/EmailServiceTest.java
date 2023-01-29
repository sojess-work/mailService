package com.myauth.mailservice.services;

import com.myauth.mailservice.Dto.MailRequest;
import com.myauth.mailservice.Dto.MailResponse;
import static org.junit.jupiter.api.Assertions.*;

import com.myauth.mailservice.Dto.VerificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmailServiceTest {
    @MockBean
    private  JavaMailSender mailSender;
    @MockBean
    private  MessageSource messageSource;
    @Autowired
    private EmailService emailService;

    @BeforeEach
    public  void init(){
        ReflectionTestUtils.setField(emailService,"mailSender",mailSender);
        ReflectionTestUtils.setField(emailService,"messageSource",messageSource);
    }

    @Test
    public void sendMailTest(){

        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        ResponseEntity<MailResponse> response = emailService.sendMail(new MailRequest());
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }
    @Test
    public void sendVerificationMailTest(){
        VerificationRequest request = new VerificationRequest();
        request.setToken("token");
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        ResponseEntity<MailResponse> response = emailService.sendVerificationMail(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    public void sendVerificationMailTokenNullTest(){
        VerificationRequest request = new VerificationRequest();
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        ResponseEntity<MailResponse> response = emailService.sendVerificationMail(request);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }
}
