package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "rotas")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String caminho;
    private String duracao;
    private String distancia;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL)
    private List<Ponto> pontos = new ArrayList<>();

    @OneToMany (mappedBy = "rota", cascade = CascadeType.ALL)
    private List<Usuario> usuarios = new ArrayList<>();

}
