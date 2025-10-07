package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.PontosAcessibilidade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PontosAcessibilidadeRepository extends MongoRepository<PontosAcessibilidade, String> {
}
