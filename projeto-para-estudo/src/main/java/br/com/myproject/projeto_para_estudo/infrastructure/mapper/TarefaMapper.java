package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Tarefa;
import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaAtualizarRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaRequest;
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

  public Tarefa toDomain(TarefaRequest request) {
    return new Tarefa(
        request.titulo(),
        request.descricao(),
        request.dataVencimento(),
        request.isConcluida(),
        new Usuario(request.usuarioId()));
  }

  public Tarefa toDomain(TarefaAtualizarRequest request) {
    return new Tarefa(
        request.id(),
        request.titulo(),
        request.descricao(),
        request.dataVencimento(),
        request.isConcluida(),
        new Usuario(request.usuarioId()));
  }
}
