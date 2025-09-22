package io.github.nivaldosilva.cadastro_usuarios.controllers.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "accessToken", "expires_in", "refreshToken", "refresh_expires_in", "tokenType" })
public class LoginResponse {

  private String accessToken;

  @JsonProperty("expires_in")
  private Long expiresIn;

  private String refreshToken;

  @JsonProperty("refresh_expires_in")
  private Long refreshExpiresIn;

  @Builder.Default
  private String tokenType = "Bearer";

}