package io.github.nivaldosilva.cadastro_usuarios.controllers.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EnderecoRequest(

        @NotBlank(message = "A rua é obrigatória.") 
        String rua,

        @NotNull(message = "O número é obrigatório.") 
        @Size(min = 1, max = 4, message = "O número deve ter entre 1 e 4 dígitos.")
        String numero,

        @NotBlank(message = "O complemento é obrigatório.")
        @Size(min = 1, max = 100, message = "O complemento deve ter entre 1 e 100 caracteres.")
        String complemento,

        @NotBlank(message = "A cidade é obrigatória.") 
        @Size(min = 1, max = 100, message = "A cidade deve ter entre 1 e 100 caracteres.")
        String cidade,

        @NotBlank(message = "O estado é obrigatório.") @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres.") 
        String estado,

        @NotBlank(message = "O CEP é obrigatório.") 
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido.") 
        String cep

        
) {}
