package io.github.nivaldosilva.cadastro_usuarios.controllers;

import io.github.nivaldosilva.cadastro_usuarios.controllers.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Operation(summary = "Listar usuários")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Operation(summary = "Criar administrador")
    public ResponseEntity<UsuarioResponse> criarAdmin(@RequestBody @Valid RegistroUsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.criarAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Ver perfil")
    public ResponseEntity<UsuarioResponse> verPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar perfil")
    public ResponseEntity<UsuarioResponse> atualizarPerfil(@RequestBody @Valid RegistroUsuarioRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(usuarioService.atualizarPerfil(email, request));
    }

    @PostMapping("/endereco")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Cadastrar endereço")
    public ResponseEntity<EnderecoResponse> cadastrarEndereco(@RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        EnderecoResponse endereco = usuarioService.cadastrarEndereco(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @PostMapping("/telefone")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Cadastrar telefone")
    public ResponseEntity<TelefoneResponse> cadastrarTelefone(@RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TelefoneResponse telefone = usuarioService.cadastrarTelefone(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(telefone);
    }

    @PutMapping("/endereco/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar endereço")
    public ResponseEntity<EnderecoResponse> atualizarEndereco(
            @PathVariable UUID id,
            @RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        EnderecoResponse endereco = usuarioService.atualizarEndereco(email, id, request);
        return ResponseEntity.ok(endereco);
    }

    @PutMapping("/telefone/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar telefone")
    public ResponseEntity<TelefoneResponse> atualizarTelefone(
            @PathVariable UUID id,
            @RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TelefoneResponse telefone = usuarioService.atualizarTelefone(email, id, request);
        return ResponseEntity.ok(telefone);
    }
}