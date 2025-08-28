package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.OnlyUsuarioResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

  private final TarefaMapper tarefaMapper;
  private final RolesMapper rolesMapper;

  public UsuarioResponse toResponse(Usuario usuario) {
    return new UsuarioResponse(
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getTarefas().stream()
            .map(tarefaMapper::toResponseDTO)
            .toList(),
        usuario.getRoles().stream()
            .map(rolesMapper::toResponseOnly)
            .toList());
  }

  public OnlyUsuarioResponse toResponseOnly(Usuario usuario) {
    return new OnlyUsuarioResponse(
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getTarefas().stream()
            .map(tarefaMapper::toResponseDTO)
            .toList());
  }

  public Usuario toDomain(UsuarioRequest request) {
    return new Usuario(
        request.nome(),
        request.email(),
        request.senha());
  }
}
