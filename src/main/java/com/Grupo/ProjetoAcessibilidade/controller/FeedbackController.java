package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.dto.FeedbackDTO;
import com.Grupo.ProjetoAcessibilidade.model.Feedback;
import com.Grupo.ProjetoAcessibilidade.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> listar() {
        return ResponseEntity.ok(feedbackService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(feedbackService.buscarPorId(id));
    }


    @PostMapping
    public ResponseEntity<Feedback> criar(@Valid @RequestBody FeedbackDTO dto) {
        Feedback feedback = feedbackService.salvar(dto);
        return ResponseEntity
                .created(URI.create("/feedbacks/" + feedback.getId()))
                .body(feedback);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Feedback> atualizar(@PathVariable String id,
                                              @Valid @RequestBody FeedbackDTO dto) {
        return ResponseEntity.ok(feedbackService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        feedbackService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}