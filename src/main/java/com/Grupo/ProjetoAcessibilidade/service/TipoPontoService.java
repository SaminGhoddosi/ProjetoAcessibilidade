package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.TipoPontoDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.repository.TipoPontoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoPontoService {

    private final TipoPontoRepository tipoPontoRepository;

    public TipoPontoService(TipoPontoRepository tipoPontoRepository) {
        this.tipoPontoRepository = tipoPontoRepository;
    }

    public List<TipoPonto> listar() {
        return tipoPontoRepository.findAll();
    }

    public TipoPonto buscarPorId(String id) {
        return tipoPontoRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Tipo de ponto não encontrado com o ID: " + id));
    }

    public TipoPonto salvar(TipoPontoDTO dto) {
        TipoPonto tipoPonto = new TipoPonto();
        tipoPonto.setTipo(dto.tipo());
        return tipoPontoRepository.save(tipoPonto);
    }

    public TipoPonto atualizar(String id, TipoPontoDTO dto) {
        TipoPonto tipoPonto = buscarPorId(id);
        tipoPonto.setTipo(dto.tipo());
        return tipoPontoRepository.save(tipoPonto);
    }

    public void deletar(String id) {
        TipoPonto tipoPonto = buscarPorId(id);
        tipoPontoRepository.delete(tipoPonto);
    }
}