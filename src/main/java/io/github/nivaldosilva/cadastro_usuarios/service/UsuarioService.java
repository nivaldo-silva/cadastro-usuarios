package io.github.nivaldosilva.cadastro_usuarios.service;

import io.github.nivaldosilva.cadastro_usuarios.entities.Endereco;
import io.github.nivaldosilva.cadastro_usuarios.entities.Telefone;
import io.github.nivaldosilva.cadastro_usuarios.entities.Usuario;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import io.github.nivaldosilva.cadastro_usuarios.exceptions.EmailJaCadastradoException;
import io.github.nivaldosilva.cadastro_usuarios.exceptions.UsuarioNaoEncontradoException;
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
    public Usuario registrarUsuario(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setRoles(Set.of(Role.USUARIO));
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário registrado: {}", salvo.getEmail());

        return salvo;
    }

    @Transactional
    public Usuario criarAdmin(Usuario usuario) {
        validarEmailUnico(usuario.getEmail());

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setRoles(Set.of(Role.ADMIN, Role.USUARIO));
        usuario.setAtivo(true);

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Administrador criado: {}", salvo.getEmail());

        return salvo;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizarPerfil(String emailAutenticado, Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(usuarioAtualizado.getEmail())) {
            validarEmailUnico(usuarioAtualizado.getEmail());
            usuario.setEmail(usuarioAtualizado.getEmail());
        }

        usuario.setNome(usuarioAtualizado.getNome());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Endereco cadastrarEndereco(String userEmail, Endereco endereco) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        endereco.setUsuario(usuario);

        return enderecoRepository.save(endereco);
    }

    @Transactional
    public Telefone cadastrarTelefone(String userEmail, Telefone telefone) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        telefone.setUsuario(usuario);

        return telefoneRepository.save(telefone);
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        usuarioRepository.deleteById(id);
        log.info("Usuário deletado: {}", usuario.getEmail());
    }

    @Transactional
    public Endereco atualizarEndereco(String userEmail, UUID enderecoId, Endereco enderecoAtualizado) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Endereço não encontrado"));

        if (!isAdmin(userEmail) && !endereco.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode atualizar seus próprios endereços");
        }

        endereco.setRua(enderecoAtualizado.getRua());
        endereco.setNumero(enderecoAtualizado.getNumero());
        endereco.setComplemento(enderecoAtualizado.getComplemento());
        endereco.setCidade(enderecoAtualizado.getCidade());
        endereco.setEstado(enderecoAtualizado.getEstado());
        endereco.setCep(enderecoAtualizado.getCep());

        Endereco atualizado = enderecoRepository.save(endereco);
        log.info("Endereço {} atualizado por {}", enderecoId, userEmail);

        return atualizado;
    }

    @Transactional
    public Telefone atualizarTelefone(String userEmail, UUID telefoneId, Telefone telefoneAtualizado) {
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Telefone telefone = telefoneRepository.findById(telefoneId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Telefone não encontrado"));

        if (!isAdmin(userEmail) && !telefone.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você só pode atualizar seus próprios telefones");
        }

        telefone.setNumero(telefoneAtualizado.getNumero());
        telefone.setDdd(telefoneAtualizado.getDdd());

        Telefone atualizado = telefoneRepository.save(telefone);
        log.info("Telefone {} atualizado por {}", telefoneId, userEmail);

        return atualizado;
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
