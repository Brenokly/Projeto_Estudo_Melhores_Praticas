package br.com.myproject.projeto_para_estudo.core.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roles implements GrantedAuthority {
  private UUID id;
  private String nome;
  private List<Usuario> usuarios;
  private List<Privilegios> privilegios;

  @Override
  public String getAuthority() {
    return "ROLE_" + nome;
  }

}
