package com.Grupo.ProjetoAcessibilidade.dto;

import jakarta.validation.constraints.NotBlank;

public record PapelDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome
) {
}
