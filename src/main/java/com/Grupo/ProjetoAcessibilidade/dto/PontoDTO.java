package com.Grupo.ProjetoAcessibilidade.dto;

import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PontoDTO(
        @JsonProperty("lat") double latitude,
        @JsonProperty("lon") double longitude
) {
    public static PontoDTO fromPonto(Ponto ponto) {
        return new PontoDTO(ponto.getLatitude(), ponto.getLongitude());
    }

    public Ponto toPonto() {
        Ponto ponto = new Ponto();
        ponto.setLatitude(this.latitude);
        ponto.setLongitude(this.longitude);
        return ponto;
    }
}