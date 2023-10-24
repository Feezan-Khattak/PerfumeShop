package com.bs.application.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:application.properties")
public class AppProps {

    @Value("${bs.perfume.application.security.signature}")
    private String signature;

    @Value("${spring.mail.username}")
    private String systemMailAddress;

    @Value("${system.user.management.reset.password.redirection.url}")
    private String resetPasswordUrl;
}
