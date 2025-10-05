package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.repository.PontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PontoService {

    private final PontoRepository repository;

    @Autowired
    public PontoService(PontoRepository repository) {
        this.repository = repository;
    }

    public Ponto salvar(PontoDTO dto) {
        Ponto ponto = new Ponto();
        ponto.setNome(dto.nome());
        ponto.setLatitude(dto.latitude());
        ponto.setLongitude(dto.longitude());
        return repository.save(ponto);
    }

    public List<Ponto> listar() {
        return repository.findAll();
    }

    public Optional<Ponto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Ponto atualizar(Long id, PontoDTO dto) {
        return repository.findById(id).map(ponto -> {
            ponto.setNome(dto.nome());
            ponto.setLatitude(dto.latitude());
            ponto.setLongitude(dto.longitude());
            return repository.save(ponto);
        }).orElseThrow(() -> new RuntimeException("Ponto não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}