package io.github.nivaldosilva.cadastro_usuarios.controllers.response;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TelefoneResponse(

        UUID id,
        String numero,
        String ddd,
        LocalDateTime dataCriacao
        
) {}
