package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "feedbacks")
public class Feedback {

    @Id
    private String id;

    private String comentario;

    private String avaliacao;

    @DBRef
    private PontosAcessibilidade pontosAcessibilidade;

    @DBRef
    private Usuario usuario;
}