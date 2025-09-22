package io.github.nivaldosilva.cadastro_usuarios.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenAPIConfig {

    @Bean
    OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("nivaldosilva.contato@gmail.com");
        contact.setName("Nivaldo Silva");
        contact.setUrl("https://www.linkedin.com/in/nivaldo-silva-5a8335289/");

        Info info = new Info()
                .title("Cadastro de Usuários API")
                .version("v1")
                .contact(contact)
                .description(
                        "API para gerenciamento de cadastro de usuários, incluindo autenticação JWT, CRUD de usuários, endereços e telefones.");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Server URL local environment");

        return new OpenAPI().info(info).servers(List.of(localServer));
    }
}