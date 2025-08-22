package br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizadoRequest(
    @NotBlank @Size(min = 3, max = 20, message = "O nome deve ter no mínimo 3 caracteres e no máximo 20.") String nome) {
}
