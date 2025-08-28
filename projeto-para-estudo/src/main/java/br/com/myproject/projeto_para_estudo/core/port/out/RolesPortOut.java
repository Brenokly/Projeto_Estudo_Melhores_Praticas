package br.com.myproject.projeto_para_estudo.core.port.out;

import java.util.Optional;
import java.util.UUID;

import br.com.myproject.projeto_para_estudo.core.entity.Roles;
import br.com.myproject.projeto_para_estudo.infrastructure.exception.RoleNaoEncontradaException;

public interface RolesPortOut {

  /**
   * Salva uma role.
   *
   * @param role A role que deve ser salva.
   * @return Recebe uma role para ser salva no banco.
   * @pre A role deve ser válida;
   * @post A role é salva e retornada
   * @throws RolesDuplicadaException Se a role com o ID fornecido já existir.
   */
  Roles save(Roles role);

  /**
   * Busca uma role pelo id.
   *
   * @param id O ID da role que deve ser buscada.
   * @return Recebe uma role correspondente ao ID informado.
   * @pre O ID deve ser válido;
   * @post A role correspondente ao ID é retornada
   * @throws RoleNaoEncontradaException Se a role com o ID fornecido não for
   *                                    encontrada.
   */
  Optional<Roles> findById(UUID id);

  /**
   * Busca uma role pelo nome.
   *
   * @param nome O nome da role que deve ser buscada.
   * @return Recebe uma role correspondente ao nome informado.
   * @pre O nome deve ser válido;
   * @post A role correspondente ao nome é retornada
   * @throws RoleNaoEncontradaException Se a role com o nome fornecido não for
   *                                    encontrada.
   */
  Optional<Roles> findByNome(String nome);

  /**
   * Deleta uma role pelo id.
   *
   * @param id O ID da role que deve ser deletada.
   * @pre O ID deve ser válido;
   * @post A role correspondente ao ID é deletada
   * @throws RoleNaoEncontradaException Se a role com o ID fornecido não for
   *                                    encontrada.
   */
  void deleteById(UUID id);

  /**
   * Deleta uma role.
   *
   * @param role A role que deve ser deletada.
   * @pre A role deve ser válida;
   * @post A role é deletada
   * @throws RoleNaoEncontradaException Se a role com o ID fornecido não for
   *                                    encontrada.
   */
  void delete(Roles role);

  /**
   * Adiciona uma role a um usuário.
   *
   * @param userId O ID do usuário que receberá a role.
   * @param roleId O ID da role que será adicionada.
   * @pre O ID do usuário e o ID da role devem ser válidos;
   * @post A role é adicionada ao usuário
   * @throws RoleNaoEncontradaException Se a role com o ID fornecido não for encontrada.
   * @throws UsuarioNaoEncontradoException Se o usuário com o ID fornecido não for encontrado.
   */
  void addRoleToUser(UUID userId, UUID roleId);

  /**
   * Salva uma role
   *
   * @param role A role que deve ser salva.
   * @return Recebe uma role para ser salva no banco.
   * @pre A role deve ser válida;
   * @post A role é salva e retornada
   * @throws
   */
  void removeRoleFromUser(UUID userId, UUID roleId);

}
