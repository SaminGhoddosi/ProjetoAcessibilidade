package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.repository.TipoAcessibilidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoAcessibilidadeService {

    private final TipoAcessibilidadeRepository tipoAcessibilidadeRepository;

    public TipoAcessibilidadeService(TipoAcessibilidadeRepository tipoAcessibilidadeRepository) {
        this.tipoAcessibilidadeRepository = tipoAcessibilidadeRepository;
    }

    public TipoAcessibilidade findOrCreate(TipoAcessibilidadeDTO dto) {
        return tipoAcessibilidadeRepository.findByTipo(dto.tipo())
                .orElseGet(() -> {
                    TipoAcessibilidade tipoAcessibilidade = new TipoAcessibilidade();
                    tipoAcessibilidade.setTipo(dto.tipo());
                    return tipoAcessibilidadeRepository.save(tipoAcessibilidade);
                });
    }

    public List<TipoAcessibilidade> listar() {
        return tipoAcessibilidadeRepository.findAll();
    }

    public TipoAcessibilidade buscarPorId(String id) {
        return tipoAcessibilidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Tipo de acessibilidade não encontrado com o ID: " + id));
    }

    public TipoAcessibilidade salvar(TipoAcessibilidadeDTO dto) {
        TipoAcessibilidade tipoAcessibilidade = new TipoAcessibilidade();
        tipoAcessibilidade.setTipo(dto.tipo());
        return tipoAcessibilidadeRepository.save(tipoAcessibilidade);
    }


    public TipoAcessibilidade atualizar(String id, TipoAcessibilidadeDTO dto) {
        TipoAcessibilidade tipoAcessibilidade = buscarPorId(id);
        tipoAcessibilidade.setTipo(dto.tipo());
        return tipoAcessibilidadeRepository.save(tipoAcessibilidade);
    }

    public void deletar(String id) {
        TipoAcessibilidade tipoAcessibilidade = buscarPorId(id);
        tipoAcessibilidadeRepository.delete(tipoAcessibilidade);
    }
}