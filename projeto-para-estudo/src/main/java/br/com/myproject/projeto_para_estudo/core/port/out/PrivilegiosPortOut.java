package br.com.myproject.projeto_para_estudo.core.port.out;

import java.util.Optional;
import java.util.UUID;

import br.com.myproject.projeto_para_estudo.core.entity.Privilegios;
import br.com.myproject.projeto_para_estudo.infrastructure.exception.PrivilegiosDuplicadoException;
import br.com.myproject.projeto_para_estudo.infrastructure.exception.PrivilegiosNaoEncontradoException;
import br.com.myproject.projeto_para_estudo.infrastructure.exception.RoleNaoEncontradaException;
import br.com.myproject.projeto_para_estudo.infrastructure.exception.UsuarioNaoEncontradoException;

public interface PrivilegiosPortOut {

  /**
   * Salva um privilegio.
   *
   * @param privilegio Um privilegio que deve ser salva.
   * @return Recebe um privilegio para ser salva no banco.
   * @pre Um privilegio deve ser válido;
   * @post Um privilegio é salva e retornada
   * @throws PrivilegiosDuplicadoException Se o privilegio já existir.
   */
  Privilegios save(Privilegios privilegio);

  /**
   * Busca um privilegio pelo id.
   *
   * @param id O ID do privilegio que deve ser buscado.
   * @return Recebe um privilegio correspondente ao ID informado.
   * @pre O ID deve ser válido;
   * @post A privilegio correspondente ao ID é retornada
   * @throws RoleNaoEncontradaException Se a privilegio com o ID fornecido não for
   *                                    encontrada.
   */
  Optional<Privilegios> findById(UUID id);

  /**
   * Busca um privilegio pelo nome.
   *
   * @param nome O nome do privilegio que deve ser buscado.
   * @return Recebe um privilegio correspondente ao nome informado.
   * @pre O nome deve ser válido;
   * @post A privilegio correspondente ao nome é retornada
   * @throws RoleNaoEncontradaException Se a privilegio com o nome fornecido não
   *                                    for
   *                                    encontrada.
   */
  Optional<Privilegios> findByNome(String nome);

  /**
   * Deleta um privilegio pelo id.
   *
   * @param id O ID do privilegio que deve ser deletado.
   * @pre O ID deve ser válido;
   * @post A privilegio correspondente ao ID é deletada
   * @throws RoleNaoEncontradaException Se a privilegio com o ID fornecido não for
   *                                    encontrada.
   */
  void deleteById(UUID id);

  /**
   * Deleta um privilegio.
   *
   * @param privilegio O privilegio que deve ser deletado.
   * @pre O privilegio deve ser válido;
   * @post O privilegio é deletado
   * @throws RoleNaoEncontradaException Se a privilegio com o ID fornecido não for
   *                                    encontrada.
   */
  void delete(Privilegios privilegio);

  /**
   * Adiciona um privilegio a um usuário.
   *
   * @param roleid       O ID do usuário que receberá o privilegio.
   * @param privilegioId O ID do privilegio que será adicionada.
   * @pre O ID do usuário e o ID do privilegio devem ser válidos;
   * @post O privilegio é adicionado ao usuário
   * @throws RoleNaoEncontradaException    Se o privilegio com o ID fornecido não
   *                                       for
   *                                       encontrado.
   * @throws UsuarioNaoEncontradoException Se o usuário com o ID fornecido não for
   *                                       encontrado.
   */
  void addPrivilegioToRole(UUID roleId, UUID privilegioId);

  /**
   * Salva um privilegio
   *
   * @param roleId       O ID da role que perderá o privilegio.
   * @param privilegioId O ID do privilegio que deve ser salva.
   * @return Recebe um privilegio para ser salva no banco.
   * @pre O privilegio deve ser válido;
   * @post O privilegio é salva e retornada
   * @throws RoleNaoEncontradaException        Se a role com o ID fornecido não
   *                                           existir.
   * @throws PrivilegiosNaoEncontradoException Se o privilegio com o ID fornecido
   *                                           não
   *                                           existir.
   */
  void removePrivilegioFromRole(UUID roleId, UUID privilegioId);

}