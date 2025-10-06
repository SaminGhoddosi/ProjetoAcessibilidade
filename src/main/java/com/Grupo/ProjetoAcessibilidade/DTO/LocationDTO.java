package com.Grupo.ProjetoAcessibilidade.DTO;

public record LocationDTO(double lat, double lon, String type) {
    public LocationDTO(double lat, double lon) {
        this(lat, lon, "break");
    }
}
