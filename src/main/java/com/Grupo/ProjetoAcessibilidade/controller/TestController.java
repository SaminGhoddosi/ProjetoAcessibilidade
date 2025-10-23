package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
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


    @GetMapping("/test-acessibilidade")
    public Mono<ResponseEntity<String>> testRoute() {

        Ponto start = new PontoDTO("Inicio Teste", -26.917070427788904, -49.071228808912174).toPonto(); // <-- CORRIGIDO
        Ponto end = new PontoDTO("Fim Teste", -26.917364182605645, -49.07095833737201).toPonto();

        System.out.println("Endpoint /test-acessibilidade foi chamado!");

        return valhallaService.getAccessibleRoute(start, end)
                .map(ResponseEntity::ok)
                .doOnError(error -> System.err.println("Erro no controller: " + error.getMessage()));
    }
}