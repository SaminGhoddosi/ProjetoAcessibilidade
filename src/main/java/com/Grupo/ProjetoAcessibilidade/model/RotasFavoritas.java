package com.Grupo.ProjetoAcessibilidade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rotas_favoritas")
public class RotasFavoritas {

    @Id
    private String id;

    @DBRef
    private Usuario usuario;

    @DBRef
    private Rota rota;
}
