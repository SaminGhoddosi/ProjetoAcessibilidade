package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Usuario_has_TipoAcessibilidade")
public class UsuarioTipoAcessibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "TipoAcessibilidade_idTipo")
    private TipoAcessibilidade tipoAcessibilidade;
}

