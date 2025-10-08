package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.dto.LoginRequestDTO;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    boolean existsByEmail(@NotBlank @Email String email);

    boolean existsByCpf(@NotBlank @CPF String cpf);

    Optional<Usuario> findByEmail(String email);
}
