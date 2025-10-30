package com.Grupo.ProjetoAcessibilidade;

import com.Grupo.ProjetoAcessibilidade.model.Papel; // Importe Papel
import com.Grupo.ProjetoAcessibilidade.model.Usuario; // Importe Usuario
import com.Grupo.ProjetoAcessibilidade.repository.PapelRepository; // Importe PapelRepository
import com.Grupo.ProjetoAcessibilidade.repository.UsuarioRepository; // Importe UsuarioRepository
import com.Grupo.ProjetoAcessibilidade.service.OsmImportService;
import crosby.binary.osmosis.OsmosisReader;
import org.springframework.boot.CommandLineRunner; // Importe CommandLineRunner
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Importe Bean
import org.springframework.transaction.annotation.Transactional; // Importe Transactional (opcional, mas bom)

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime; // Importe LocalDateTime
import java.util.Optional; // Importe Optional

@SpringBootApplication
public class ProjetoAcessibilidadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoAcessibilidadeApplication.class, args);
    }

    @Bean
    public CommandLineRunner importOsmData(OsmImportService osmImportService) {
        return args -> {
            try {
                // 1. Defina o caminho para o seu arquivo .pbf
                //    Coloque o arquivo 'blumenau-acessibilidade.pbf' na raiz do seu projeto ou em um local conhecido.
                String pbfFilePath = "blumenau-acessibilidade.pbf"; //

                System.out.println("Iniciando importação do arquivo PBF: " + pbfFilePath);

                FileInputStream inputStream = new FileInputStream(pbfFilePath);

                // 2. Crie um leitor PBF
                OsmosisReader reader = new OsmosisReader(inputStream);

                // 3. Configure o "Sink" (o nosso serviço de importação)
                reader.setSink(osmImportService);

                // 4. Inicie o processo de leitura e processamento
                reader.run(); // Isso vai chamar o método process() do seu OsmImportService para cada item no arquivo

                System.out.println("Importação do PBF concluída.");

            } catch (FileNotFoundException e) {
                System.err.println("Erro: Arquivo PBF não encontrado. " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Erro durante a importação do PBF: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

}
