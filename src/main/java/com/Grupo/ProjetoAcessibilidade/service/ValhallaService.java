package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.LocationDTO;
import com.Grupo.ProjetoAcessibilidade.DTO.ValhallaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ValhallaService {

    private final WebClient valhallaWebClient;

    public ValhallaService(WebClient valhallaWebClient) {
        this.valhallaWebClient = valhallaWebClient;
    }

    public Mono<String> getAcessibleRoute(LocationDTO start, LocationDTO end) {
        ValhallaRequest request = new ValhallaRequest(List.of(start, end));

        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }
}