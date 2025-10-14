package com.Grupo.ProjetoAcessibilidade.service;// package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.ValhallaRequest;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaCommunicationException;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class ValhallaService {

    private final WebClient valhallaWebClient;

    public ValhallaService(WebClient valhallaWebClient) {
        this.valhallaWebClient = valhallaWebClient;
    }

    public Mono<String> getAccessibleRoute(Ponto start, Ponto end) {
        ValhallaRequest request = new ValhallaRequest(List.of(
                PontoDTO.fromPonto(start),
                PontoDTO.fromPonto(end)
        ));

        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("Erro na resposta do Valhalla. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
                    return new ValhallaCommunicationException("Erro de comunicação com o serviço Valhalla: " + e.getStatusCode() + e);
                })
                .onErrorMap(e -> !(e instanceof ValhallaCommunicationException), e -> {
                    log.error("Erro de conexão com o Valhalla.", e);
                    return new ValhallaCommunicationException("Não foi possível conectar ao serviço Valhalla."+ e);
                });
    }
}