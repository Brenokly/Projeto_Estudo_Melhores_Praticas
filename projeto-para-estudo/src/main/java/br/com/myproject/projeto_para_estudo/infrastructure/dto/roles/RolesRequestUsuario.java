package br.com.myproject.projeto_para_estudo.infrastructure.dto.roles;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record RolesRequestUsuario(
    @NotNull UUID id) {

}
