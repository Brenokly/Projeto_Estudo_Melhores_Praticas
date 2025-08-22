package br.com.myproject.projeto_para_estudo.core.port.in;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaResponse;

public interface TarefaPortIn {

  /**
   * Cadastra uma nova tarefa para o usuário logado.
   *
   * @param id           ID do usuário logado.
   * @param requestDTO   Dados da tarefa a ser cadastrada.
   * @param usuarioInfos Informações do usuário logado.
   * @return A resposta contendo os detalhes da tarefa cadastrada.
   */
  TarefaResponse cadastrarTarefaDeUsuarioLogado(UUID id, TarefaRequest requestDTO, UserDetails usuarioInfos);

  /**
   * Deleta uma tarefa do usuário logado.
   *
   * @param id           ID da tarefa a ser deletada.
   * @param usuarioInfos Informações do usuário logado.
   */
  void deletarTarefaDeUsuarioLogado(UUID id, UserDetails usuarioInfos);

  /**
   * Atualiza uma tarefa do usuário logado.
   *
   * @param id           ID da tarefa a ser atualizada.
   * @param requestDTO   Dados da tarefa a ser atualizada.
   * @param usuarioInfos Informações do usuário logado.
   * @return A resposta contendo os detalhes da tarefa atualizada.
   */
  TarefaResponse atualizarTarefaDeUsuarioLogado(UUID id, TarefaRequest requestDTO, UserDetails usuarioInfos);

  /**
   * Busca uma tarefa pelo ID do usuário logado.
   *
   * @param id           ID da tarefa a ser buscada.
   * @param usuarioInfos Informações do usuário logado.
   * @return A resposta contendo os detalhes da tarefa encontrada.
   */
  TarefaResponse buscarTarefaPorIdDeUsuarioLogado(UUID id, UserDetails usuarioInfos);

  /**
   * Busca todas as tarefas do usuário logado.
   *
   * @param usuarioInfos Informações do usuário logado.
   * @return A lista de respostas contendo os detalhes das tarefas encontradas.
   */
  List<TarefaResponse> buscarTodasTarefasDeUsuarioLogado(UserDetails usuarioInfos);

}
