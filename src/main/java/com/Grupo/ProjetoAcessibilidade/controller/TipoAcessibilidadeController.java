package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.service.TipoAcessibilidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipos-acessibilidade")
public class TipoAcessibilidadeController {

    private final TipoAcessibilidadeService tipoAcessibilidadeService;

    public TipoAcessibilidadeController(TipoAcessibilidadeService tipoAcessibilidadeService) {
        this.tipoAcessibilidadeService = tipoAcessibilidadeService;
    }


    @GetMapping
    public ResponseEntity<List<TipoAcessibilidade>> listar() {
        return ResponseEntity.ok(tipoAcessibilidadeService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAcessibilidade> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(tipoAcessibilidadeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoAcessibilidade> criar(@Valid @RequestBody TipoAcessibilidadeDTO dto) {
        TipoAcessibilidade tipoAcessibilidade = tipoAcessibilidadeService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/tipos-acessibilidade/" + tipoAcessibilidade.getId()))
                .body(tipoAcessibilidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAcessibilidade> atualizar(@PathVariable String id,
                                                        @Valid @RequestBody TipoAcessibilidadeDTO dto) {
        return ResponseEntity.ok(tipoAcessibilidadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        tipoAcessibilidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
//alterado