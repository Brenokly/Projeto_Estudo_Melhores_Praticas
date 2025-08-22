package br.com.myproject.projeto_para_estudo.core.application;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import br.com.myproject.projeto_para_estudo.core.port.in.TarefaPortIn;
import br.com.myproject.projeto_para_estudo.core.port.out.TarefaPortOut;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaRequest;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.tarefa.TarefaResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TarefaApplication implements TarefaPortIn {

  private final TarefaPortOut tarefaPortOut;

  @Override
  public TarefaResponse cadastrarTarefaDeUsuarioLogado(UUID id, TarefaRequest requestDTO, UserDetails usuarioInfos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'cadastrarTarefaDeUsuarioLogado'");
  }

  @Override
  public void deletarTarefaDeUsuarioLogado(UUID id, UserDetails usuarioInfos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deletarTarefaDeUsuarioLogado'");
  }

  @Override
  public TarefaResponse atualizarTarefaDeUsuarioLogado(UUID id, TarefaRequest requestDTO, UserDetails usuarioInfos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'atualizarTarefaDeUsuarioLogado'");
  }

  @Override
  public TarefaResponse buscarTarefaPorIdDeUsuarioLogado(UUID id, UserDetails usuarioInfos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscarTarefaPorIdDeUsuarioLogado'");
  }

  @Override
  public List<TarefaResponse> buscarTodasTarefasDeUsuarioLogado(UserDetails usuarioInfos) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'buscarTodasTarefasDeUsuarioLogado'");
  }

}
