package io.github.nivaldosilva.cadastro_usuarios.controllers;

import io.github.nivaldosilva.cadastro_usuarios.controllers.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.mappers.EnderecoMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.TelefoneMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.UsuarioMapper;
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
import java.util.stream.Collectors;

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
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<UsuarioResponse> response = usuarios.stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @Operation(summary = "Criar administrador")
    public ResponseEntity<UsuarioResponse> criarAdmin(@RequestBody @Valid RegistroUsuarioRequest request) {
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario novoUsuario = usuarioService.criarAdmin(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(novoUsuario));
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
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar perfil")
    public ResponseEntity<UsuarioResponse> atualizarPerfil(@RequestBody @Valid RegistroUsuarioRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = UsuarioMapper.toEntity(request);
        Usuario usuarioAtualizado = usuarioService.atualizarPerfil(email, usuario);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuarioAtualizado));
    }

    @PostMapping("/endereco")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Cadastrar endereço")
    public ResponseEntity<EnderecoResponse> cadastrarEndereco(@RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Endereco endereco = EnderecoMapper.toEntity(request);
        Endereco novoEndereco = usuarioService.cadastrarEndereco(email, endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnderecoMapper.toResponse(novoEndereco));
    }

    @PostMapping("/telefone")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Cadastrar telefone")
    public ResponseEntity<TelefoneResponse> cadastrarTelefone(@RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Telefone telefone = TelefoneMapper.toEntity(request);
        Telefone novoTelefone = usuarioService.cadastrarTelefone(email, telefone);
        return ResponseEntity.status(HttpStatus.CREATED).body(TelefoneMapper.toResponse(novoTelefone));
    }

    @PutMapping("/endereco/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar endereço")
    public ResponseEntity<EnderecoResponse> atualizarEndereco(
            @PathVariable UUID id,
            @RequestBody @Valid EnderecoRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Endereco endereco = EnderecoMapper.toEntity(request);
        Endereco enderecoAtualizado = usuarioService.atualizarEndereco(email, id, endereco);
        return ResponseEntity.ok(EnderecoMapper.toResponse(enderecoAtualizado));
    }

    @PutMapping("/telefone/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_USUARIO')")
    @Operation(summary = "Atualizar telefone")
    public ResponseEntity<TelefoneResponse> atualizarTelefone(
            @PathVariable UUID id,
            @RequestBody @Valid TelefoneRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Telefone telefone = TelefoneMapper.toEntity(request);
        Telefone telefoneAtualizado = usuarioService.atualizarTelefone(email, id, telefone);
        return ResponseEntity.ok(TelefoneMapper.toResponse(telefoneAtualizado));
    }
}
