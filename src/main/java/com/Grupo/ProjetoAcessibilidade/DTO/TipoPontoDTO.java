package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TipoPontoDTO(
        @NotBlank(message = "O tipo não pode estar em branco")
        String tipo
) {
}
