package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.DTO.PedestrianOptionsDTO;

public record CostingOptionsDTO(PedestrianOptionsDTO pedestrian) {
    public CostingOptionsDTO() {
        this(new PedestrianOptionsDTO());
    }
}