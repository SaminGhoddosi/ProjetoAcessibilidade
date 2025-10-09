package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.RotasFavoritas;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RotasFavoritasRepository extends MongoRepository<RotasFavoritas, String> {

    List<RotasFavoritas> findByUsuario(Usuario usuario);

    Optional<RotasFavoritas> findByUsuarioAndRotaId(Usuario usuario, String rotaId);
}
