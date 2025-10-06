// package com.Grupo.ProjetoAcessibilidade.service;
// ... imports ...

import com.Grupo.ProjetoAcessibilidade.DTO.ValhallaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValhallaService {

    @Autowired
    private WebClient valhallaWebClient;

    public Mono<String> getAcessibleRoute(Location start, Location end) {

        ValhallaRequest request = new ValhallaRequest(List.of(start, end));

        // O resto do código permanece igual
        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }
}