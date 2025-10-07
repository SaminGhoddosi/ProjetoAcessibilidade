package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "TipoAcessibilidade")
public class TipoAcessibilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipo;

    private String tipo;

    @OneToMany(mappedBy = "tipoAcessibilidade")
    private List<PontosAcessibilidade> pontosAcessibilidade = new ArrayList<>();

    @ManyToMany(mappedBy = "tiposAcessibilidade")
    private Set<Usuario> usuarios = new HashSet<>();
}