package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.PapelDTO;
import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.service.PapelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/papeis")
public class PapelController {

    private final PapelService papelService;

    public PapelController(PapelService papelService) {
        this.papelService = papelService;
    }


    @GetMapping
    public ResponseEntity<List<Papel>> listar() {
        return ResponseEntity.ok(papelService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Papel> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(papelService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Papel> criar(@Valid @RequestBody PapelDTO dto) {
        Papel papel = papelService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/papeis/" + papel.getId()))
                .body(papel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Papel> atualizar(@PathVariable String id,
                                           @Valid @RequestBody PapelDTO dto) {
        return ResponseEntity.ok(papelService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        papelService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}