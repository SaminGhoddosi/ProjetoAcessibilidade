package com.Grupo.ProjetoAcessibilidade.DTO;

import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioDTO(String nome, String email, String cpf, LocalDateTime dataNascimento, String telefone, String senha, Papel papel) {
}
