package br.com.myproject.projeto_para_estudo.core.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.myproject.projeto_para_estudo.core.entity.Usuario;

/*
 * - domain.port.out (Portas de Saída)
 * O que é?
    * Interfaces que definem as necessidades de serviços externos que o domínio e a aplicação possuem.
 * Qual seu trabalho?
    * Servir como um contrato formal para dependências externas, como persistência de dados ou serviços de terceiros.
    * Exemplo: interface UserRepository { void save(User user); }. O domínio diz "eu preciso salvar um usuário", mas não sabe como.
 */

public interface UsuarioPortOut {
   /**
    * Salva um usuário no banco de dados.
    *
    * @param user o usuário a ser salvo
    * @return o usuário salvo
    * @pre O usuário deve ser válido
    * @post O usuário é persistido no banco de dados
    * @throws RegraNegocioException se o usuário não for válido.
    */
   Usuario save(Usuario user);

   /**
    * Busca um usuário pelo email.
    *
    * @param email o email do usuário a ser buscado
    * @return um Optional contendo o usuário encontrado, ou vazio se não encontrado
    * @pre O email deve ser válido
    * @post O usuário é retornado se encontrado
    * @throws RegraNegocioException se o email não for válido.
    */
   Optional<Usuario> findByEmail(String email);

   /**
    * Busca um usuário pelo ID.
    *
    * @param id o ID do usuário a ser buscado
    * @return um Optional contendo o usuário encontrado, ou vazio se não encontrado
    * @pre O ID deve ser válido
    * @post O usuário é retornado se encontrado
    * @throws RegraNegocioException se o ID não for válido.
    */
   Optional<Usuario> findById(UUID id);

   /**
    * Deleta um usuário pelo ID.
    *
    * @param id o ID do usuário a ser deletado
    * @pre O ID deve ser válido
    * @post O usuário é removido do banco de dados
    * @throws RegraNegocioException se o ID não for válido.
    */
   void deleteById(UUID id);

   /**
    * Deleta um usuário.
    *
    * @param user o usuário a ser deletado
    * @pre O usuário deve ser válido
    * @post O usuário é removido do banco de dados
    * @throws RegraNegocioException se o usuário não for válido.
    */
   void delete(Usuario user);

   /**
    * Atualiza um usuário.
    *
    * @param user o usuário a ser atualizado
    * @pre O usuário deve ser válido
    * @post O usuário é atualizado no banco de dados
    * @throws RegraNegocioException se o usuário não for válido.
    */
   void update(Usuario user);

   /**
    * Atualiza a senha de um usuário.
    *
    * @param senhaAtual a senha atual do usuário
    * @param novaSenha a nova senha do usuário
    * @param confirmacaoSenha a confirmação da nova senha
    * @pre A senha atual deve ser válida
    * @post A senha do usuário é atualizada no banco de dados
    * @throws RegraNegocioException se a senha atual não for válida.
    */
   void update(String senhaAtual, String novaSenha, String confirmacaoSenha);

   /**
    * Verifica se um usuário existe pelo email.
    *
    * @param email o email do usuário a ser verificado
    * @return true se o usuário existir, false caso contrário
    * @pre O email deve ser válido
    * @post Retorna true se o usuário existir, false caso contrário
    */
   boolean existsByEmail(String email);

   /**
    * Verifica se um usuário existe pelo ID.
    *
    * @param id o ID do usuário a ser verificado
    * @return true se o usuário existir, false caso contrário
    * @pre O ID deve ser válido
    * @post Retorna true se o usuário existir, false caso contrário
    */
   boolean existsById(UUID id);

   /**
    * Busca todos os usuários.
    *
    * @return uma lista contendo todos os usuários
    * @pre Nenhuma
    * @post Retorna uma lista com todos os usuários
    */
   List<Usuario> findAll();

   /**
    * Busca todos os usuários com paginação.
    *
    * @param pageable informações de paginação
    * @return uma página contendo os usuários
    * @pre Nenhuma
    * @post Retorna uma página com os usuários
    */
   Page<Usuario> findAll(Pageable pageable);
}
