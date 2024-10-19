package com.onlybuns.onlybuns.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.onlybuns.onlybuns.core.misc.AppLogger;
import com.onlybuns.onlybuns.core.misc.EmailParser;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public EmailParser emailParser() {
        return new EmailParser();
    }

    @Bean
    public AppLogger appLogger() {
        return new AppLogger();
    }
}
