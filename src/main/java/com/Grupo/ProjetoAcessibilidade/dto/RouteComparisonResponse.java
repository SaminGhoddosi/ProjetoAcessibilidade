package com.Grupo.ProjetoAcessibilidade.dto;

import lombok.Data;

@Data
public class RouteComparisonResponse {

    // --- MUDANÇA AQUI ---
    // Agora enviamos o "tracejado" e o "resumo" separados
    private String accessibleShape;
    private String standardShape;
    private ValhallaResponseDTO.Attributes accessibleSummary;
    private ValhallaResponseDTO.Attributes standardSummary;

    private String comparisonSummary;
    private boolean accessibleRouteFound;

    public RouteComparisonResponse(ValhallaResponseDTO accessible, ValhallaResponseDTO standard) {

        // --- LÓGICA DE LEITURA CORRIGIDA ---
        if (accessible != null && accessible.getTrip() != null) {
            this.accessibleSummary = accessible.getTrip().getAttributes();
            // Pega o "tracejado" da PRIMEIRA "leg" da viagem
            if (accessible.getTrip().getLegs() != null && !accessible.getTrip().getLegs().isEmpty()) {
                this.accessibleShape = accessible.getTrip().getLegs().get(0).getShape();
            }
        }

        if (standard != null && standard.getTrip() != null) {
            this.standardSummary = standard.getTrip().getAttributes();
            if (standard.getTrip().getLegs() != null && !standard.getTrip().getLegs().isEmpty()) {
                this.standardShape = standard.getTrip().getLegs().get(0).getShape();
            }
        }

        // A flag agora checa o 'shape'
        this.accessibleRouteFound = (this.accessibleShape != null && !this.accessibleShape.isEmpty());
        this.comparisonSummary = generateSummary();
    }

    private String generateSummary() {
        boolean standardRouteExists = (standardSummary != null);
        boolean accessibleRouteExists = (accessibleSummary != null);

        if (!standardRouteExists && !accessibleRouteExists) {
            return "Não foi possível calcular nenhuma rota. Verifique os pontos de início e fim.";
        }
        if (!accessibleRouteExists && standardRouteExists) {
            return "AVISO: Não foi possível encontrar uma rota totalmente acessível. Mostrando a rota padrão, que pode conter escadas ou barreiras.";
        }
        if (accessibleRouteExists && !standardRouteExists) {
            return "Rota acessível calculada. (Não foi possível calcular a rota padrão para comparação).";
        }

        // Ambas funcionaram
        long stairsAvoided = standardSummary.getStepsCount() - accessibleSummary.getStepsCount();
        double extraDistanceMeters = (accessibleSummary.getLength() - standardSummary.getLength()) * 1000;

        StringBuilder summary = new StringBuilder("Rota acessível calculada. ");

        if (stairsAvoided > 0) {
            summary.append(String.format("Você está evitando %d lances de escada. ", stairsAvoided));
        } else if (standardSummary.getStepsCount() == 0) {
            summary.append("A rota padrão já parece ser livre de escadas. ");
        }

        if (extraDistanceMeters > 50) {
            summary.append(String.format("Esta rota é %.0f metros mais longa.", extraDistanceMeters));
        } else if (extraDistanceMeters < -50) {
            summary.append(String.format("Esta rota é %.0f metros mais curta!", -extraDistanceMeters));
        }
        return summary.toString();
    }
}