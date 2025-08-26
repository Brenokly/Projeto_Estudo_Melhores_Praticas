package br.com.myproject.projeto_para_estudo.core.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
  private UUID id;
  private String nome;
  private String email;
  private String senha;
  private List<Tarefa> tarefas = new ArrayList<>();

  public Usuario(UUID id, String nome, String email, String senha) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
  }

  public Usuario(String nome, String email, String senha) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
  }

  public Usuario(UUID id) {
    this.id = id;
    this.nome = "";
    this.email = "";
    this.senha = "";
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

  // tostring
  @Override
  public String toString() {
    return "Usuario{" +
        "id=" + id +
        ", nome='" + nome + '\'' +
        ", email='" + email + '\'' +
        ", senha='" + senha + '\'' +
        '}';
  }
}
