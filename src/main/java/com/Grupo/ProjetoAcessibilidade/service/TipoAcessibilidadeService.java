package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.repository.TipoAcessibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoAcessibilidadeService {

    private final TipoAcessibilidadeRepository repository;

    @Autowired
    public TipoAcessibilidadeService(TipoAcessibilidadeRepository repository) {
        this.repository = repository;
    }

    public TipoAcessibilidade salvar(TipoAcessibilidadeDTO dto) {
        TipoAcessibilidade tipoAcessibilidade = new TipoAcessibilidade();
        tipoAcessibilidade.setTipo(dto.tipo());
        return repository.save(tipoAcessibilidade);
    }

    public List<TipoAcessibilidade> listar() {
        return repository.findAll();
    }

    public Optional<TipoAcessibilidade> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public TipoAcessibilidade atualizar(Long id, TipoAcessibilidadeDTO dto) {
        return repository.findById(id).map(tipoAcessibilidade -> {
            tipoAcessibilidade.setTipo(dto.tipo());
            return repository.save(tipoAcessibilidade);
        }).orElseThrow(() -> new RuntimeException("Tipo de acessibilidade não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}