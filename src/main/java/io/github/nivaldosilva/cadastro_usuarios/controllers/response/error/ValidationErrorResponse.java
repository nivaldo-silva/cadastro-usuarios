package io.github.nivaldosilva.cadastro_usuarios.controllers.response.error;

import java.time.LocalDateTime;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(

        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        Map<String, String> fieldErrors
        
) {}
