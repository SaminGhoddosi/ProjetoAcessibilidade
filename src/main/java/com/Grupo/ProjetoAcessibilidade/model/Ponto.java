package com.Grupo.ProjetoAcessibilidade.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pontos")
public class Ponto {

    @Id
    private String id;

    private String nome;
    private double latitude;
    private double longitude;

}