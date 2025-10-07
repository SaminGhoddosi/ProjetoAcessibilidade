package com.Grupo.ProjetoAcessibilidade.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PontoDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @NotNull(message = "A latitude é obrigatória.")
        double latitude,

        @NotNull(message = "A longitude é obrigatória.")
        double longitude) {

}
