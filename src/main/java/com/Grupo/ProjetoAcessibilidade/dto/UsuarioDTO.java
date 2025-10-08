package com.Grupo.ProjetoAcessibilidade.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

public record UsuarioDTO(
        @NotBlank String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @CPF
        String cpf,

        @NotNull
        LocalDateTime dataNascimento,

        // regex para phone
        String telefone,

        @NotBlank
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String senha,

        @NotNull
        String papelId // 💡 Apenas o ID do papel é necessário
) {
}