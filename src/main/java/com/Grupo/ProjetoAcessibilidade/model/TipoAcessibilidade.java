package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "tiposAcessibilidade")
public class TipoAcessibilidade {

    @Id
    private String id;

    @Indexed(unique = true)
    private String tipo;

}