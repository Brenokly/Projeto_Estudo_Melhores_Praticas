package br.com.myproject.projeto_para_estudo.core.application;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.core.port.in.UsuarioPortIn;
import br.com.myproject.projeto_para_estudo.core.port.out.UsuarioPortOut;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioAtualizadoRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioSenhaAtualizadaRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.mapper.UsuarioMapper;
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

@Service
@RequiredArgsConstructor
public class UsuarioApplication implements UsuarioPortIn {

       private final UsuarioPortOut usuarioPortOut;
       private final PasswordEncoder passwordEncoder;
       private final UsuarioMapper usuarioMapper;

       @Override
       public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {
              if (usuarioRequest == null) {
                     throw new IllegalArgumentException("Dados de usuário inválido");
              }

              Usuario usuario = usuarioMapper.toDomain(usuarioRequest);

              usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

              usuario = usuarioPortOut.save(usuario);

              if (usuario == null) {
                     throw new RuntimeException("Erro ao cadastrar usuário");
              }

              return usuarioMapper.toResponse(usuario);

       }

       @Override
       public UsuarioResponse buscarUsuarioLogado(UserDetails usuarioInfos) {
              if (usuarioInfos == null) {
                     throw new IllegalArgumentException("Usuário não autenticado");
              }

              Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

              return usuarioPortOut.findByEmail(usuario.getEmail())
                            .map(usuarioMapper::toResponse)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
       }

       @Override
       public UsuarioResponse atualizarDadosPessoaisDoUsuarioLogado(UUID id, UsuarioAtualizadoRequest requestDTO,
                     UserDetails usuarioInfos) {

              if (requestDTO == null || usuarioInfos == null || id == null) {
                     throw new IllegalArgumentException("Dados inválidos");
              }

              Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

              if (!usuario.getId().equals(id)) {
                     throw new AccessDeniedException("Ação permitida apenas para o usuário autenticado.");
              }

              usuario = usuarioPortOut.findById(id)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

              usuario.setNome(requestDTO.nome());

              usuarioPortOut.save(usuario);

              return usuarioMapper.toResponse(usuario);
       }

       @Override
       public void atualizarSenhaDoUsuarioLogado(UUID id, UsuarioSenhaAtualizadaRequest requestDTO,
                     UserDetails usuarioInfos) {
              if (requestDTO == null || usuarioInfos == null || id == null) {
                     throw new IllegalArgumentException("Dados inválidos");
              }

              Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);
              if (!usuario.getId().equals(id)) {
                     throw new AccessDeniedException("Ação permitida apenas para o usuário autenticado.");
              }

              usuario = usuarioPortOut.findById(id)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

              usuarioPortOut.update(requestDTO.senhaAtual(), requestDTO.novaSenha(), requestDTO.confirmacaoNovaSenha());

              usuarioPortOut.save(usuario);
       }

       private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
              if (!(userDetails instanceof Usuario)) {
                     throw new AccessDeniedException("Ação permitida apenas para usuários autenticados.");
              }
              return (Usuario) userDetails;
       }
}
