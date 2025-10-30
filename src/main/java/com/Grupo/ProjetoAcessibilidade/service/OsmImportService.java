package com.Grupo.ProjetoAcessibilidade.service;

import com.Grupo.ProjetoAcessibilidade.dto.PontoDTO;
import com.Grupo.ProjetoAcessibilidade.dto.PontosAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.dto.TipoAcessibilidadeDTO;
import com.Grupo.ProjetoAcessibilidade.dto.TipoPontoDTO;
import com.Grupo.ProjetoAcessibilidade.model.Ponto;
import com.Grupo.ProjetoAcessibilidade.model.TipoAcessibilidade;
import com.Grupo.ProjetoAcessibilidade.model.TipoPonto;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;

import com.Grupo.ProjetoAcessibilidade.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class OsmImportService implements Sink {

    @Autowired
    private PontoService pontoService;

    @Autowired
    private TipoPontoService tipoPontoService;

    @Autowired
    private TipoAcessibilidadeService tipoAcessibilidadeService;

    @Autowired
    private PontosAcessibilidadeService pontosAcessibilidadeService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Usado para buscar um usuário "sistema"

    // Este método é onde a mágica acontece.
    // Ele será chamado para cada "entidade" (ponto, caminho, relação) no arquivo PBF.
    @Override
    public void process(EntityContainer entityContainer) {
        // Estamos interessados apenas em "Nodes" (pontos) por enquanto
        if (entityContainer.getEntity() instanceof Node) {
            Node node = (Node) entityContainer.getEntity();

            // 1. Verificar se este ponto tem as "tags corretas" de acessibilidade
            //    Vamos usar "wheelchair" (cadeira de rodas) como exemplo.
            String wheelchairTagValue = null;
            String amenityTagValue = null;
            String nameTagValue = "Ponto sem nome"; // Valor padrão

            for (Tag tag : node.getTags()) {
                if (tag.getKey().equals("wheelchair")) {
                    wheelchairTagValue = tag.getValue();
                }
                if (tag.getKey().equals("amenity")) { // 'amenity' é um tipo comum de ponto (banco, banheiro, etc.)
                    amenityTagValue = tag.getValue();
                }
                if (tag.getKey().equals("name")) {
                    nameTagValue = tag.getValue();
                }
            }

            // 2. Se tiver uma tag de acessibilidade, processamos
            if (wheelchairTagValue != null && amenityTagValue != null) {

                // 3. Criar ou encontrar o Ponto (Localização)
                PontoDTO pontoDTO = new PontoDTO(
                        nameTagValue,
                        node.getLatitude(),
                        node.getLongitude()
                );
                Ponto ponto = pontoService.salvar(pontoDTO); //

                // 4. Criar ou encontrar o TipoPonto
                //    (Idealmente, você teria um método findOrCreate para evitar duplicatas)
                TipoPontoDTO tipoPontoDTO = new TipoPontoDTO(amenityTagValue);
                TipoPonto tipoPonto = tipoPontoService.salvar(tipoPontoDTO); //

                // 5. Criar ou encontrar o TipoAcessibilidade
                //    (Idealmente, você teria um método findOrCreate)
                TipoAcessibilidadeDTO tipoAcessibilidadeDTO = new TipoAcessibilidadeDTO("wheelchair:" + wheelchairTagValue);
                TipoAcessibilidade tipoAcessibilidade = tipoAcessibilidadeService.salvar(tipoAcessibilidadeDTO); //

                // 6. Pegar um usuário padrão (ex: ID 1) para atribuir a criação
                //    Em um cenário real, você pode ter um usuário "admin" ou "importação"
                Optional<Usuario> usuarioOpt = usuarioRepository.findById("6902889dcd6a0a23937c55d2"); //
                if (usuarioOpt.isEmpty()) {
                    System.err.println("Usuário com ID 1 não encontrado. Crie um usuário primeiro.");
                    return;
                }
                Usuario usuario = usuarioOpt.get();

                // 7. Criar o registro PontosAcessibilidade
                PontosAcessibilidadeDTO paDTO = new PontosAcessibilidadeDTO(
                        "IMPORTADO", // Status
                        String.valueOf(tipoAcessibilidade.getTipo()), // Passa o ID como String
                        String.valueOf(usuario.getId()),         // Passa o ID como String
                        String.valueOf(tipoPonto.getId()),             // Passa o ID como String
                        String.valueOf(ponto.getId())                  // Passa o ID como String
                );
                pontosAcessibilidadeService.salvar(paDTO); //

                System.out.println("Importado ponto: " + nameTagValue + " com acessibilidade: " + wheelchairTagValue);
            }
        }
    }

    // Métodos obrigatórios da interface Sink que não usaremos
    @Override public void initialize(Map<String, Object> metaData) {}
    @Override public void complete() {}
    @Override public void close() {}
}