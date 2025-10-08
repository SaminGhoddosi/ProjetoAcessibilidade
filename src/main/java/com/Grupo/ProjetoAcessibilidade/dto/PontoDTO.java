package com.Grupo.ProjetoAcessibilidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PontoDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @NotNull(message = "A latitude é obrigatória.")
        double latitude,

        @NotNull(message = "A longitude é obrigatória.")
        double longitude) {

}
