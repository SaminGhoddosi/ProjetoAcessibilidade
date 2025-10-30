package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.dto.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.dto.TipoPontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.UsuarioRepository;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OsmImportService implements Sink {

    @Autowired private PontoService pontoService;
    @Autowired private TipoPontoService tipoPontoService;
    @Autowired private TipoAcessibilidadeService tipoAcessibilidadeService;
    @Autowired private PontosAcessibilidadeService pontosAcessibilidadeService;
    @Autowired private UsuarioRepository usuarioRepository;

    private Usuario importUser;
    private int count = 0;

    // Cache para não bater no DB milhares de vezes
    private final Map<String, TipoPonto> tipoPontoCache = new HashMap<>();
    private final Map<String, TipoAcessibilidade> tipoAcessibilidadeCache = new HashMap<>();

    @Override
    public void initialize(Map<String, Object> metaData) {
        System.out.println("Iniciando processo de importação OSM...");
        // IMPORTANTE: Crie este usuário no seu MongoDB manualmente antes de rodar!
        this.importUser = usuarioRepository.findByEmail("admin@exemplo.com")
                .orElseThrow(() -> new RuntimeException("Usuário de importação 'admin@import.com' não encontrado. Crie-o no banco de dados primeiro."));
        System.out.println("Importação será executada pelo usuário: " + this.importUser.getNome());
    }

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer.getEntity() instanceof Node) {
            Node node = (Node) entityContainer.getEntity();

            Map<String, String> tags = new HashMap<>();
            for (Tag tag : node.getTags()) {
                tags.put(tag.getKey(), tag.getValue());
            }

            // --- LÓGICA DE IMPORTAÇÃO APRIMORADA ---
            // Vamos procurar por VÁRIAS "tags corretas"

            String accessibilityType = null;
            String accessibilityValue = "yes"; // Valor padrão

            if (tags.containsKey("wheelchair")) {
                accessibilityType = "wheelchair";
                accessibilityValue = tags.get("wheelchair");
            } else if (tags.containsKey("ramp") && !"no".equals(tags.get("ramp"))) {
                accessibilityType = "ramp";
                accessibilityValue = tags.get("ramp");
            } else if ("lowered".equals(tags.get("kerb"))) {
                accessibilityType = "kerb";
                accessibilityValue = "lowered";
            } else if (tags.containsKey("tactile_paving") && !"no".equals(tags.get("tactile_paving"))) {
                accessibilityType = "tactile_paving";
                accessibilityValue = tags.get("tactile_paving");
            }

            // Se não encontramos NENHUMA tag de acessibilidade relevante, pulamos este ponto.
            if (accessibilityType == null) {
                return;
            }

            // Se encontramos, pegamos o tipo do ponto (ou um nome genérico)
            String pontoTipoStr = tags.getOrDefault("amenity",
                    tags.getOrDefault("building", "Ponto de Acessibilidade"));

            String nameTagValue = tags.getOrDefault("name", pontoTipoStr.replace('_', ' ')); // Usa o tipo como nome se não houver nome

            // --- FIM DA LÓGICA APRIMORADA ---


            // 3. Criar o Ponto (Localização)
            PontoDTO pontoDTO = new PontoDTO(nameTagValue, node.getLatitude(), node.getLongitude());
            Ponto ponto = pontoService.salvar(pontoDTO);

            // 4. Encontrar ou Criar o TipoPonto (usando cache)
            TipoPonto tipoPonto = tipoPontoCache.computeIfAbsent(pontoTipoStr, key -> {
                return tipoPontoService.findOrCreate(new TipoPontoDTO(key));
            });

            // 5. Encontrar ou Criar o TipoAcessibilidade (usando cache)
            String acessibilidadeStr = accessibilityType + ":" + accessibilityValue;
            TipoAcessibilidade tipoAcessibilidade = tipoAcessibilidadeCache.computeIfAbsent(acessibilidadeStr, key -> {
                return tipoAcessibilidadeService.findOrCreate(new TipoAcessibilidadeDTO(key));
            });

            // 6. Criar o registro PontosAcessibilidade com os IDs (String)
            PontosAcessibilidadeDTO paDTO = new PontosAcessibilidadeDTO(
                    "IMPORTADO", // Status
                    tipoAcessibilidade.getId(), // ID (String) do MongoDB
                    this.importUser.getId(),       // ID (String) do MongoDB
                    tipoPonto.getId(),              // ID (String) do MongoDB
                    ponto.getId()                   // ID (String) do MongoDB
            );

            pontosAcessibilidadeService.salvar(paDTO);
            count++;
        }
    }
    @Override
    public void complete() {
        System.out.println("Importação OSM concluída. " + count + " pontos de acessibilidade importados.");
    }

    @Override
    public void close() {
        // Nada a fazer
    }
}