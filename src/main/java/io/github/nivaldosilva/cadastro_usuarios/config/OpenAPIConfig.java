package io.github.nivaldosilva.cadastro_usuarios.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description = "🔑 Insira o token obtido via POST /auth/login no formato: Bearer {token}"
)
public class OpenAPIConfig {

	@Value("${server.port:8082}")
	private String serverPort;

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(buildInfo())
				.servers(buildServers())
				.tags(buildTags());
	}

	private Info buildInfo() {
		return new Info()
				.title("🔐 Cadastro de Usuários API")
				.version("v1.0.0")
				.description("""
                        API RESTful para gerenciamento de usuários com autenticação stateless via **JWT (RS256)**.

                        ## ⚙️ Funcionalidades
                        - **Autenticação**: Registro, login e renovação de token JWT
                        - **Usuários**: CRUD completo com controle de perfil e roles
                        - **Endereços**: Cadastro e atualização vinculados ao usuário
                        - **Telefones**: Cadastro e atualização vinculados ao usuário

                        ## 🛡️ Segurança
                        - Autenticação stateless com **JWT assinado via RSA (RS256)**
                        - Controle de acesso baseado em roles com **Spring Security**
                        - Senhas criptografadas com **BCrypt (strength 12)**
                        - Refresh token para renovação segura de sessão

                        ## 🔑 Como Autenticar
                        1. `POST /auth/registro` — crie sua conta
                        2. `POST /auth/login` — obtenha o `accessToken`
                        3. Clique em **Authorize 🔓** e insira `Bearer {token}`
                        4. `POST /auth/refresh-token` — renove quando expirar
                        
                        """)
				.contact(new Contact()
						.name("Nivaldo Silva")
						.url("https://github.com/Nivaldo-Silva")
						.email("nivaldosilva.contato@gmail.com"));

	}

	private List<Server> buildServers() {
		return List.of(
				new Server()
						.url("http://localhost:" + serverPort)
						.description("Ambiente de desenvolvimento local"),
				new Server()
						.url("https://api.nivaldosilva.io")
						.description("Ambiente de produção")
		);
	}

	private List<Tag> buildTags() {
		return List.of(
				new Tag().name("Autenticação").description("🔐 Registro, login e renovação de token JWT"),
				new Tag().name("Usuários").description("👤 Gerenciamento de usuários e perfis"),
				new Tag().name("Endereços").description("📍 Cadastro e atualização de endereços"),
				new Tag().name("Telefones").description("📞 Cadastro e atualização de telefones")
		);
	}
}