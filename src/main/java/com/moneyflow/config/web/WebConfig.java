package com.moneyflow.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
public class WebConfig {
    @Value("${cors.origins}")
    private String corsOrigins;

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        //Permite origens específicas
        configuration.setAllowedOrigins(Arrays.asList(corsOrigins.split(",")));
        //Permite todos os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS","HEAD"));
        //Permite todos os headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        //Permite credenciais
        configuration.setAllowCredentials(true);
        //Expor headers de resposta (importante para JWT)
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Configurar para todos os endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
