package io.github.nivaldosilva.cadastro_usuarios.controllers.response.error;

import java.time.LocalDateTime;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
        
) {}
