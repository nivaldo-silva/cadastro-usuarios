package io.github.nivaldosilva.cadastro_usuarios.controllers;

import io.github.nivaldosilva.cadastro_usuarios.controllers.request.LoginRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RefreshTokenRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.LoginResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.security.JwtAuthenticationService;
import io.github.nivaldosilva.cadastro_usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class JwtAuthController {

    private final JwtAuthenticationService autenticacaoService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Fazer login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse response = autenticacaoService.autenticarUsuario(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar usuário comum")
    public ResponseEntity<UsuarioResponse> registro(@RequestBody @Valid RegistroUsuarioRequest request) {
        UsuarioResponse response = usuarioService.registrarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Renovar token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        LoginResponse response = autenticacaoService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.ok(response);
    }
}
