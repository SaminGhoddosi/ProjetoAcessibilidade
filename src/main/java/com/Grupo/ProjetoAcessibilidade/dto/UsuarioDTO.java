package com.Grupo.ProjetoAcessibilidade.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioDTO(
        @NotBlank String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank

        String cpf,

        @NotNull
        LocalDate dataNascimento,


        String telefone,

        @NotBlank
        @Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres")
        String senha,

        @NotNull
        String papelId
) {
}
