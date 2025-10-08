package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.service.PontosAcessibilidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pontos-acessibilidade")
public class PontosAcessibilidadeController {

    private final PontosAcessibilidadeService pontosAcessibilidadeService;

    public PontosAcessibilidadeController(PontosAcessibilidadeService pontosAcessibilidadeService) {
        this.pontosAcessibilidadeService = pontosAcessibilidadeService;
    }

    @GetMapping
    public ResponseEntity<List<PontosAcessibilidade>> listar() {
        return ResponseEntity.ok(pontosAcessibilidadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontosAcessibilidade> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pontosAcessibilidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PontosAcessibilidade> criar(@Valid @RequestBody PontosAcessibilidadeDTO dto) {
        PontosAcessibilidade ponto = pontosAcessibilidadeService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/pontosAcessibilidade/" + ponto.getId()))
                .body(ponto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontosAcessibilidade> atualizar(@PathVariable String id,
                                                          @Valid @RequestBody PontosAcessibilidadeDTO dto) {
        return ResponseEntity.ok(pontosAcessibilidadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        pontosAcessibilidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}