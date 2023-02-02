package com.myauth.mailservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

@Configuration
@ConditionalOnProperty(prefix ="mail",name="username")
public class MailConfig {

    @Value("${mail.username}")
     private  String USER_NAME;

    @Value("${mail.password}")
    private String PASSWORD;
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        String userName = new String(Base64.getDecoder().decode(USER_NAME.getBytes(StandardCharsets.UTF_8)));
        mailSender.setUsername(userName);
        String password = new String(Base64.getDecoder().decode(PASSWORD.getBytes(StandardCharsets.UTF_8)));
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
