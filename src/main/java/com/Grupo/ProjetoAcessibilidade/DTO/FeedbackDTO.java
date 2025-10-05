package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;

public record FeedbackDTO(String comentario, String avaliacao, PontosAcessibilidade pontosAcessibilidade, Usuario usuario) {
}
