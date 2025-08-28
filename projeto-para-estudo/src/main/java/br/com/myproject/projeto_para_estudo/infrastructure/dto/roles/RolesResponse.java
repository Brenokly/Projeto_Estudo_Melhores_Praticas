package br.com.myproject.projeto_para_estudo.infrastructure.dto.roles;

import java.util.List;
import java.util.UUID;

import br.com.myproject.projeto_para_estudo.infrastructure.dto.privilegios.PrivilegiosResponse;

public record RolesResponse(
    UUID id, String nome, List<PrivilegiosResponse> privilegios) {
}
