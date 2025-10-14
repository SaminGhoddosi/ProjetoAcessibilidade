package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.RotaDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaCommunicationException;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaNoRouteFoundException;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.repository.RotaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe esta anotação

import java.util.ArrayList;
import java.util.List;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final ValhallaService valhallaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RotaService(RotaRepository rotaRepository, ValhallaService valhallaService) {
        this.rotaRepository = rotaRepository;
        this.valhallaService = valhallaService;
    }

    public List<Rota> listar() {
        return rotaRepository.findAll();
    }

    public Rota buscarPorId(String id) {
        return rotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Rota não encontrada com o ID: " + id));
    }

    @Transactional
    public Rota salvar(RotaDTO dto) {
        Rota novaRota = new Rota();
        novaRota.setOrigem(dto.origem().toPonto());
        novaRota.setDestino(dto.destino().toPonto());

        preencherCaminhoComValhalla(novaRota);

        return rotaRepository.save(novaRota);
    }

    @Transactional
    public Rota atualizar(String id, RotaDTO dto) {
        Rota rotaExistente = buscarPorId(id);

        rotaExistente.setOrigem(dto.origem().toPonto());
        rotaExistente.setDestino(dto.destino().toPonto());

        preencherCaminhoComValhalla(rotaExistente);

        return rotaRepository.save(rotaExistente);
    }

    @Transactional
    public void deletar(String id) {
        Rota rotaExistente = buscarPorId(id);
        rotaRepository.delete(rotaExistente);
    }

    private void preencherCaminhoComValhalla(Rota rota) {
        try {
            String rotaValhallaJson = valhallaService.getAccessibleRoute(rota.getOrigem(), rota.getDestino()).block();

            JsonNode root = objectMapper.readTree(rotaValhallaJson);
            String encodedPolyline = root.at("/trip/legs/0/shape").asText();

            if (encodedPolyline == null || encodedPolyline.isEmpty()) {
                throw new ValhallaNoRouteFoundException("Valhalla não retornou uma geometria ('shape') para a rota solicitada.");
            }

            List<double[]> coords = decodePolyline(encodedPolyline, 6);
            List<Ponto> caminho = coords.stream()
                    .map(coord -> new Ponto(null, null, coord[0], coord[1]))
                    .toList();

            rota.setCaminho(caminho);

        } catch (JsonProcessingException e) {
            throw new ValhallaCommunicationException("Falha ao processar o JSON da resposta do Valhalla.");
        }
    }

    private List<double[]> decodePolyline(String encoded, int precision) {
        List<double[]> path = new ArrayList<>();
        int index = 0, len = encoded.length();
        double lat = 0, lon = 0;
        double factor = Math.pow(10, precision);

        while (index < len) {
            int result = 0, shift = 0, b;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            double dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            result = 0;
            shift = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            double dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lon += dlng;

            path.add(new double[]{lat / factor, lon / factor});
        }
        return path;
    }
}