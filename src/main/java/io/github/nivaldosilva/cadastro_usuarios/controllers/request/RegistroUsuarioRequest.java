package io.github.nivaldosilva.cadastro_usuarios.controllers.request;

import java.util.List;
import java.util.Set;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegistroUsuarioRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String senha,

        Set<Role> roles, 

        @Valid
        List<EnderecoRequest> enderecos,

        @Valid
        List<TelefoneRequest> telefones

) {}
