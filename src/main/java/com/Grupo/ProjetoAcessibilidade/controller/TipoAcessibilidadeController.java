package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.DTO.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.service.TipoAcessibilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-acessibilidade")
public class TipoAcessibilidadeController {

    private final TipoAcessibilidadeService service;

    public TipoAcessibilidadeController(TipoAcessibilidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoAcessibilidade> criar(@RequestBody TipoAcessibilidadeDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<TipoAcessibilidade>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAcessibilidade> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAcessibilidade> atualizar(@PathVariable Long id, @RequestBody TipoAcessibilidadeDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}