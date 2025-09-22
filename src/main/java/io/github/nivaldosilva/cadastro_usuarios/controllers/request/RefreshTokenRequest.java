package io.github.nivaldosilva.cadastro_usuarios.controllers.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

    @NotBlank(message = "O refresh token não pode ser vazio")
    String refreshToken
    
) {}