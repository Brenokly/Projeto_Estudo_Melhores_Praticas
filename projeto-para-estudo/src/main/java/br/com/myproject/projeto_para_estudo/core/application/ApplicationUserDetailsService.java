package br.com.myproject.projeto_para_estudo.core.application;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.core.port.out.UsuarioPortOut;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

  private final UsuarioPortOut userPort;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // 1. Usa nossa porta de repositório para buscar nosso User do domínio
    Usuario domainUser = userPort.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

    // 2. "Traduz" nosso User do domínio para o User do Spring Security
    // O construtor aceita: username, password, e uma coleção de authorities
    // (perfis/regras)
    return new org.springframework.security.core.userdetails.User(
        domainUser.getEmail(),
        domainUser.getPassword(),
        new ArrayList<>() // Deixamos a lista de permissões vazia por enquanto
    );
  }
}
