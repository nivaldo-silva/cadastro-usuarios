package io.github.nivaldosilva.cadastro_usuarios.controllers.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnderecoResponse(


        UUID id,
        String rua,
        String numero,
        String complemento,
        String cidade,
        String estado,
        String cep,
        LocalDateTime dataCriacao
        
) {}