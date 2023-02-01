package com.myauth.mailservice.services;

import com.myauth.mailservice.Dto.MailRequest;
import com.myauth.mailservice.Dto.MailResponse;
import com.myauth.mailservice.Dto.VerificationRequest;
import com.myauth.mailservice.config.ApplicationProperties;
import com.myauth.mailservice.config.AuthAppServiceConfiguration;
import com.myauth.mailservice.exceptions.InvalidTokenException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final AuthAppServiceConfiguration authAppServiceConfiguration;
    private final ApplicationProperties applicationProperties;
    private final JavaMailSender mailSender;


    public ResponseEntity<MailResponse> sendMail(MailRequest request){
        MailResponse mailResponse = new MailResponse();
        ResponseEntity<MailResponse> response;
        request.setContent("<h1> How ya doin! ?</h1>");
        try{
            mailSender.send(createMail(request));
            mailResponse.setMessage("Email Sent Succesfully to: " +request.getTo());
            response = new ResponseEntity<>(mailResponse,HttpStatus.OK);
            log.debug("Email Sent Successfully to: " +request.getTo());
        }catch( MailException | MessagingException e){
            log.error("Cannot Send Email to: " +request.getTo());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<MailResponse> sendVerificationMail(VerificationRequest request){
         MailRequest mailRequest = new MailRequest();
        MailResponse mailResponse = new MailResponse();
        ResponseEntity<MailResponse> response;
        BeanUtils.copyProperties(request,mailRequest);
        StringBuilder url = new StringBuilder();
        url.append(applicationProperties.getAuthAppUrl()).append(authAppServiceConfiguration.getConfirmUserMailEndpoint());
        try{
            if(request.getToken()!=null && !ObjectUtils.isEmpty(request.getToken())){
                mailRequest.setSubject("Verification email");
                mailRequest.setContent("<p>Hi User</p><p>Click on this verification link to confirm</p><p><a href=\""
                        + url + request.getToken()+"\">confirm</a></p>");
                mailSender.send(createMail(mailRequest));
                mailResponse.setMessage("Verification Email Sent Succesfully to: " +request.getTo());
                response = new ResponseEntity<>(mailResponse,HttpStatus.OK);
            }else {
                throw new InvalidTokenException("No token provided");
            }
        }catch (MailException | MessagingException e){
            log.error("Cannot Send Verification Email to: " +request.getTo());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (InvalidTokenException e){
            log.error(e.getMessage());
            mailResponse.setMessage(e.getMessage());
            response = new ResponseEntity<>(mailResponse,HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private MimeMessage createMail(MailRequest request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(request.getContent(), true);
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setFrom(request.getFrom());
        return mimeMessage;
    }
}
