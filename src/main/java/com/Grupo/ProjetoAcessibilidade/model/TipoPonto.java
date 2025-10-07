package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tiposPonto")
public class TipoPonto {

    @Id
    private String id;

    @Indexed(unique = true)
    private String tipo;
}