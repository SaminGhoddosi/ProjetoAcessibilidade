package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.service.PontoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pontos")
public class PontoController {

    private final PontoService pontoService;

    public PontoController(PontoService pontoService) {
        this.pontoService = pontoService;
    }

    @GetMapping
    public ResponseEntity<List<Ponto>> listar() {
        return ResponseEntity.ok(pontoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ponto> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pontoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Ponto> criar(@Valid @RequestBody PontoDTO dto) {
        Ponto ponto = pontoService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/pontos/" + ponto.getId()))
                .body(ponto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ponto> atualizar(@PathVariable String id,
                                           @Valid @RequestBody PontoDTO dto) {
        return ResponseEntity.ok(pontoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        pontoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}