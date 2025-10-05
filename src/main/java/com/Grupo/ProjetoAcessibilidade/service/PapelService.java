package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.PapelDTO;
import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.repository.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PapelService {

    private final PapelRepository repository;

    @Autowired
    public PapelService(PapelRepository repository) {
        this.repository = repository;
    }

    public Papel salvar(PapelDTO dto) {
        Papel papel = new Papel();
        papel.setNome(dto.nome());
        return repository.save(papel);
    }

    public List<Papel> listar() {
        return repository.findAll();
    }

    public Optional<Papel> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Papel atualizar(Long id, PapelDTO dto) {
        return repository.findById(id).map(papel -> {
            papel.setNome(dto.nome());
            return repository.save(papel);
        }).orElseThrow(() -> new RuntimeException("Papel não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}