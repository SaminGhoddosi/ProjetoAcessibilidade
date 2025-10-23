package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.CalcularRotaDTO;
import com.Grupo.ProjetoAcessibilidade.dto.RotaDTO;
import com.Grupo.ProjetoAcessibilidade.exceptions.ResourceNotFound;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaCommunicationException;
import com.Grupo.ProjetoAcessibilidade.exceptions.ValhallaNoRouteFoundException;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.Rota;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.RotaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe esta anotação
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final ValhallaService valhallaService;
    private final ValhallaProfileService valhallaProfileService;
    private final UsuarioService usuarioService;
    private final PontoService pontoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RotaService(RotaRepository rotaRepository, ValhallaService valhallaService, ValhallaProfileService valhallaProfileService, UsuarioService usuarioService,  PontoService pontoService) {
        this.rotaRepository = rotaRepository;
        this.valhallaService = valhallaService;
        this.valhallaProfileService = valhallaProfileService;
        this.usuarioService = usuarioService;
        this.pontoService = pontoService;
    }

    public List<Rota> listar() {
        return rotaRepository.findAll();
    }

    public Rota buscarPorId(String id) {
        System.out.println("RotaService: Buscando Rota por ID String: " + id + " [CRUD]");
        return rotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Rota não encontrada com o ID: " + id));    }

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

    public Mono<String> calcularRotaAcessivel(Long usuarioIdLong, String perfil, CalcularRotaDTO.CoordenadasDTO coordenadas) throws RuntimeException {

        System.out.println("RotaService: Iniciando cálculo para Usuario ID (Long): " + usuarioIdLong + ", Perfil: " + perfil);

        // 1. Buscar usuário: CONVERTE Long para String E USA orElseThrow
        String usuarioIdString = String.valueOf(usuarioIdLong); // Converte Long para String
        System.out.println("RotaService: Buscando usuário com ID (String): " + usuarioIdString);

        Usuario usuario = usuarioService.buscarPorId(usuarioIdString);

        // Se chegou aqui, o usuário foi encontrado com sucesso.
        System.out.println("RotaService: Usuário encontrado: " + usuario.getNome());


        // 2. Buscar as opções de custo
        System.out.println("RotaService: Buscando opções de custo para o perfil: " + perfil);
        Map<String, Object> costingOptions = valhallaProfileService.getCostingOptionsForProfile(perfil);
        if (costingOptions == null) {
            System.err.println("RotaService: ValhallaProfileService retornou costingOptions nulas!");
            // Retorna Mono com erro para fluxo reativo
            return Mono.error(new RuntimeException("Não foi possível obter as opções de custo para o perfil: " + perfil));
        }

        // 3. Buscar waypoints (Placeholder)
        System.out.println("RotaService: Buscando waypoints (simulado)");
        List<Ponto> waypoints = List.of();

        // 4. Criar Pontos início/fim (Corrigido para String, conforme Ponto.java)
        System.out.println("RotaService: Criando Ponto inicio/fim (Ponto espera String)");
        Ponto pontoInicio = new Ponto();
        pontoInicio.setLatitude(coordenadas.latInicio());
        pontoInicio.setLongitude(coordenadas.lonInicio());

        Ponto pontoFim = new Ponto();
        pontoFim.setLatitude(coordenadas.latFim());
        pontoFim.setLongitude(coordenadas.lonFim());

        // 5. Chamar ValhallaService REAL
        System.out.println("RotaService: Chamando ValhallaService.getAccessibleRouteWithOptions...");
        Mono<String> monoRespostaValhalla = valhallaService.getAccessibleRouteWithOptions(
                pontoInicio,
                pontoFim,
                costingOptions,
                waypoints
        );

        // 6. Retornar o resultado
        System.out.println("RotaService: Retornando o Mono da resposta do Valhalla para o Controller");
        return monoRespostaValhalla;
    }


}