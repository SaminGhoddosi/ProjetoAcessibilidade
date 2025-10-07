package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "pontosAcessibilidade")
public class PontosAcessibilidade {

    @Id
    private String id;

    private String status;

    @CreatedDate
    private LocalDateTime dataCriacao;

    @DBRef
    private TipoAcessibilidade tipoAcessibilidade;

    @DBRef
    private Usuario usuario;

    @DBRef
    private TipoPonto tipoPonto;

    @DBRef
    private Ponto ponto;
}