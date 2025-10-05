package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.DTO.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.service.PontosAcessibilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pontos-acessibilidade")
public class PontosAcessibilidadeController {

    private final PontosAcessibilidadeService service;

    public PontosAcessibilidadeController(PontosAcessibilidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PontosAcessibilidade> criar(@RequestBody PontosAcessibilidadeDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PontosAcessibilidade>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PontosAcessibilidade> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PontosAcessibilidade> atualizar(@PathVariable Long id, @RequestBody PontosAcessibilidadeDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}