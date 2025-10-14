package com.Grupo.ProjetoAcessibilidade.dto;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ValhallaRequest(
        List<PontoDTO> locations,
        String costing,
        @JsonProperty("costing_options") CostingOptionsDTO costingOptions,
        String language
) {
    public ValhallaRequest(List<PontoDTO> locations) {
        this(locations, "pedestrian", new CostingOptionsDTO(), "pt-BR");
    }
}