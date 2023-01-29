package com.myauth.mailservice.controller;

import com.myauth.mailservice.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmailControllerTest {

    @MockBean
    private EmailService emailService;
    @Autowired
    private EmailController emailController;

    @Autowired
    private MockMvc mockMvc;

    private final String RESOURCE_PATH="src/test/resources/";

    @Test
    public void sendSimpleMailTest() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.post("/mail/sendSimpleMail")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(Files.readAllBytes(Paths.get(RESOURCE_PATH+"sampl-email.json"))))
              .andExpect(MockMvcResultMatchers.status().isOk())
              .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void sendVerificationMailTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/mail/sendVerificationMail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(Paths.get(RESOURCE_PATH+"verification-email.json"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
