package com.example.uniqueidgenerator.config;

import com.example.uniqueidgenerator.model.AppPlacement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppPlacementConfig {

    @Bean
    public AppPlacement appPlacement() {
        AppPlacement appPlacement = new AppPlacement();
        log.info("AppPlacement: {}", appPlacement.toString());
        return appPlacement;
    }

}
