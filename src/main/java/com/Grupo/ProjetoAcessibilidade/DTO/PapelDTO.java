package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.model.Usuario;

import java.util.List;

public record PapelDTO(String nome, List<Usuario> usuarios) {
}
