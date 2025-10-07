package com.Grupo.ProjetoAcessibilidade.DTO;
import com.Grupo.ProjetoAcessibilidade.DTO.CostingOptionsDTO;
import com.Grupo.ProjetoAcessibilidade.DTO.LocationDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ValhallaRequest(
        List<LocationDTO> locations,
        String costing,
        @JsonProperty("costing_options") CostingOptionsDTO costingOptions,
        String language
) {
    public ValhallaRequest(List<LocationDTO> locations) {
        this(locations, "pedestrian", new CostingOptionsDTO(), "pt-BR");
    }
}