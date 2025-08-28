package br.com.myproject.projeto_para_estudo.infrastructure.dto.privilegios;

import jakarta.validation.constraints.NotBlank;

public record PrivilegiosRequest(
    @NotBlank String nome) {
}
