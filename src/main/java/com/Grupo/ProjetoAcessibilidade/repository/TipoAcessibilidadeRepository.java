package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoAcessibilidadeRepository extends MongoRepository<TipoAcessibilidade, String> {
    Optional<TipoAcessibilidade> findByTipo(String tipo); // Adicione este método
}
