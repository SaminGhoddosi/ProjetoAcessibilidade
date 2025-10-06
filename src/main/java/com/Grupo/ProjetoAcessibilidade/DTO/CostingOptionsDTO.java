package com.Grupo.ProjetoAcessibilidade.DTO;

public record CostingOptionsDTO(PedestrianOptionsDTO pedestrian) {

    public CostingOptionsDTO() {
        this(new PedestrianOptionsDTO());
    }
}