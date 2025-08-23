package br.com.myproject.projeto_para_estudo.core.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.myproject.projeto_para_estudo.core.entity.Tarefa;

public interface TarefaPortOut {

  /**
   * Salva uma tarefa no banco de dados.
   *
   * @param tarefa a tarefa a ser salva.
   * @return a tarefa salva.
   * @pre A tarefa deve ser válida.
   * @post A tarefa é persistida no banco de dados.
   * @throws RegraNegocioException se a tarefa não for válida.
   */
  Tarefa save(Tarefa tarefa);

  /**
   * Busca uma tarefa pelo ID.
   *
   * @param id o ID da tarefa a ser buscada.
   * @return um Optional contendo a tarefa encontrada, ou vazio se não encontrado.
   * @pre O ID deve ser válido.
   * @post A tarefa é retornada se encontrada.
   * @throws RegraNegocioException se o ID não for válido.
   */
  Optional<Tarefa> findById(UUID id);

  /**
   * Deleta uma tarefa pelo ID.
   *
   * @param id o ID da tarefa a ser deletada.
   * @pre O ID deve ser válido.
   * @post A tarefa é removida do banco de dados.
   * @throws RegraNegocioException se o ID não for válido.
   */
  void deleteById(UUID id);

  /**
   * Deleta uma tarefa.
   *
   * @param tarefa a tarefa a ser deletada.
   * @pre A tarefa deve ser válida.
   * @post A tarefa é removida do banco de dados.
   * @throws RegraNegocioException se a tarefa não for válida.
   */
  void delete(Tarefa tarefa);

  /**
   * Atualiza uma tarefa.
   *
   * @param tarefa a tarefa a ser atualizada.
   * @pre A tarefa deve ser válida.
   * @post A tarefa é atualizada no banco de dados.
   * @throws RegraNegocioException se a tarefa não for válida.
   */
  Optional<Tarefa> update(Tarefa tarefa);

  /**
   * Verifica se uma tarefa existe pelo ID.
   *
   * @param id o ID da tarefa a ser verificada.
   * @return true se a tarefa existir, false caso contrário.
   * @pre O ID deve ser válido.
   * @post Retorna true se a tarefa existir, false caso contrário.
   */
  boolean existsById(UUID id);

  /**
   * Busca todas as tarefas de um usuário.
   *
   * @param usuarioId o ID do usuário cujas tarefas devem ser buscadas.
   * @return uma lista contendo todas as tarefas do usuário.
   * @pre O ID do usuário deve ser válido.
   * @post Retorna uma lista com todas as tarefas do usuário.
   */
  List<Tarefa> findAllByUsuarioId(UUID usuarioId);

  /**
   * Busca todas as tarefas de um usuário com paginação.
   *
   * @param usuarioId o ID do usuário cujas tarefas devem ser buscadas.
   * @param pageable  informações de paginação.
   * @return uma página contendo as tarefas do usuário.
   * @pre O ID do usuário deve ser válido.
   * @post Retorna uma página com as tarefas do usuário.
   */
  Page<Tarefa> findAllByUsuarioId(UUID usuarioId, Pageable pageable);
}
