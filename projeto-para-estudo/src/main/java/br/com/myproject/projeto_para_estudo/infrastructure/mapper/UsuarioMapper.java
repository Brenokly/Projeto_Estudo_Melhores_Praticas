package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.usuario.UsuarioResponse;

@Component
public class UsuarioMapper {

  private final TarefaMapper tarefaMapper;

  public UsuarioMapper(TarefaMapper tarefaMapper) {
    this.tarefaMapper = tarefaMapper;
  }

  public UsuarioResponse toResponse(Usuario usuario) {
    return new UsuarioResponse(
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
