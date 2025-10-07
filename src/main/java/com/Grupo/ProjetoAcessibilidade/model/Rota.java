package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "rotas")
public class Rota {

    @Id
    private String id;

    private String caminho;

    private String duracao;

    private String distancia;

    @CreatedDate
    @Field("criado_em")
    private LocalDateTime criadoEm;

    private List<Ponto> pontos = new ArrayList<>();

}