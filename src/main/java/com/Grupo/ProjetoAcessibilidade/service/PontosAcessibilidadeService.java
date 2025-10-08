package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.*;
import com.Grupo.ProjetoAcessibilidade.repository.PontosAcessibilidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontosAcessibilidadeService {

    private final PontosAcessibilidadeRepository pontosAcessibilidadeRepository;
    private final UsuarioService usuarioService;
    private final TipoAcessibilidadeService tipoAcessibilidadeService;
    private final TipoPontoService tipoPontoService;
    private final PontoService pontoService;

    public PontosAcessibilidadeService(PontosAcessibilidadeRepository pontosAcessibilidadeRepository,
                                       UsuarioService usuarioService,
                                       TipoAcessibilidadeService tipoAcessibilidadeService,
                                       TipoPontoService tipoPontoService,
                                       PontoService pontoService) {
        this.pontosAcessibilidadeRepository = pontosAcessibilidadeRepository;
        this.usuarioService = usuarioService;
        this.tipoAcessibilidadeService = tipoAcessibilidadeService;
        this.tipoPontoService = tipoPontoService;
        this.pontoService = pontoService;
    }

    public List<PontosAcessibilidade> listar() {
        return pontosAcessibilidadeRepository.findAll();
    }

    public PontosAcessibilidade buscarPorId(String id) {
        return pontosAcessibilidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Ponto de Acessibilidade não encontrado com ID: " + id));
    }

    public PontosAcessibilidade salvar(PontosAcessibilidadeDTO dto) {

        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());
        TipoAcessibilidade tipoAcessibilidade = tipoAcessibilidadeService.buscarPorId(dto.tipoAcessibilidadeId());
        TipoPonto tipoPonto = tipoPontoService.buscarPorId(dto.tipoPontoId());
        Ponto ponto = pontoService.buscarPorId(dto.pontoId());

        PontosAcessibilidade novoPonto = new PontosAcessibilidade();

        novoPonto.setStatus(dto.status());
        novoPonto.setUsuario(usuario);
        novoPonto.setTipoAcessibilidade(tipoAcessibilidade);
        novoPonto.setTipoPonto(tipoPonto);
        novoPonto.setPonto(ponto);

        return pontosAcessibilidadeRepository.save(novoPonto);
    }

    public PontosAcessibilidade atualizar(String id, PontosAcessibilidadeDTO dto) {
        PontosAcessibilidade pontoExistente = buscarPorId(id);

        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());
        TipoAcessibilidade tipoAcessibilidade = tipoAcessibilidadeService.buscarPorId(dto.tipoAcessibilidadeId());
        TipoPonto tipoPonto = tipoPontoService.buscarPorId(dto.tipoPontoId());
        Ponto ponto = pontoService.buscarPorId(dto.pontoId());

        pontoExistente.setStatus(dto.status());
        pontoExistente.setUsuario(usuario);
        pontoExistente.setTipoAcessibilidade(tipoAcessibilidade);
        pontoExistente.setTipoPonto(tipoPonto);
        pontoExistente.setPonto(ponto);

        return pontosAcessibilidadeRepository.save(pontoExistente);
    }

    public void deletar(String id) {
        PontosAcessibilidade pontoParaDeletar = buscarPorId(id);
        pontosAcessibilidadeRepository.delete(pontoParaDeletar);
    }
}