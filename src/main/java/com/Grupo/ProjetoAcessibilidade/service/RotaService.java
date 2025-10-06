package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.repository.RotaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;

    @Autowired
    public RotaService (RotaRepository rotaRepository){
        this.rotaRepository = rotaRepository;
    }

    public Rota criarRota(Rota rota) {
        return rotaRepository.save(rota);
    }

    public List<Rota> listarTodasAsRotas() {
        return rotaRepository.findAll();
    }

    public Rota buscarRotaPorId(Long id) {
        return rotaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rota não encontradaa" + id));
    }

    public Rota atualizarRota(Long id, Rota rotaAtualizada) {
        Rota rotaExistente = buscarRotaPorId(id);

        rotaExistente.setCaminho(rotaAtualizada.getCaminho());
        rotaExistente.setDuracao(rotaAtualizada.getDuracao());
        rotaExistente.setDistancia(rotaAtualizada.getDistancia());

        return rotaRepository.save(rotaExistente);
    }

    public void deletarRota(Long id) {
        rotaRepository.deleteById(id);
    }
}


