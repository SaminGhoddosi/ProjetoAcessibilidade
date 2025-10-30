package com.Grupo.ProjetoAcessibilidade.init;

import com.Grupo.ProjetoAcessibilidade.model.Papel;
import com.Grupo.ProjetoAcessibilidade.model.Usuario;
import com.Grupo.ProjetoAcessibilidade.repository.PapelRepository;
import com.Grupo.ProjetoAcessibilidade.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(">>> [DataInitializer] Verificando/Criando dados iniciais...");

        // --- 1. Garante Papel ADMIN ---
        final String NOME_PAPEL_ADMIN = "ADMIN";
        Optional<Papel> papelAdminOpt = papelRepository.findAll().stream()
                .filter(p -> NOME_PAPEL_ADMIN.equalsIgnoreCase(p.getNome()))
                .findFirst();
        Papel papelAdmin = papelAdminOpt.orElseGet(() -> {
            System.out.println("[DataInitializer] Criando Papel: " + NOME_PAPEL_ADMIN);
            Papel p = new Papel();
            p.setNome(NOME_PAPEL_ADMIN);
            return papelRepository.save(p);
        });

        // --- 2. Garante Usuário Admin (com ID String) ---
        final String EMAIL_ADMIN = "admin@exemplo.com";

        // Agora 'existsById' vai receber uma String (se o seu repo foi atualizado)
        // Mas vamos buscar pelo email, que é mais seguro
        Optional<Usuario> adminExistente = usuarioRepository.findAll().stream()
                .filter(u -> EMAIL_ADMIN.equalsIgnoreCase(u.getEmail()))
                .findFirst();

        if (adminExistente.isEmpty()) {
            System.out.println("[DataInitializer] Criando Usuário Admin com Email: " + EMAIL_ADMIN);
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail(EMAIL_ADMIN);
            admin.setCpf("00000000000");
            admin.setDataNascimento(LocalDate.now().minusYears(30));
            admin.setTelefone("00000000000");
            admin.setSenha("senha");
            admin.setPapel(papelAdmin);

            Usuario adminSalvo = usuarioRepository.save(admin);
            System.out.println("[DataInitializer] Usuário Admin criado! ID gerado: " + adminSalvo.getId()); // Assumindo que o getter manual está lá
        } else {
            System.out.println("[DataInitializer] Usuário '" + EMAIL_ADMIN + "' já existe. ID: " + adminExistente.get().getId()); // Assumindo que o getter manual está lá
        }
        System.out.println(">>> [DataInitializer] Verificação/Criação concluída.");
    }
}