package com.Grupo.ProjetoAcessibilidade.dto; // Ou .DTO

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

// Este DTO representa os dados que o frontend enviará para /rotas/calcular
public record CalcularRotaDTO(
        @NotNull Long usuarioId, // ID do usuário que está pedindo a rota
        @NotNull String perfil, // "PADRAO", "CADEIRANTE", "IDOSO"
        @NotNull @Valid CoordenadasDTO coordenadas // Objeto com lat/lon de início e fim
) {

    // Classe interna para agrupar as coordenadas
    public record CoordenadasDTO(
            @NotNull Double latInicio,
            @NotNull Double lonInicio,
            @NotNull Double latFim,
            @NotNull Double lonFim
    ) {}
}