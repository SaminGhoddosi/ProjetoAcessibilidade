package com.Grupo.ProjetoAcessibilidade.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PontoDTO(String nome, String latitude, String longitude) {

}
