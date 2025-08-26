package br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
        @NotBlank String email, @NotBlank String senha) {

}
