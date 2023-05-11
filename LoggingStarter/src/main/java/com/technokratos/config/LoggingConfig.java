package com.technokratos.config;

import com.technokratos.aspect.LoggingAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoggingConfig {

    @Bean
    public LoggingAspect methodLogAspect() {
        return new LoggingAspect();
    }
}
