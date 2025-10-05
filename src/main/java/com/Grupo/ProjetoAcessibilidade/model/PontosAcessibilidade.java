package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PontosAcessibilidade")
public class PontosAcessibilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "TipoAcessibilidade_idTipo")
    private TipoAcessibilidade tipoAcessibilidade;

    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "TipoPonto_id")
    private TipoPonto tipoPonto;

    @ManyToOne
    @JoinColumn(name = "ponto_id")
    private Ponto ponto;
}