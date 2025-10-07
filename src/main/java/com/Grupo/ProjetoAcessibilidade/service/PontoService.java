package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.repository.PontoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontoService {

    private final PontoRepository repository;

    public PontoService(PontoRepository repository) {
        this.repository = repository;
    }

    public List<Ponto> listar() {
        return repository.findAll();
    }

    public Ponto buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Ponto não encontrado com ID: " + id));
    }

    public Ponto salvar(PontoDTO dto) {
        Ponto ponto = new Ponto();
        ponto.setNome(dto.nome());
        ponto.setLatitude(dto.latitude());
        ponto.setLongitude(dto.longitude());
        return repository.save(ponto);
    }

    public Ponto atualizar(String id, PontoDTO dto) {
        Ponto pontoExistente = buscarPorId(id);

        pontoExistente.setNome(dto.nome());
        pontoExistente.setLatitude(dto.latitude());
        pontoExistente.setLongitude(dto.longitude());

        return repository.save(pontoExistente);
    }

    public void deletar(String id) {
        Ponto pontoParaDeletar = buscarPorId(id);
        repository.delete(pontoParaDeletar);
    }
}