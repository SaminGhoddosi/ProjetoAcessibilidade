package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idfeedback;

    @Column(nullable = false)
    private String comentario;

    @Column(nullable = false)
    private String avaliacao;

    @ManyToOne
    @JoinColumn(name = "pontosAcessibilidade_id")
    private PontosAcessibilidade pontosAcessibilidade;

    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;
}