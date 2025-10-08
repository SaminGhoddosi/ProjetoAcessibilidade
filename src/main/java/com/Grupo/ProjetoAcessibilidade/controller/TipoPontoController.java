package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.TipoPontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.service.TipoPontoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipos-ponto")
public class TipoPontoController {

    private final TipoPontoService tipoPontoService;

    public TipoPontoController(TipoPontoService tipoPontoService) {
        this.tipoPontoService = tipoPontoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoPonto>> listar() {
        return ResponseEntity.ok(tipoPontoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPonto> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(tipoPontoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoPonto> criar(@Valid @RequestBody TipoPontoDTO dto) {
        TipoPonto tipoPonto = tipoPontoService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/tipos-ponto/" + tipoPonto.getId()))
                .body(tipoPonto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<TipoPonto> atualizar(@PathVariable String id,
                                               @Valid @RequestBody TipoPontoDTO dto) {
        return ResponseEntity.ok(tipoPontoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        tipoPontoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}