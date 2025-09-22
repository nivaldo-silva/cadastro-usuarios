package io.github.nivaldosilva.cadastro_usuarios.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.LoginRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenGenerator;
    private final UsuarioRepository usuarioRepository;
    private final JwtDecoder jwtDecoder;

    public LoginResponse autenticarUsuario(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase().trim();

        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));
            if (!usuario.isEnabled()) {
                log.warn("Tentativa de login de conta desativada: {}", email);
                throw new DisabledException("Conta desativada");
            }
            if (!usuario.isAccountNonLocked()) {
                log.warn("Tentativa de login de conta bloqueada: {}", email);
                throw new LockedException("Conta bloqueada");
            }

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email,loginRequest.getSenha());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("Login realizado com sucesso para usuário: {} com roles: {}",email, usuario.getRoles());

            return tokenGenerator.generateTokens(authentication);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            log.warn("Falha na autenticao para usuario: {} - {}", email, e.getMessage());
            throw new BadCredentialsException("Email ou senha inválidos");
        } catch (DisabledException | LockedException e) {
            log.warn("Conta com restrições tentou fazer login: {} - {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado na autenticao para usuario: {}", email, e);
            throw new BadCredentialsException("Erro interno. Tente novamente mais tarde.");
        }
    }


    public LoginResponse refreshAccessToken(String refreshToken) {

        try {
            var jwt = jwtDecoder.decode(refreshToken);
            if (!"refresh".equals(jwt.getClaimAsString("token_type"))) {
                log.warn("Tentativa de refresh com token inválido (tipo de token incorreto)");
                throw new BadCredentialsException("Token de refresh inválido");
            }
            String email = jwt.getSubject();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario do token não encontrado: " + email));
            if (!usuario.isEnabled())
                throw new DisabledException("Conta desativada");
            if (!usuario.isAccountNonLocked())
                throw new LockedException("Conta bloqueada");
            Authentication newAuth = new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities() 
            );
            log.info("Token de acesso renovado para o usuario: {}", email);
            
            return tokenGenerator.generateAccessToken(newAuth);
            
            } catch (JwtException e) {
            log.warn("Falha na validação do refresh token: {}", e.getMessage());
            throw new BadCredentialsException("Refresh token inválido ou expirado");
        }
    }
}
