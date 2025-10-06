package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.service.RotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rotas")
public class RotaController {

    private final RotaService rotaService;

    public RotaController(RotaService rotaService){
        this.rotaService = rotaService;
    }

    @GetMapping
    public ResponseEntity<List<Rota>> listarRotas() {
        List<Rota> rotas = rotaService.listarTodasAsRotas();
        return ResponseEntity.ok(rotas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rota> buscarRotaPorId(@PathVariable Long id) {
        Rota rota = rotaService.buscarRotaPorId(id);
        return ResponseEntity.ok(rota);
    }

    @PostMapping
    public ResponseEntity<Rota> criarRota(@RequestBody Rota rota) {
        Rota novaRota = rotaService.criarRota(rota);
        return new ResponseEntity<>(novaRota, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rota> atualizarRota(@PathVariable Long id, @RequestBody Rota rotaDetalhes) {
        Rota rotaAtualizada = rotaService.atualizarRota(id, rotaDetalhes);
        return ResponseEntity.ok(rotaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRota(@PathVariable Long id) {
        rotaService.deletarRota(id);
        return ResponseEntity.noContent().build();
    }

}
