package io.github.nivaldosilva.cadastro_usuarios.security;

import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev") 
public class DatabaseSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (usuarioRepository.count() == 0) {
            log.info("Banco de dados vazio. Populando com dados iniciais...");

          
            Usuario admin = new Usuario();
            admin.setNome("Administrador do Sistema");
            admin.setEmail("admin@sistema.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(Role.ADMIN, Role.USUARIO));
            admin.setAtivo(true);
            usuarioRepository.save(admin);
            log.info("Usuário ADMIN criado: admin@sistema.com");

          
            Usuario comum = new Usuario();
            comum.setNome("Nivaldo Silva");
            comum.setEmail("nivaldosilva@sistema.com");
            comum.setSenha(passwordEncoder.encode("usuario123"));
            comum.setRoles(Set.of(Role.USUARIO));
            comum.setAtivo(true);
            usuarioRepository.save(comum);
            log.info("Usuário COMUM criado: nivaldosilva@sistema.com");

            log.info("População inicial de dados concluida.");
        } else {
            log.info("Banco de dados já populado. Ignorando seeding.");
        }
    }
}