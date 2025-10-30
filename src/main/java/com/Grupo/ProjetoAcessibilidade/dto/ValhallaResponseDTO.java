package com.Grupo.ProjetoAcessibilidade.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValhallaResponseDTO {
    private Trip trip;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trip {
        private Summary summary; // O summary principal
        private List<Leg> legs; // A lista de "pernas" da viagem
        private Attributes attributes; // Onde os atributos que pedimos (steps_count) virão
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leg {
        private Summary summary;
        private String shape; // <-- O "TRACEJADO" (shape) ESTÁ AQUI
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Summary {
        // O summary de cada "leg"
        private double length;
        private double time;
    }

    // --- NOVA CLASSE PARA LER OS ATRIBUTOS ---
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {
        @JsonProperty("length")
        private double length; // em km

        @JsonProperty("time")
        private double time; // em segundos

        @JsonProperty("steps_count")
        private long stepsCount; // <-- O CONTADOR DE ESCADAS
    }
}