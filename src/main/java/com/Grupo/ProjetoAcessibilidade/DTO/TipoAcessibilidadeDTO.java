package com.Grupo.ProjetoAcessibilidade.DTO;

import jakarta.validation.constraints.NotBlank;

public record TipoAcessibilidadeDTO(
        @NotBlank(message = "O tipo não pode estar em branco")
        String tipo
) {
}
