package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPontoRepository extends MongoRepository<TipoPonto, String> {
    TipoPonto tipo(String tipo);
}
