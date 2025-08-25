package br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TarefaAtualizarRequest(
    @NotNull UUID id,
    @NotBlank @Size(min = 3, max = 100) String titulo,
    @NotBlank @Size(min = 10, max = 500) String descricao,
    @FutureOrPresent LocalDate dataVencimento,
    @NotNull boolean isConcluida,
    @NotNull UUID usuarioId) {
}
