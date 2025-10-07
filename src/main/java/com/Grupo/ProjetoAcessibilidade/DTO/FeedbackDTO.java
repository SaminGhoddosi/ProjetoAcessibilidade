package com.Grupo.ProjetoAcessibilidade.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackDTO(
        @NotBlank(message = "O comentário não pode estar em branco.")
        String comentario,

        @NotBlank(message = "A avaliação não pode estar em branco.")
        String avaliacao,

        @NotNull(message = "O ID do ponto de acessibilidade é obrigatório.")
        String pontoAcessibilidadeId,

        @NotNull(message = "O ID do usuário é obrigatório.")
        String usuarioId
) {
}