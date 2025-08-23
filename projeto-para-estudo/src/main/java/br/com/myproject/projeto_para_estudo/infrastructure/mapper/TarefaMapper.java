package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Tarefa;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaResponse;

@Component
public class TarefaMapper {
  public TarefaResponse toResponseDTO(Tarefa tarefa) {
    return new TarefaResponse(
        tarefa.getId(),
        tarefa.getTitulo(),
        tarefa.getDescricao(),
        tarefa.getDataVencimento(),
        tarefa.isConcluida(),
        tarefa.getUsuario().getId());
  }

  public Tarefa toDomain(TarefaResponse response) {
    return new Tarefa(
        response.id(),
        response.titulo(),
        response.descricao(),
        response.dataVencimento(),
        response.isConcluida(),
        response.usuarioId());
  }

}
