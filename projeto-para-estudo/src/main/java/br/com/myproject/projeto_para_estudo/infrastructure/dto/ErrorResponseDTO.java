package br.com.myproject.projeto_para_estudo.infrastructure.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
    String message,
    int statusCode,
    LocalDateTime timestamp) {

}
