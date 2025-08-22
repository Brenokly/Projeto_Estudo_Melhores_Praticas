package br.com.myproject.projeto_para_estudo.core.application;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.myproject.projeto_para_estudo.core.port.in.UsuarioPortIn;
import br.com.myproject.projeto_para_estudo.core.port.out.UsuarioPortOut;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioAtualizadoRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioSenhaAtualizadaRequest;
import lombok.RequiredArgsConstructor;

/*
 * Esta camada faz a ponte entre a infraestrutura e o domínio.
 * Ela contém a lógica de fluxo, mas não a lógica de negócio.
 *
 * O que é?
 *     A implementação concreta dos Casos de Uso definidos nas portas de entrada.
 * Qual seu trabalho?
 *     * Implementar uma interface de domain.port.in.
 *     * Coordenar o fluxo de uma operação:
        1.  Receber a chamada de um adaptador de entrada.
        2.  Usar as portas de saída para buscar dados.
        3.  Chamar o domain.service ou domain.model para executar a lógica de negócio.
        4.  Usar as portas de saída novamente para persistir o resultado.
 */

@RequiredArgsConstructor
public class UsuarioApplication implements UsuarioPortIn {

       private final UsuarioPortOut usuarioPortOut;
       private final PasswordEncoder passwordEncoder;

       @Override
       public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'cadastrarUsuario'");
       }

       @Override
       public UsuarioResponse buscarUsuarioLogado(UserDetails usuarioInfos) {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'buscarUsuarioLogado'");
       }

       @Override
       public UsuarioResponse atualizarDadosPessoaisDoUsuarioLogado(UUID id, UsuarioAtualizadoRequest requestDTO,
                     UserDetails usuarioInfos) {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'atualizarDadosPessoaisDoUsuarioLogado'");
       }

       @Override
       public void atualizarSenhaDoUsuarioLogado(UUID id, UsuarioSenhaAtualizadaRequest requestDTO,
                     UserDetails usuarioInfos) {
              // TODO Auto-generated method stub
              throw new UnsupportedOperationException("Unimplemented method 'atualizarSenhaDoUsuarioLogado'");
       }

}
