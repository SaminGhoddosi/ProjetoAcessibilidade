package com.Grupo.ProjetoAcessibilidade.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValhallaProfileService {

    /**
     * Retorna um Map contendo as opções de custo ('costing_options') do Valhalla
     * baseadas no perfil de mobilidade fornecido.
     *
     * @param perfilMobilidade String representando o perfil ("PADRAO", "CADEIRANTE", "IDOSO").
     * @return Map<String, Object> com as opções para serem incluídas na chave "pedestrian"
     * dentro de "costing_options" na requisição para o Valhalla. Retorna um mapa vazio
     * se o perfil não for reconhecido (usará os padrões do Valhalla).
     */
    public Map<String, Object> getCostingOptionsForProfile(String perfilMobilidade) {

        // Cria um mapa para guardar as opções customizadas
        Map<String, Object> options = new HashMap<>();

        // Verifica o perfil (ignorando maiúsculas/minúsculas para segurança)
        if (perfilMobilidade == null) {
            System.err.println("ValhallaProfileService: Perfil de mobilidade nulo recebido. Usando opções padrão.");
            return options; // Retorna mapa vazio, Valhalla usará os defaults
        }

        switch (perfilMobilidade.toUpperCase()) {
            case "CADEIRANTE":
                System.out.println("ValhallaProfileService: Gerando opções para CADEIRANTE.");
                options.put("walking_speed", 4.0);       // Velocidade um pouco menor
                options.put("use_hills", 0.05);          // Penalidade MÁXIMA para subidas
                options.put("avoid_stairs", 7200);       // Penalidade ALTÍSSIMA para escadas (2 horas)
                options.put("avoid_curbs", 1.0);         // Penalidade MÁXIMA para meios-fios/guias
                options.put("sidewalk_factor", 0.7);     // Forte preferência por calçadas
                // Adicione outras opções se necessário
                break;

            case "IDOSO":
                System.out.println("ValhallaProfileService: Gerando opções para IDOSO.");
                options.put("walking_speed", 3.5);       // Velocidade de caminhada mais lenta
                options.put("use_hills", 0.3);           // Penaliza subidas, mas permite as leves
                options.put("avoid_stairs", 600);        // Penalidade de 10 minutos para escadas (evita, não proíbe)
                options.put("avoid_curbs", 0.8);         // Forte penalidade para meios-fios, por segurança
                options.put("sidewalk_factor", 0.8);     // Preferência por calçadas
                // Adicione outras opções se necessário
                break;

            case "PADRAO":
            default:
                // Para o perfil padrão, não precisamos enviar nenhuma opção customizada.
                // O Valhalla usará os valores default dele.
                System.out.println("ValhallaProfileService: Perfil PADRAO ou não reconhecido. Usando opções padrão do Valhalla.");
                // Retornamos um mapa vazio, o que fará com que a chave "costing_options"
                // nem seja enviada na requisição, usando assim os padrões do Valhalla.
                break;
        }

        return options; // Retorna o mapa com as opções (pode estar vazio para o padrão)
    }
}