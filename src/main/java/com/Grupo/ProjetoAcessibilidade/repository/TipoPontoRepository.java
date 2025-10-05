package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPontoRepository extends JpaRepository<TipoPonto, Long> {
}
