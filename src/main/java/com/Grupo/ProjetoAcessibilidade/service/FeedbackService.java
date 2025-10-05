package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.DTO.FeedbackDTO;
import com.Grupo.ProjetoAcessibilidade.model.Feedback;
import com.Grupo.ProjetoAcessibilidade.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    @Autowired
    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public Feedback salvar(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setComentario(dto.comentario());
        feedback.setAvaliacao(dto.avaliacao());
        feedback.setPontosAcessibilidade(dto.pontosAcessibilidade());
        feedback.setUsuario(dto.usuario());
        return repository.save(feedback);
    }

    public List<Feedback> listar() {
        return repository.findAll();
    }

    public Optional<Feedback> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Feedback atualizar(Long id, FeedbackDTO dto) {
        return repository.findById(id).map(feedback -> {
            feedback.setComentario(dto.comentario());
            feedback.setAvaliacao(dto.avaliacao());
            feedback.setPontosAcessibilidade(dto.pontosAcessibilidade());
            feedback.setUsuario(dto.usuario());
            return repository.save(feedback);
        }).orElseThrow(() -> new RuntimeException("Feedback não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}