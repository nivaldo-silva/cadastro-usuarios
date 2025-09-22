package io.github.nivaldosilva.cadastro_usuarios.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Carregando usuÃ¡rio por email: {}", email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Tentativa de login com email nÃ£o encontrado: {}", email);
                    return new UsernameNotFoundException("UsuÃ¡rio nÃ£o encontrado: " + email);
                });

        log.debug("UsuÃ¡rio encontrado: {} com roles: {}", usuario.getEmail(), usuario.getRoles());

        // Retorna o prÃ³prio Usuario que jÃ¡ implementa UserDetails
        // Isso mantÃ©m todas as informaÃ§Ãµes do usuÃ¡rio disponÃ­veis no contexto de seguranÃ§a
        return usuario;
    }
}
