package com.Grupo.ProjetoAcessibilidade.controller;

import com.Grupo.ProjetoAcessibilidade.DTO.FeedbackDTO;
import com.Grupo.ProjetoAcessibilidade.DTO.FeedbackResponseDTO; // Importe o DTO de resposta
import com.Grupo.ProjetoAcessibilidade.DTO.UserInfoDTO; // Importe o DTO aninhado
import com.Grupo.ProjetoAcessibilidade.model.Feedback;
import com.Grupo.ProjetoAcessibilidade.service.FeedbackService;
import jakarta.validation.Valid; // Importe o @Valid
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> criar(@Valid @RequestBody FeedbackDTO dto) {
        Feedback novoFeedback = service.salvar(dto);

        // Constrói a URL do recurso criado (ex: /feedbacks/uuid-gerado)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoFeedback.getId())
                .toUri();

        FeedbackResponseDTO responseDTO = toResponseDTO(novoFeedback);
        return ResponseEntity.created(location).body(responseDTO);
    }

    // 2. Listar: Retorna uma lista de DTOs de resposta
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> listar() {
        List<Feedback> feedbacks = service.listar();
        List<FeedbackResponseDTO> responseDTOs = feedbacks.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // 3. Buscar por ID: Tipo de ID corrigido e lógica simplificada
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> buscarPorId(@PathVariable String id) {
        Feedback feedback = service.buscarPorId(id);
        return ResponseEntity.ok(toResponseDTO(feedback));
    }

    // 4. Atualizar: Tipo de ID corrigido e retorna DTO de resposta
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> atualizar(@PathVariable String id, @Valid @RequestBody FeedbackDTO dto) {
        Feedback feedbackAtualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(toResponseDTO(feedbackAtualizado));
    }

    // 5. Deletar: Tipo de ID corrigido e resposta 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // 6. Método auxiliar para converter Entidade em DTO de Resposta
    private FeedbackResponseDTO toResponseDTO(Feedback feedback) {
        UserInfoDTO userInfo = new UserInfoDTO(feedback.getUsuario().getId().toString(), feedback.getUsuario().getNome());
        return new FeedbackResponseDTO(
                feedback.getId().toString(),
                feedback.getComentario(),
                feedback.getAvaliacao(),
                feedback.getPontosAcessibilidade().getId().toString(),
                userInfo
        );
    }
}