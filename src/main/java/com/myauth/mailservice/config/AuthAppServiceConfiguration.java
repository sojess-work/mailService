package com.myauth.mailservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("auth-app-service")
public class AuthAppServiceConfiguration {

    private String confirmUserMailEndpoint;
}
