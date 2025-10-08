package com.Grupo.ProjetoAcessibilidade.dto;

public record CostingOptionsDTO(PedestrianOptionsDTO pedestrian) {
    public CostingOptionsDTO() {
        this(new PedestrianOptionsDTO());
    }
}