package com.Grupo.ProjetoAcessibilidade.dto;

import jakarta.validation.Valid;

public record RotaDTO(
        @Valid PontoDTO origem,
        @Valid PontoDTO destino
) {}
