package br.com.myproject.projeto_para_estudo.core.entity;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entidades JPA puras: Sem dependência do mundo externo ou de framework

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {
  private UUID id;
  private String titulo;
  private String descricao;
  private LocalDate dataVencimento;
  private boolean concluida;
  private Usuario usuario;

  public Tarefa(String titulo, String descricao, LocalDate dataVencimento, boolean concluida, Usuario usuario) {
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataVencimento = dataVencimento;
    this.concluida = concluida;
    this.usuario = usuario;
  }

  public Tarefa(UUID id, String titulo, String descricao, LocalDate dataVencimento, boolean concluida, UUID usuarioId) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataVencimento = dataVencimento;
    this.concluida = concluida;
    this.usuario = new Usuario(usuarioId);
  }
}
