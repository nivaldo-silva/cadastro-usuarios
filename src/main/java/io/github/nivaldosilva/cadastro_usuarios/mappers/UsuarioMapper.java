package io.github.nivaldosilva.cadastro_usuarios.mappers;

import java.util.HashSet;
import java.util.Set;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioMapper {

    public static Usuario toEntity(RegistroUsuarioRequest request) {

        Set<Role> rolesFinais = request.roles();
        if (rolesFinais == null || rolesFinais.isEmpty()) {
            rolesFinais = Set.of(Role.USUARIO);
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(request.senha())
                .roles(new HashSet<>(rolesFinais))
                .ativo(true)
                .contaBloqueada(false)
                .credenciaisExpiradas(false)
                .build();

        if (request.enderecos() != null) {
            usuario.setEnderecos(request.enderecos().stream().map(enderecoRequest -> {
                Endereco endereco = EnderecoMapper.toEntity(enderecoRequest);
                endereco.setUsuario(usuario);
                return endereco;
            }).toList());
        }

        if (request.telefones() != null) {
            usuario.setTelefones(request.telefones().stream().map(telefoneRequest -> {
                Telefone telefone = TelefoneMapper.toEntity(telefoneRequest);
                telefone.setUsuario(usuario);
                return telefone;
            }).toList());
        }

        return usuario;
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .roles(usuario.getRoles())
                .ativo(usuario.getAtivo())
                .enderecos(
                        usuario.getEnderecos() != null ? EnderecoMapper.toResponseList(usuario.getEnderecos()) : null)
                .telefones(
                        usuario.getTelefones() != null ? TelefoneMapper.toResponseList(usuario.getTelefones()) : null)
                .build();
    }

    public static Usuario updateFromRequest(Usuario usuario, RegistroUsuarioRequest request) {
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());

        if (request.roles() != null && !request.roles().isEmpty()) {
            usuario.setRoles(new HashSet<>(request.roles()));
        }

        return usuario;
    }
}