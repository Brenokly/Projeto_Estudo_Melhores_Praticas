package br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario;

import java.util.List;
import java.util.UUID;

import br.com.myproject.projeto_para_estudo.infrastructure.dto.roles.OnlyRolesResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaResponse;

public record UsuarioResponse(UUID id, String nome, String email, List<TarefaResponse> tarefas,
    List<OnlyRolesResponse> roles) {

}
