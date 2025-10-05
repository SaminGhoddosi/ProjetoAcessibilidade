package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "TipoPonto")
public class TipoPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @OneToMany(mappedBy = "tipoPonto")
    private List<PontosAcessibilidade> pontosAcessibilidade = new ArrayList<>();
}