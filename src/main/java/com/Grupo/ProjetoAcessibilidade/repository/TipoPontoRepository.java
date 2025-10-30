package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoPontoRepository extends MongoRepository<TipoPonto, String> {
    TipoPonto tipo(String tipo);
    Optional<TipoPonto> findByTipo(String tipo);
}
