package br.com.myproject.projeto_para_estudo.infrastructure.adapter.in.rest;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.myproject.projeto_para_estudo.core.port.in.UsuarioPortIn;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioAtualizadoRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioSenhaAtualizadaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/*
 * - infrastructure.adapter.in (Adaptadores de Entrada)
 * O que é?
    * Classes que recebem estímulos do mundo exterior e os traduzem em chamadas para a camada de application.
 * Qual seu trabalho?
    * Adaptar um protocolo específico (ex: HTTP, Linha de Comando) para uma chamada de método Java.
    * rest.controller: Recebe uma requisição HTTP, converte o JSON (usando um DTO) para um objeto de comando e chama o serviço da application.
    * cli: Lê argumentos da linha de comando e chama o serviço da application.
 */

@RestController
@RequestMapping("api/v1/usuarios")
@Tag(name = "usuarios", description = "Endpoints para gerenciamento de usuários")
@RequiredArgsConstructor
public class UsuarioAdapterInRest {
   private final UsuarioPortIn usuarioUseCases;

   @PostMapping()
   @Operation(summary = "Cadastra um novo usuário", description = "Cria um novo usuário no sistema.")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
         @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou regra de negócio violada.")
   })
   public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioRequest requestDTO) {
      UsuarioResponse responseDTO = usuarioUseCases.cadastrarUsuario(requestDTO);
      URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(responseDTO.id())
            .toUri();

      return ResponseEntity.created(location).body(responseDTO);
   }

   @GetMapping("/meu-perfil")
   @SecurityRequirement(name = "bearerAuth")
   @Operation(summary = "Busca os dados do usuário autenticado", description = "Retorna os detalhes do usuário autenticado.")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Dados do usuário retornados."),
         @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
   })
   public ResponseEntity<UsuarioResponse> buscarMeuPerfil(Authentication authentication) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      return ResponseEntity.ok(usuarioUseCases.buscarUsuarioLogado(userDetails));
   }

   @PatchMapping("/{id}/dados-pessoais")
   @SecurityRequirement(name = "bearerAuth")
   @Operation(summary = "Atualiza os dados pessoais de um usuário", description = "Atualiza o nome e/ou data de nascimento. Requer autenticação do próprio usuário..")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso."),
         @ApiResponse(responseCode = "403", description = "Acesso negado."),
         @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
   })
   public ResponseEntity<UsuarioResponse> atualizarDadosPessoais(@PathVariable UUID id,
         @RequestBody @Valid UsuarioAtualizadoRequest requestDTO, Authentication authentication) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      UsuarioResponse usuarioAtualizado = usuarioUseCases.atualizarDadosPessoaisDoUsuarioLogado(id, requestDTO,
            userDetails);
      return ResponseEntity.ok(usuarioAtualizado);
   }

   @PatchMapping("/{id}/senha")
   @SecurityRequirement(name = "bearerAuth")
   @Operation(summary = "Atualiza a senha de um Usuario", description = "Atualiza a senha. Requer a senha atual e autenticação do próprio Usuario.")
   @ApiResponses(value = {
         @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso."),
         @ApiResponse(responseCode = "400", description = "Regra de negócio violada (ex: senha atual incorreta)."),
         @ApiResponse(responseCode = "403", description = "Acesso negado."),
         @ApiResponse(responseCode = "404", description = "Usuario não encontrado.")
   })
   public ResponseEntity<Void> atualizarSenha(@PathVariable UUID id,
         @RequestBody @Valid UsuarioSenhaAtualizadaRequest requestDTO, Authentication authentication) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      usuarioUseCases.atualizarSenhaDoUsuarioLogado(id, requestDTO, userDetails);
      return ResponseEntity.noContent().build();
   }
}
