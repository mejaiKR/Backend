package mejai.mejaigg.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.addServersItem(new Server().url("http://localhost:8080"))
			.addServersItem(new Server().url("https://mejai.xyz"))
			.components(new Components().addSecuritySchemes("Bearer Token", securityScheme()))
			.addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
			.info(new Info()
				.title("Mejai API")
				.version("0.0.1")
				.description("환영합니다! Mejai.gg는 League of Legends의 게임 데이터를 분석하여 사용자에게 통계를 제공하는 서비스입니다.")
				.contact(new Contact()
					.email("wnddms12345@gmail.com")
					.name("Mejai Support")));
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.in(SecurityScheme.In.HEADER)
			.name("Authorization")
			.scheme("bearer")
			.bearerFormat("JWT");
	}
}
