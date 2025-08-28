package br.com.myproject.projeto_para_estudo.infrastructure.dto.roles;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RolesRequest(
    @NotNull UUID id, @NotBlank String nome, List<UUID> privilegios) {
}
