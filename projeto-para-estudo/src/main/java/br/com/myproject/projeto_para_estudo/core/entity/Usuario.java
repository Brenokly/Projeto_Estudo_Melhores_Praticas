package br.com.myproject.projeto_para_estudo.core.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {
  private UUID id;
  private String nome;
  private String email;
  private String senha;
  private List<Tarefa> tarefas;

  public Usuario(UUID id, String nome, String email) {
    this.id = id;
    this.nome = nome;
    this.email = email;
  }

  public Usuario(String nome, String email, String senha) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
  }

  public Usuario(UUID id) {
    this.id = id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // No momento o projeto está sem ROLE's
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return this.senha;
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
