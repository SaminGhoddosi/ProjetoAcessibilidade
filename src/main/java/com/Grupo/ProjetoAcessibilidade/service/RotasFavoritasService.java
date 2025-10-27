package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.RotasFavoritasDTO;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.model.RotasFavoritas;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.RotaRepository;
import com.Grupo.ProjetoAcessibilidade.repository.RotasFavoritasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RotasFavoritasService {

    private final RotasFavoritasRepository rotasFavoritasRepository;
    private final RotaRepository rotaRepository;

    public List<RotasFavoritasDTO> listarFavoritas(Usuario usuario) {
        return rotasFavoritasRepository.findByUsuario(usuario)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RotasFavoritasDTO adicionarFavorita(Usuario usuario, String rotaId) {
        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada"));

        rotasFavoritasRepository.findByUsuarioAndRotaId(usuario, rotaId)
                .ifPresent(f -> {
                    throw new RuntimeException("Rota já favoritada por este usuário");
                });

        RotasFavoritas favorita = new RotasFavoritas();
        favorita.setUsuario(usuario);
        favorita.setRota(rota);
        RotasFavoritas salva = rotasFavoritasRepository.save(favorita);
        return toDTO(salva);
    }

    public void removerFavorita(Usuario usuario, String favoritaId) {
        RotasFavoritas favorita = rotasFavoritasRepository.findById(favoritaId)
                .orElseThrow(() -> new RuntimeException("Favorita não encontrada"));

        if (!favorita.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não pode remover a favorita de outro usuário");
        }

        rotasFavoritasRepository.delete(favorita);
    }

    private RotasFavoritasDTO toDTO(RotasFavoritas favorita) {
        return new RotasFavoritasDTO(
                favorita.getId(),
                favorita.getRota().getId(),
                favorita.getRota().getCaminho().toString(),
                favorita.getRota().getCriadoEm()
        );
    }
}

