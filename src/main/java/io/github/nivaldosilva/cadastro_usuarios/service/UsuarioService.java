package io.github.nivaldosilva.cadastro_usuarios.service;

import io.github.nivaldosilva.cadastro_usuarios.controllers.request.EnderecoRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.TelefoneRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.request.RegistroUsuarioRequest;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.EnderecoResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.TelefoneResponse;
import io.github.nivaldosilva.cadastro_usuarios.controllers.response.UsuarioResponse;
import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import io.github.nivaldosilva.cadastro_usuarios.exceptions.EmailJaCadastradoException;
import io.github.nivaldosilva.cadastro_usuarios.exceptions.UsuarioNaoEncontradoException;
import io.github.nivaldosilva.cadastro_usuarios.mappers.EnderecoMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.TelefoneMapper;
import io.github.nivaldosilva.cadastro_usuarios.mappers.UsuarioMapper;
import io.github.nivaldosilva.cadastro_usuarios.repository.EnderecoRepository;
import io.github.nivaldosilva.cadastro_usuarios.repository.TelefoneRepository;
import io.github.nivaldosilva.cadastro_usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    @Transactional
    public UsuarioResponse registrarUsuario(RegistroUsuarioRequest request) {
        validarEmailUnico(request.email());

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .roles(Set.of(Role.USUARIO))
                .ativo(true)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário registrado: {}", salvo.getEmail());

        return UsuarioMapper.toResponse(salvo);
    }

    @Transactional
    public UsuarioResponse criarAdmin(RegistroUsuarioRequest request) {
        validarEmailUnico(request.email());

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .roles(Set.of(Role.ADMIN, Role.USUARIO))
                .ativo(true)
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Administrador criado: {}", salvo.getEmail());

        return UsuarioMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizarPerfil(String emailAutenticado, RegistroUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(request.email())) {
            validarEmailUnico(request.email());
            usuario.setEmail(request.email());
        }

        usuario.setNome(request.nome());

        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(atualizado);
    }

    @Transactional
    public EnderecoResponse cadastrarEndereco(String userEmail, EnderecoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Endereco endereco = EnderecoMapper.toEntity(request);
        endereco.setUsuario(usuario);

        Endereco salvo = enderecoRepository.save(endereco);
        return EnderecoMapper.toResponse(salvo);
    }

    @Transactional
    public TelefoneResponse cadastrarTelefone(String userEmail, TelefoneRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Telefone telefone = TelefoneMapper.toEntity(request);
        telefone.setUsuario(usuario);

        Telefone salvo = telefoneRepository.save(telefone);
        return TelefoneMapper.toResponse(salvo);
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        usuarioRepository.deleteById(id);
        log.info("Usuário deletado: {}", usuario.getEmail());
    }

    @Transactional
    public EnderecoResponse atualizarEndereco(String userEmail, UUID enderecoId, EnderecoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Endereço não encontrado"));

        if (!isAdmin(userEmail) && !endereco.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode atualizar seus próprios endereços");
        }

        endereco.setRua(request.rua());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setCidade(request.cidade());
        endereco.setEstado(request.estado());
        endereco.setCep(request.cep());

        Endereco atualizado = enderecoRepository.save(endereco);
        log.info("Endereço {} atualizado por {}", enderecoId, userEmail);

        return EnderecoMapper.toResponse(atualizado);
    }

    @Transactional
    public TelefoneResponse atualizarTelefone(String userEmail, UUID telefoneId, TelefoneRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Telefone não encontrado"));

        if (!isAdmin(userEmail) && !telefone.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode atualizar seus próprios telefones");
        }

        telefone.setNumero(request.numero());
        telefone.setDdd(request.ddd());

        Telefone atualizado = telefoneRepository.save(telefone);
        log.info("Telefone {} atualizado por {}", telefoneId, userEmail);

        return TelefoneMapper.toResponse(atualizado);
    }

    private boolean isAdmin(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
        return usuario.getRoles().contains(Role.ADMIN);
    }

    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }
    }
}