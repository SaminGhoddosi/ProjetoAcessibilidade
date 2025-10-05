package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.repository.PontosAcessibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PontosAcessibilidadeService {

    private final PontosAcessibilidadeRepository repository;

    @Autowired
    public PontosAcessibilidadeService(PontosAcessibilidadeRepository repository) {
        this.repository = repository;
    }

    public PontosAcessibilidade salvar(PontosAcessibilidadeDTO dto) {
        PontosAcessibilidade pontoAcessibilidade = new PontosAcessibilidade();
        pontoAcessibilidade.setStatus(dto.status());
        pontoAcessibilidade.setDataCriacao(dto.dataCriacao());
        pontoAcessibilidade.setTipoAcessibilidade(dto.tipoAcessibilidade());
        pontoAcessibilidade.setUsuario(dto.usuario());
        pontoAcessibilidade.setTipoPonto(dto.tipoPonto());
        pontoAcessibilidade.setPonto(dto.ponto());
        return repository.save(pontoAcessibilidade);
    }

    public List<PontosAcessibilidade> listar() {
        return repository.findAll();
    }

    public Optional<PontosAcessibilidade> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public PontosAcessibilidade atualizar(Long id, PontosAcessibilidadeDTO dto) {
        return repository.findById(id).map(pontoAcessibilidade -> {
            pontoAcessibilidade.setStatus(dto.status());
            pontoAcessibilidade.setDataCriacao(dto.dataCriacao());
            pontoAcessibilidade.setTipoAcessibilidade(dto.tipoAcessibilidade());
            pontoAcessibilidade.setUsuario(dto.usuario());
            pontoAcessibilidade.setTipoPonto(dto.tipoPonto());
            pontoAcessibilidade.setPonto(dto.ponto());
            return repository.save(pontoAcessibilidade);
        }).orElseThrow(() -> new RuntimeException("Ponto de acessibilidade não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}