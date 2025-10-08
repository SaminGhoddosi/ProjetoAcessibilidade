package com.Grupo.ProjetoAcessibilidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record PontosAcessibilidadeDTO(

        @NotBlank(message = "O status não pode estar em branco.")
        String status,

        @NotNull(message = "O ID do tipo de acessibilidade é obrigatório.")
        String tipoAcessibilidadeId,

        @NotNull(message = "O ID do usuário que está criando o ponto é obrigatório.")
        String usuarioId,

        @NotNull(message = "O ID do tipo de ponto é obrigatório.")
        String tipoPontoId,

        @NotNull(message = "O ID do ponto (localização) é obrigatório.")
        String pontoId
) {
}