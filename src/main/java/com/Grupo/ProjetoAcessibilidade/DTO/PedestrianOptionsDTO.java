package com.Grupo.ProjetoAcessibilidade.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PedestrianOptionsDTO(@JsonProperty("walking_speed") int walkingSpeed,
                                   @JsonProperty("step_penalty") int stepPenalty,
                                   @JsonProperty("use_hills") double useHills,
                                   @JsonProperty("use_stairs") double useStairs,
                                   @JsonProperty("max_hiking_difficulty") int maxHikingDifficulty
) {
    public PedestrianOptionsDTO() {
        this(4, 3000, 0.1, 0.01, 1);
    }
}
