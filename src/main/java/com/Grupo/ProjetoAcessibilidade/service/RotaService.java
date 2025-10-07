package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.repository.RotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;

    public RotaService (RotaRepository rotaRepository){
        this.rotaRepository = rotaRepository;
    }

    public List<Rota> listar() {
        return rotaRepository.findAll();
    }

    public Rota buscarPorId(String id) {
        return rotaRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Rota não encontrada com o ID: " + id));
    }

    public Rota salvar(Rota rota) {
        return rotaRepository.save(rota);
    }

    public Rota atualizar(String id, Rota rotaAtualizada) {
        Rota rotaExistente = buscarPorId(id);

        rotaExistente.setCaminho(rotaAtualizada.getCaminho());
        rotaExistente.setDuracao(rotaAtualizada.getDuracao());
        rotaExistente.setDistancia(rotaAtualizada.getDistancia());

        return rotaRepository.save(rotaExistente);
    }

    public void deletar(String id) {
        Rota rotaExistente = buscarPorId(id);
        rotaRepository.delete(rotaExistente);
    }
}


