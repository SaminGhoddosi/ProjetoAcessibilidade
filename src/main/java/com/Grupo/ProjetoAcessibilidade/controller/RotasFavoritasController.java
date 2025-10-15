package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.RotasFavoritasDTO;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.service.RotasFavoritasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rotas/favoritas")
@RequiredArgsConstructor
public class RotasFavoritasController {

    private final RotasFavoritasService rotasFavoritasService;

    private Usuario getUsuarioLogado() {
        return new Usuario(); // placeholder
    }

    @GetMapping
    public List<RotasFavoritasDTO> listarFavoritas() {
        Usuario usuario = getUsuarioLogado();
        return rotasFavoritasService.listarFavoritas(usuario);
    }

    @PostMapping("/{rotaId}")
    public RotasFavoritasDTO adicionarFavorita(@PathVariable String rotaId) {
        Usuario usuario = getUsuarioLogado();
        return rotasFavoritasService.adicionarFavorita(usuario, rotaId);
    }

    @DeleteMapping("/{favoritaId}")
    public void removerFavorita(@PathVariable String favoritaId) {
        Usuario usuario = getUsuarioLogado();
        rotasFavoritasService.removerFavorita(usuario, favoritaId);
    }
}
