package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.PapelDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.repository.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PapelService {

    private final PapelRepository repository;

    public PapelService(PapelRepository repository) {
        this.repository = repository;
    }

    public List<Papel> listar() {
        return repository.findAll();
    }

    public Papel buscarPorId(String id) {
        return repository.findById(id).orElseThrow(()-> new ResourceNotFound("Role não encontrado com ID: "+ id));
    }

    public Papel salvar(PapelDTO dto) {
        Papel papel = new Papel();
        papel.setNome(dto.nome());
        return repository.save(papel);
    }

    public Papel atualizar(String id, PapelDTO dto) {
        Papel papel = buscarPorId(id);
        papel.setNome(dto.nome());
        return repository.save(papel);
    }

    public void deletar(String id) {
        Papel papel = buscarPorId(id);
        repository.delete(papel);
    }
}