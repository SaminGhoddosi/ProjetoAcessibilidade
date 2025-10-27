package com.Grupo.ProjetoAcessibilidade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "usuarios")
public class Usuario implements UserDetails {

    @Id
    private String id;
    private String nome;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String cpf;

    private LocalDate dataNascimento;
    private String telefone;
    private String senha;

    @DBRef
    private Papel papel;

    @DBRef
    private List<Rota> rotasSalvas = new ArrayList<>();

    @DBRef
    private Set<TipoAcessibilidade> tiposAcessibilidade = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (papel != null && papel.getNome() != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + papel.getNome()));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}