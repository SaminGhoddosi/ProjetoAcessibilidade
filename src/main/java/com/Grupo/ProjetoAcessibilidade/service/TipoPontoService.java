package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.TipoPontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.repository.TipoPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoPontoService {

    private final TipoPontoRepository repository;

    @Autowired
    public TipoPontoService(TipoPontoRepository repository) {
        this.repository = repository;
    }

    public TipoPonto salvar(TipoPontoDTO dto) {
        TipoPonto tipoPonto = new TipoPonto();
        tipoPonto.setTipo(dto.tipo());
        return repository.save(tipoPonto);
    }

    public List<TipoPonto> listar() {
        return repository.findAll();
    }

    public Optional<TipoPonto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public TipoPonto atualizar(Long id, TipoPontoDTO dto) {
        return repository.findById(id).map(tipoPonto -> {
            tipoPonto.setTipo(dto.tipo());
            return repository.save(tipoPonto);
        }).orElseThrow(() -> new RuntimeException("Tipo de ponto não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}