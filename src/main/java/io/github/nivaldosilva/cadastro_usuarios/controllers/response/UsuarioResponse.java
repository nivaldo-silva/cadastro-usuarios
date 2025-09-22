package io.github.nivaldosilva.cadastro_usuarios.controllers.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.nivaldosilva.cadastro_usuarios.enums.Role;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UsuarioResponse(

        UUID id,
        String nome,
        String email,
        Set<Role> roles, 
        Boolean ativo,
        List<EnderecoResponse> enderecos,
        List<TelefoneResponse> telefones,
        LocalDateTime dataCriacao


        
) {}
