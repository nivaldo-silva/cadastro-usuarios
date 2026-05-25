package io.github.nivaldosilva.cadastro_usuarios.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description = "Autenticação JWT. Obtenha o token através do endpoint /auth/login e inclua no header: Authorization: Bearer {token}"
)
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		Contact contact = new Contact()
				.email("nivaldosilva.contato@gmail.com")
				.name("Nivaldo Silva")
				.url("https://github.com/Nivaldo-Silva");

		License license = new License()
				.name("Apache 2.0")
				.url("https://www.apache.org/licenses/LICENSE-2.0.html");

		Info info = new Info()
				.title("Cadastro de Usuários API")
				.version("v1.0.0")
				.description("""
                        API REST para gerenciamento completo de cadastro de usuários com autenticação JWT.

                        ## Autenticação
                        Esta API utiliza autenticação JWT com chaves RSA (RS256). Para autenticar:
                        1. Registre-se através do endpoint `POST /auth/registro`
                        2. Faça login através do endpoint `POST /auth/login`
                        3. Use o `accessToken` retornado no header `Authorization: Bearer {token}`
                        4. Quando o token expirar, renove via `POST /auth/refresh-token`

                        ## Funcionalidades
                        - **Autenticação**: Registro, login e renovação de token JWT
                        - **Usuários**: Listagem, criação de admins, atualização de perfil e exclusão
                        - **Endereços**: Cadastro e atualização de endereços vinculados ao usuário
                        - **Telefones**: Cadastro e atualização de telefones vinculados ao usuário

                        ## Perfis de Acesso
                        - **USUARIO**: Acesso ao próprio perfil, endereços e telefones
                        - **ADMIN**: Acesso completo, incluindo listagem e exclusão de qualquer usuário
                        """)
				.contact(contact)
				.license(license);

		Server localServer = new Server()
				.url("http://localhost:8082")
				.description("Servidor de Desenvolvimento Local");

		return new OpenAPI()
				.info(info)
				.servers(List.of(localServer));
	}
}