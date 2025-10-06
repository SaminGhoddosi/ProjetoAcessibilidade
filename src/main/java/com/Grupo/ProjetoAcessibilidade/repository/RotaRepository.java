package com.Grupo.ProjetoAcessibilidade.repository;

import com.Grupo.ProjetoAcessibilidade.model.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {
}
