package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.LocationDTO;
import com.Grupo.ProjetoAcessibilidade.service.ValhallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

// 1. @RestController: Anotação que transforma esta classe em um Controller de API.
@RestController
public class TestController {

    @Autowired
    private ValhallaService valhallaService;

    // 2. @GetMapping: Anotação que mapeia a URL para o método.
    //    É AQUI que o nome "test-acessibilidade" foi definido.
    @GetMapping("/test-acessibilidade")
    public Mono<ResponseEntity<String>> testRoute() {
        // Pontos de exemplo
        LocationDTO start = new LocationDTO(-26.917070427788904, -49.071228808912174);
        LocationDTO end = new LocationDTO(-26.917364182605645, -49.07095833737201);

        System.out.println("Endpoint /test-acessibilidade foi chamado!");

        return valhallaService.getAcessibleRoute(start, end)
                .map(responseBody -> ResponseEntity.ok(responseBody))
                .doOnError(error -> System.err.println("Erro no controller: " + error.getMessage()));
    }
}