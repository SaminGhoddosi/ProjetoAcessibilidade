package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.RotaDTO;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.service.RotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("rotas")
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
