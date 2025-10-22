package com.Grupo.ProjetoAcessibilidade.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RotasFavoritasDTO {

    private String id;
    private String rotaId;
    private LocalDateTime criadoEm;
}
