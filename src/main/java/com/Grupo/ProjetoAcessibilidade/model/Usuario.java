package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String cpf;

    private LocalDateTime dataNascimento;

    private String telefone;

    private String senha;

    @DBRef
    private Papel papel;

    @DBRef
    private List<Rota> rotasSalvas = new ArrayList<>();

    @DBRef
    private Set<TipoAcessibilidade> tiposAcessibilidade = new HashSet<>();
}