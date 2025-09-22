package io.github.nivaldosilva.cadastro_usuarios.security;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration.seconds:3600}")
    private Long jwtExpirationSeconds;

    @Value("${jwt.refresh.expiration.days:7}")
    private Long jwtRefreshExpirationDays;

    @Value("${jwt.issuer:api://cadastro-usuarios}")
    private String jwtIssuer;

    public LoginResponse generateTokens(Authentication authentication) {
        log.debug("Gerando tokens para usuÃ¡rio: {}", authentication.getName());

        String accessToken = generateAccessTokenString(authentication);
        String refreshToken = generateRefreshTokenString(authentication);

        long refreshExpiresInSeconds = jwtRefreshExpirationDays * 24 * 3600;

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpirationSeconds)
                .refreshExpiresIn(refreshExpiresInSeconds)
                .build();
    }

    public LoginResponse generateAccessToken(Authentication authentication) {
        log.debug("Gerando novo access token para usuÃ¡rio: {}", authentication.getName());

        String accessToken = generateAccessTokenString(authentication);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(null) 
                .expiresIn(jwtExpirationSeconds)
                .build();
    }

    private String generateAccessTokenString(Authentication authentication) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(jwtExpirationSeconds);

        String scope = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.joining(" "));

        String userId = null;
        String nome = null;
        if (authentication.getPrincipal() instanceof Usuario usuario) {
            userId = usuario.getId().toString();
            nome = usuario.getNome();
        }

        var claimsBuilder = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("scope", scope)
                .id(UUID.randomUUID().toString());

        if (userId != null) {
            claimsBuilder.claim("user_id", userId);
        }
        if (nome != null) {
            claimsBuilder.claim("nome", nome);
        }
        JwtClaimsSet claims = claimsBuilder.build();
        log.debug("Gerando access token para: {} com scopes: {}", authentication.getName(), scope);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String generateRefreshTokenString(Authentication authentication) {
        var now = Instant.now();
        var expiresAt = now.plusSeconds(jwtRefreshExpirationDays * 24 * 3600);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(authentication.getName())
                .claim("token_type", "refresh")
                .id(UUID.randomUUID().toString())
                .build();

        log.debug("Gerando refresh token para: {}", authentication.getName());

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}