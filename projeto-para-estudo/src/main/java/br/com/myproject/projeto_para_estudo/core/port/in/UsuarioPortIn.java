package br.com.myproject.projeto_para_estudo.core.port.in;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioAtualizadoRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioSenhaAtualizadaRequest;

/*
 * - domain.port.in (Portas de Entrada)
 * O que é?
    *  Interfaces que definem os Casos de Uso (funcionalidades) que a aplicação expõe ao mundo exterior.
 * Qual seu trabalho?
    * Servir como um contrato formal das ações que podem ser executadas na aplicação.
    * Exemplo: interface RunSimulationUseCase { void execute(); }. O nome descreve a intenção do usuário.
 */

public interface UsuarioPortIn {

   /**
    * Cadastra um novo usuário.
    *
    * @param usuarioRequest os dados do usuário a serem cadastrados.
    * @return os dados do usuário cadastrado.
    * @pre O usuário deve ser válido.
    * @post O usuário é cadastrado com sucesso.
    */
   UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest);

   /**
    * Busca um usuário pelo ID.
    *
    * @param id o ID do usuário a ser buscado.
    * @return um Optional contendo o usuário encontrado, ou vazio se não
    *         encontrado.
    * @pre O ID deve ser válido.
    * @post O usuário é retornado se encontrado.
    * @throws Exception se o ID não for válido.
    */
   UsuarioResponse buscarUsuarioLogado(UserDetails usuarioInfos);

   /**
    * Atualiza os dados pessoais do usuário logado.
    *
    * @param id           o ID do usuário a ser atualizado.
    * @param requestDTO   os dados atualizados do usuário.
    * @param usuarioInfos as informações do usuário logado.
    * @return os dados do usuário atualizado.
    * @pre O ID deve ser válido.
    * @post O usuário é atualizado com sucesso.
    */
   UsuarioResponse atualizarDadosPessoaisDoUsuarioLogado(UUID id, UsuarioAtualizadoRequest requestDTO,
         UserDetails usuarioInfos);

   /**
    * Atualiza a senha do usuário logado.
    *
    * @param id           o ID do usuário cuja senha será atualizada.
    * @param requestDTO   os dados da nova senha.
    * @param usuarioInfos as informações do usuário logado.
    * @return os dados do usuário com a senha atualizada.
    * @pre O ID deve ser válido.
    * @post A senha do usuário é atualizada com sucesso.
    */
   void atualizarSenhaDoUsuarioLogado(UUID id, UsuarioSenhaAtualizadaRequest requestDTO, UserDetails usuarioInfos);
}
