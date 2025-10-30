package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
// Removi a importação não utilizada: import com.Grupo.ProjetoAcessibilidade.dto.ValhallaRequest;
import com.Grupo.ProjetoAcessibilidade.dto.ValhallaResponseDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaCommunicationException;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList; // <-- Adicionado Import
import java.util.HashMap;   // <-- Adicionado Import
import java.util.List;
import java.util.Map;       // <-- Adicionado Import
import java.util.stream.Collectors; // <-- Adicionado Import

@Slf4j
@Service
public class ValhallaService {

    private final WebClient valhallaWebClient;

    public ValhallaService(WebClient valhallaWebClient) {
        this.valhallaWebClient = valhallaWebClient;
    }


    /**
     * Busca uma rota no Valhalla, agora retornando um Objeto DTO
     * e pedindo os atributos de acessibilidade (como contagem de escadas).
     */
    public Mono<ValhallaResponseDTO> getRoute(Map<String, Object> requestBody) {

        // --- ADIÇÃO IMPORTANTE ---
        // Pede ao Valhalla para contar os atributos na rota
        requestBody.put("attributes", List.of(
                "shape",
                "length",
                "time",
                "steps_count"
        ));

        requestBody.put("id", requestBody.get("costing") + "_route_request");

        log.info("Enviando requisição (CORRIGIDA) para Valhalla: {}", requestBody);

        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ValhallaResponseDTO.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("Erro na resposta do Valhalla. Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return new ValhallaCommunicationException("Erro de comunicação com o serviço Valhalla: " + e.getStatusCode());
                })
                .onErrorMap(e -> !(e instanceof ValhallaCommunicationException), e -> {
                    log.error("Erro de conexão com o Valhalla.", e);
                    return new ValhallaCommunicationException("Não foi possível conectar ao serviço Valhalla.");
                });
    }



    /**
     * Método original para buscar uma rota simples entre dois pontos.
     * Pode ser mantido ou removido se não for mais necessário.
     */
    public Mono<String> getAccessibleRoute(Ponto start, Ponto end) {
        // Usa o ValhallaRequest DTO original se ele for útil apenas para locations
        // Se ValhallaRequest for apenas List<PontoDTO>, podemos simplificar:
        List<PontoDTO> locationsSimple = List.of(
                PontoDTO.fromPonto(start),
                PontoDTO.fromPonto(end)
        );
        Map<String, Object> requestSimpleBody = Map.of(
                "locations", locationsSimple,
                "costing", "pedestrian"
        );


        log.info("Enviando requisição simples para Valhalla: {}", requestSimpleBody);
        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(requestSimpleBody) // Envia o Map diretamente
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.error("Erro na resposta do Valhalla (simples). Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return new ValhallaCommunicationException("Erro de comunicação com o serviço Valhalla: " + e.getStatusCode());
                })
                .onErrorMap(e -> !(e instanceof ValhallaCommunicationException), e -> {
                    log.error("Erro de conexão com o Valhalla (simples).", e);
                    return new ValhallaCommunicationException("Não foi possível conectar ao serviço Valhalla.");
                });
    }

    /**
     * NOVO MÉTODO: Busca uma rota considerando opções de custo (perfil) e waypoints.
     */
    public Mono<String> getAccessibleRouteWithOptions(Ponto start, Ponto end, Map<String, Object> costingOptions, List<Ponto> waypoints) {

        // 1. Montar a lista completa de locations: início + waypoints + fim
        List<PontoDTO> locations = new ArrayList<>();
        locations.add(PontoDTO.fromPonto(start)); // Ponto inicial

        // Adiciona os waypoints (convertendo Ponto para PontoDTO)
        if (waypoints != null && !waypoints.isEmpty()) {
            locations.addAll(waypoints.stream()
                    .map(PontoDTO::fromPonto) // Converte cada Ponto para PontoDTO
                    .collect(Collectors.toList()));
        }

        locations.add(PontoDTO.fromPonto(end)); // Ponto final

        // 2. Montar o corpo completo da requisição como um Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("locations", locations);
        requestBody.put("costing", "pedestrian"); // O modo base é sempre pedestrian

        // Adiciona as opções de custo customizadas SE elas existirem
        if (costingOptions != null && !costingOptions.isEmpty()) {
            // A estrutura esperada pelo Valhalla é {"costing_options": {"pedestrian": { ...opções... }}}
            requestBody.put("costing_options", Map.of("pedestrian", costingOptions));
        }

        log.info("Enviando requisição com opções para Valhalla: {}", requestBody);

        // 3. Fazer a chamada POST com o corpo completo
        return valhallaWebClient.post()
                .uri("/route")
                .bodyValue(requestBody) // Envia o Map com a estrutura completa
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class, e -> {
                    // Log detalhado do erro
                    log.error("Erro na resposta do Valhalla (com opções). Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
                    return new ValhallaCommunicationException("Erro de comunicação com o serviço Valhalla: " + e.getStatusCode());
                })
                .onErrorMap(e -> !(e instanceof ValhallaCommunicationException), e -> {
                    // Log detalhado do erro
                    log.error("Erro de conexão com o Valhalla (com opções).", e);
                    return new ValhallaCommunicationException("Não foi possível conectar ao serviço Valhalla.");
                });
    }


}