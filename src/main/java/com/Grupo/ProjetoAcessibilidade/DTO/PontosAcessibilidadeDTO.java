package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;

import java.time.LocalDateTime;

public record PontosAcessibilidadeDTO(String status, LocalDateTime dataCriacao, TipoAcessibilidade tipoAcessibilidade, Usuario usuario, TipoPonto tipoPonto, Ponto ponto) {
}
