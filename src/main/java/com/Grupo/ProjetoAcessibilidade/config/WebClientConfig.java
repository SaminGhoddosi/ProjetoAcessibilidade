package com.Grupo.ProjetoAcessibilidade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    // Pega a URL do seu arquivo application.properties
    @Value("${valhalla.api.url}")
    private String valhallaBaseUrl;

    @Bean
    public WebClient valhallaWebClient() {
        return WebClient.builder()
                .baseUrl(valhallaBaseUrl) // Usa a URL configurada
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
