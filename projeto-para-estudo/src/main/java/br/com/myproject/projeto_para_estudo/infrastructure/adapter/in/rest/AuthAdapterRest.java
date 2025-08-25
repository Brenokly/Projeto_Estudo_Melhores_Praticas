package br.com.myproject.projeto_para_estudo.infrastructure.adapter.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.myproject.projeto_para_estudo.core.application.ApplicationUserDetailsService;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.AuthenticationRequestDTO;
import br.com.myproject.projeto_para_estudo.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Endpoints para autenticação de usuários")
@RequiredArgsConstructor
public class AuthAdapterRest {
  private final AuthenticationManager authenticationManager;
  private final ApplicationUserDetailsService authenticationService;
  private final JwtService jwtService;

  @PostMapping("/login")
  @Operation(summary = "Realiza o login do usuário", description = "Realiza o login de usuários e retorna o token JWT via cookie HttpOnly.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida."),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
  })
  public ResponseEntity<Void> login(@RequestBody @Valid AuthenticationRequestDTO request,
      HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    // Cria e configura o SecurityContext com a autenticação
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);

    // Salva na sessão HTTP o SecurityContext para manter o usuário autenticado via
    // sessão
    HttpSession session = httpRequest.getSession(true);
    session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

    // Gera o token JWT com base no UserDetails
    final UserDetails user = authenticationService.loadUserByUsername(request.email());
    final String token = jwtService.generateToken(user);

    Cookie cookie = new Cookie("jwt", token);
    cookie.setHttpOnly(true); // Impede acesso via JavaScript
    cookie.setSecure(true); // Apenas HTTPS
    cookie.setPath("/"); // Disponível para toda a aplicação
    cookie.setMaxAge(7 * 24 * 60 * 60); // Expiração em 7 dias

    httpResponse.addHeader("Set-Cookie", cookie.toString());

    return ResponseEntity.ok().build();
  }

}
