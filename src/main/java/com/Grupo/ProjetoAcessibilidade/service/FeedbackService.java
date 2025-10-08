package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.FeedbackDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Feedback;
import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PontosAcessibilidadeService pontosAcessibilidadeService;
    private final UsuarioService usuarioService;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           PontosAcessibilidadeService pontosAcessibilidadeService,
                           UsuarioService usuarioService) {
        this.feedbackRepository = feedbackRepository;
        this.pontosAcessibilidadeService = pontosAcessibilidadeService;
        this.usuarioService = usuarioService;
    }

    public List<Feedback> listar() {
        return feedbackRepository.findAll();
    }

    public Feedback buscarPorId(String id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Feedback não encontrado com ID: " + id));
    }

    public Feedback salvar(FeedbackDTO dto) {
        Feedback feedback = new Feedback();

        PontosAcessibilidade pontosAcessibilidade = pontosAcessibilidadeService.buscarPorId(dto.pontoAcessibilidadeId());
        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());

        feedback.setComentario(dto.comentario());
        feedback.setAvaliacao(dto.avaliacao());
        feedback.setPontosAcessibilidade(pontosAcessibilidade);
        feedback.setUsuario(usuario);

        return feedbackRepository.save(feedback);
    }


    public Feedback atualizar(String id, FeedbackDTO dto) {
        Feedback feedback = buscarPorId(id);

        PontosAcessibilidade pontosAcessibilidade = pontosAcessibilidadeService.buscarPorId(dto.pontoAcessibilidadeId());
        Usuario usuario = usuarioService.buscarPorId(dto.usuarioId());

        feedback.setComentario(dto.comentario());
        feedback.setAvaliacao(dto.avaliacao());
        feedback.setPontosAcessibilidade(pontosAcessibilidade);
        feedback.setUsuario(usuario);

        return feedbackRepository.save(feedback);
    }

    public void deletar(String id) {
        Feedback feedback = buscarPorId(id);
        feedbackRepository.delete(feedback);
    }
}