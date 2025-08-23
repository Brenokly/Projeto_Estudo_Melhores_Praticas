package br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa;

import java.time.LocalDate;
import java.util.UUID;

public record TarefaResponse(UUID id, String titulo, String descricao, LocalDate dataVencimento,
        boolean isConcluida, UUID usuarioId) {

}
