package br.com.myproject.projeto_para_estudo.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API de Gerenciamento de Tarefas", version = "v1", description = "Uma API simples para gerenciar tarefas de usuários, feitas pra fins de estudos."))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Insira o token JWT obtido no endpoint de login.")
public class OpenApiConfig {
}