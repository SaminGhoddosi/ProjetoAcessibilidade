package com.Grupo.ProjetoAcessibilidade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private LocalDateTime dataNascimento;

    @Column(length = 11)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "Papel_id")
    private Papel papel;

    @OneToMany(mappedBy = "usuario")
    private List<PontosAcessibilidade> pontosAcessibilidade = new ArrayList<>();
}