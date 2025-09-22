package io.github.nivaldosilva.cadastro_usuarios.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record TelefoneRequest(

        @NotBlank(message = "O número é obrigatório.") 
        @Size(min = 8, max = 9, message = "O número deve ter entre 8 e 9 dígitos.") 
        String numero,

        @NotBlank(message = "O DDD é obrigatório.") 
        @Size(min = 2, max = 2, message = "O DDD deve ter 2 dígitos.") 
        String ddd

) {}