package br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(min = 3, max = 20, message = "O nome deve ter no mínimo 3 caracteres e no máximo 20.") String nome,
        @NotBlank @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.") @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "A senha deve conter pelo menos 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial.")
        String senha) {

}