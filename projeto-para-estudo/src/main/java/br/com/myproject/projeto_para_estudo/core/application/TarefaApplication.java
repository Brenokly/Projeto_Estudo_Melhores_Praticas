package br.com.myproject.projeto_para_estudo.core.application;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.myproject.projeto_para_estudo.core.entity.Tarefa;
import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.core.port.in.TarefaPortIn;
import br.com.myproject.projeto_para_estudo.core.port.out.TarefaPortOut;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaAtualizarRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.mapper.TarefaMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarefaApplication implements TarefaPortIn {

  private final TarefaPortOut tarefaPortOut;
  private final TarefaMapper tarefaMapper;

  @Override
  public TarefaResponse cadastrarTarefaDeUsuarioLogado(UUID id, TarefaRequest requestDTO, UserDetails usuarioInfos) {
    if (id == null || requestDTO == null || usuarioInfos == null) {
      throw new IllegalArgumentException("Dados inválidos");
    }

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);
    if (!usuario.getId().equals(id)) {
      throw new AccessDeniedException("Ação não permitida para este usuário.");
    }

    Tarefa tarefa = tarefaPortOut.save(tarefaMapper.toDomain(requestDTO));

    return tarefaMapper.toResponseDTO(tarefa);
  }

  @Override
  public void deletarTarefaDeUsuarioLogado(UUID id, UserDetails usuarioInfos) {
    if (id == null || usuarioInfos == null) {
      throw new IllegalArgumentException("Dados Inválidos");
    }

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

    Tarefa tarefa = tarefaPortOut.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

    if (!tarefa.getUsuario().equals(usuario)) {
      throw new AccessDeniedException("Ação não permitida para este usuário.");
    }

    tarefaPortOut.delete(tarefa);
  }

  @Override
  public TarefaResponse atualizarTarefaDeUsuarioLogado(UUID id, TarefaAtualizarRequest requestDTO,
      UserDetails usuarioInfos) {
    if (id == null || requestDTO == null || usuarioInfos == null) {
      throw new IllegalArgumentException("Dados inválidos");
    }

    Tarefa tarefa = tarefaPortOut.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

    if (!tarefa.getUsuario().equals(usuario)) {
      throw new AccessDeniedException("Ação não permitida para este usuário.");
    }

    return tarefaMapper.toResponseDTO(tarefaPortOut.save(tarefaMapper.toDomain(requestDTO)));
  }

  @Override
  public TarefaResponse buscarTarefaPorIdDeUsuarioLogado(UUID id, UserDetails usuarioInfos) {
    if (id == null || usuarioInfos == null) {
      throw new IllegalArgumentException("Dados inválidos");
    }

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

    Tarefa tarefa = tarefaPortOut.findById(id).orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada"));

    if (!tarefa.getUsuario().equals(usuario)) {
      throw new AccessDeniedException("Ação não permitida para este usuário.");
    }

    return tarefaMapper.toResponseDTO(tarefa);
  }

  @Override
  public List<TarefaResponse> buscarTodasTarefasDeUsuarioLogado(UserDetails usuarioInfos) {
    if (usuarioInfos == null) {
      throw new IllegalArgumentException("Dados inválidos");
    }

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

    List<Tarefa> tarefas = tarefaPortOut.findAllByUsuarioId(usuario.getId());

    return tarefas.stream()
        .map(tarefaMapper::toResponseDTO)
        .toList();
  }

  @Override
  public List<TarefaResponse> buscarTodasTarefasDeUsuarioLogadoComPaginacao(UserDetails usuarioInfos, Pageable page) {
    if (usuarioInfos == null || page == null) {
      throw new IllegalArgumentException("Dados inválidos");
    }

    Usuario usuario = getUsuarioFromUserDetails(usuarioInfos);

    List<Tarefa> tarefas = tarefaPortOut.findAllByUsuarioId(usuario.getId(), page).getContent();

    return tarefas.stream()
        .map(tarefaMapper::toResponseDTO)
        .toList();
  }

  private Usuario getUsuarioFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Usuario)) {
      throw new AccessDeniedException("Ação permitida apenas para usuários autenticados.");
    }
    return (Usuario) userDetails;
  }

}
