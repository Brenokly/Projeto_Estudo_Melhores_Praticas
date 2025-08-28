package br.com.myproject.projeto_para_estudo.core.entity;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Privilegios {
  private UUID id;
  private String nome;
  private List<Roles> roles;

  public Privilegios(UUID id, String nome) {
    this.id = id;
    this.nome = nome;
  }
}
