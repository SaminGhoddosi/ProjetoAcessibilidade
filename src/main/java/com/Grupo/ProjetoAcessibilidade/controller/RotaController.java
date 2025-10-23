package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.CalcularRotaDTO;
import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.RotaDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaNoRouteFoundException;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.service.RotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/rotas")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService){
        this.rotaService = rotaService;
    }

    @GetMapping
    public ResponseEntity<List<Rota>> listar() {
        List<Rota> rotas = rotaService.listar();
        return ResponseEntity.ok(rotas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> buscarPorId(@PathVariable String id) {
        Rota rota = rotaService.buscarPorId(id);
        return ResponseEntity.ok(rota);
    }

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularRotaAcessivel(@Valid @RequestBody CalcularRotaDTO dtoCalcular) {
        try {
            // Extrai os dados do DTO
            Long usuarioId = dtoCalcular.usuarioId();
            String perfil = dtoCalcular.perfil();
            CalcularRotaDTO.CoordenadasDTO coordenadas = dtoCalcular.coordenadas();

            // Chama o Service, que retorna Mono<String>
            Mono<String> monoResultado = rotaService.calcularRotaAcessivel(
                    usuarioId,
                    perfil,
                    coordenadas
            );

            // *** CORREÇÃO AQUI: Espera o Mono completar e pega a String ***
            // O .block() vai pausar aqui até o Valhalla responder (ou dar timeout).
            // É bom adicionar um timeout para não esperar para sempre.
            String resultadoRota = monoResultado.block(Duration.ofSeconds(30)); // Espera por até 30 segundos

            // Se o .block() retornar null (acontece se o Mono completar vazio), trate como erro
            if (resultadoRota == null) {
                throw new ValhallaNoRouteFoundException("Valhalla não retornou uma rota (resposta vazia).");
            }

            // Retorna a String JSON da rota com sucesso
            return ResponseEntity.ok(resultadoRota);

        } catch (Exception e) {
            // Captura erros do .block() (como timeout) ou erros lançados pelo Service
            System.err.println("Erro no Controller ao calcular rota: " + e.getMessage()); // Log
            return ResponseEntity.internalServerError().body("Erro ao calcular a rota: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Rota> criar(@Valid @RequestBody RotaDTO dto) {
        Rota novaRota = rotaService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/rotas/" + novaRota.getId()))
                .body(novaRota);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rota> atualizar(@PathVariable String id,
                                          @Valid @RequestBody RotaDTO dto) {
        Rota rotaAtualizada = rotaService.atualizar(id, dto);
        return ResponseEntity.ok(rotaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        rotaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
