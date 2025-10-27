package com.Grupo.ProjetoAcessibilidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ProjetoAcessibilidadeApplication {

	public static void main(String[] args) {
        SpringApplication.run(ProjetoAcessibilidadeApplication.class, args);

       RestTemplate restTemplate = new RestTemplate();
        String valhallaUrl = "http://localhost:8002/route";

        //para a criação de uma mapa representando o json
        Map<String, Object> requestBody = new HashMap<>();

        List<Map<String, Double>> locations = List.of(
                Map.of("lat", -26.9194, "lon", -49.0661),
                Map.of("lat", -26.9189, "lon", -49.0652)
        );

        requestBody.put("locations", locations);
        requestBody.put("costing", "pedestrian");

        try {
            // 1. ROTA PADRÃO
            String respostaJsonPadrao = restTemplate.postForObject(valhallaUrl, requestBody, String.class);
            System.out.println("--- COPIE O CÓDIGO ABAIXO PARA O ARQUIVO: mock-rota-padrao.json ---");
            System.out.println(respostaJsonPadrao);

            // 2. ROTA CADEIRANTE
            Map<String, Object> pedestrianOptionsCadeirante = Map.of(
                    "use_hills", 0.05,
                    "avoid_stairs", 7200,
                    "avoid_curbs", 1.0
            );
            requestBody.put("costing_options", Map.of("pedestrian", pedestrianOptionsCadeirante));
            String respostaCadeirante = restTemplate.postForObject(valhallaUrl, requestBody, String.class);
            System.out.println("\n--- COPIE O CÓDIGO ABAIXO PARA O ARQUIVO: mock-rota-cadeirante.json ---");
            System.out.println(respostaCadeirante);

            // 3. ROTA IDOSO
            Map<String, Object> pedestrianOptionsIdoso = Map.of(
                    "walking_speed", 3.5,
                    "use_hills", 0.3,
                    "avoid_stairs", 600
            );
            requestBody.put("costing_options", Map.of("pedestrian", pedestrianOptionsIdoso));
            String respostaIdoso = restTemplate.postForObject(valhallaUrl, requestBody, String.class);
            System.out.println("\n--- COPIE O CÓDIGO ABAIXO PARA O ARQUIVO: mock-rota-idoso.json ---");
            System.out.println(respostaIdoso);

        } catch (Exception e) {
            System.out.println("\n--- OCORREU UM ERRO ---");
            System.out.println("Não foi possível conectar ao Valhalla. Verifique se o Docker está rodando.");
            e.printStackTrace();
        }
    }
}

