package com.myauth.mailservice.services;

import com.myauth.mailservice.Dto.MailRequest;
import com.myauth.mailservice.Dto.MailResponse;
import static org.junit.jupiter.api.Assertions.*;

import com.myauth.mailservice.Dto.VerificationRequest;
import com.myauth.mailservice.config.ApplicationProperties;
import com.myauth.mailservice.config.AuthAppServiceConfiguration;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
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
    private ApplicationProperties applicationProperties;
    @MockBean
    private AuthAppServiceConfiguration authAppServiceConfiguration;
    @Autowired
    private EmailService emailService;

    @BeforeEach
    public  void init(){
        ReflectionTestUtils.setField(emailService,"mailSender",mailSender);
        ReflectionTestUtils.setField(emailService,"applicationProperties",applicationProperties);
        ReflectionTestUtils.setField(emailService,"authAppServiceConfiguration",authAppServiceConfiguration);
    }

    @Test
    public void sendMailTest(){
        MailRequest request = new MailRequest();
        request.setTo("test@gmail.com");
        request.setSubject("<h1>test subject<h1>");
        request.setContent("content");
        request.setFrom("from@gmail.com");
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
        Mockito.when(applicationProperties.getAuthAppUrl()).thenReturn("url");
        Mockito.when(authAppServiceConfiguration.getConfirmUserMailEndpoint()).thenReturn("url");
        ResponseEntity<MailResponse> response = emailService.sendMail(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());

    }
    @Test
    public void sendVerificationMailTest(){
        VerificationRequest request = new VerificationRequest();
        request.setTo("test@gmail.com");
        request.setFrom("from@gmail.com");
        request.setToken("token");
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        Mockito.when(mailSender.createMimeMessage()).thenReturn(Mockito.mock(MimeMessage.class));
        Mockito.when(applicationProperties.getAuthAppUrl()).thenReturn("url");
        Mockito.when(authAppServiceConfiguration.getConfirmUserMailEndpoint()).thenReturn("url");
        ResponseEntity<MailResponse> response = emailService.sendVerificationMail(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    public void sendVerificationMailTokenNullTest(){
        VerificationRequest request = new VerificationRequest();
        Mockito.doNothing().when(mailSender).send(ArgumentMatchers.<SimpleMailMessage>any());
        Mockito.when(applicationProperties.getAuthAppUrl()).thenReturn("url");
        Mockito.when(authAppServiceConfiguration.getConfirmUserMailEndpoint()).thenReturn("url");
        ResponseEntity<MailResponse> response = emailService.sendVerificationMail(request);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }
}
