package br.com.myproject.projeto_para_estudo.infrastructure.adapter.out;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.myproject.projeto_para_estudo.core.entity.Tarefa;
import br.com.myproject.projeto_para_estudo.core.port.out.TarefaPortOut;

@Repository
public class TarefaAdapterOut implements TarefaPortOut {

  @Autowired
  private DataSource dataSource;

  @Override
  @Transactional()
  public Tarefa save(Tarefa tarefa) {
    String sql = "{call sp_SalvarTarefa(?,?,?,?,?,?)}";

    try (Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall(sql)) {

      cs.setString(1, tarefa.getTitulo());
      cs.setString(2, tarefa.getDescricao());
      cs.setDate(3, java.sql.Date.valueOf(tarefa.getDataVencimento()));
      cs.setObject(4, tarefa.isConcluida());
      cs.setObject(5, tarefa.getUsuario().getId());
      cs.registerOutParameter(6, java.sql.Types.VARCHAR);

      cs.execute();

      String novoIdString = cs.getString(6);
      UUID novoId = UUID.fromString(novoIdString);

      tarefa.setId(novoId);

      return tarefa;
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao salvar tarefa no banco de dados.", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Tarefa> findById(UUID id) {
    String sql = "{SELECT * FROM ufn_BuscarTarefaPorId(?)}";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, id);

      Tarefa tarefa = null;

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UUID tarefaId = UUID.fromString(rs.getString("id"));
          String tarefaTitulo = rs.getString("titulo");
          String tarefaDescricao = rs.getString("descricao");
          LocalDate tarefaDataVencimento = rs.getDate("data_vencimento").toLocalDate();
          boolean tarefaConcluida = rs.getBoolean("concluida");
          UUID usuarioId = UUID.fromString(rs.getString("usuario_id"));

          tarefa = new Tarefa(tarefaId, tarefaTitulo, tarefaDescricao, tarefaDataVencimento, tarefaConcluida,
              usuarioId);
        }
      }

      return Optional.of(tarefa);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar tarefa por ID no banco de dados.", e);
    }
  }

  @Override
  @Transactional()
  public void deleteById(UUID id) {
    String sql = "{call sp_DeleteTarefaPorId(?)}";

    try (Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall(sql)) {

      cs.setObject(1, id);
      cs.execute();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar tarefa por ID no banco de dados.", e);
    }
  }

  @Override
  @Transactional()
  public void delete(Tarefa tarefa) {
    String sql = "{call sp_DeleteTarefa(?)}";

    try (Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall(sql)) {

      cs.setObject(1, tarefa.getId());
      cs.execute();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao deletar tarefa no banco de dados.", e);
    }
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  @Transactional()
  public void update(Tarefa tarefa) {
    String sql = "{call sp_AtualizarTarefa(?,?,?,?,?,?)}";

    try (Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall(sql)) {

      cs.setObject(1, tarefa.getId());
      cs.setString(2, tarefa.getTitulo());
      cs.setString(3, tarefa.getDescricao());
      cs.setDate(4, java.sql.Date.valueOf(tarefa.getDataVencimento()));
      cs.setObject(5, tarefa.isConcluida());
      cs.setObject(6, tarefa.getUsuario().getId());

      cs.execute();
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao atualizar tarefa no banco de dados.", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(UUID id) {
    String sql = "{? = call ufn_TarefaExistePorId(?)}";

    try (Connection conn = dataSource.getConnection();
        CallableStatement cs = conn.prepareCall(sql)) {

      cs.registerOutParameter(1, Types.BIT);
      cs.setObject(2, id);
      cs.execute();

      return cs.getBoolean(1);
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao verificar existência de tarefa por ID no banco de dados.", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Tarefa> findAllByUsuarioId(UUID usuarioId) {
    String sql = "SELECT * FROM ufn_BuscarTodasTarefasDeUmUsuario(?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, usuarioId);

      try (ResultSet rs = ps.executeQuery()) {
        List<Tarefa> tarefas = new ArrayList<>();
        while (rs.next()) {
          UUID tarefaId = UUID.fromString(rs.getString("id"));
          String titulo = rs.getString("titulo");
          String descricao = rs.getString("descricao");
          LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
          boolean concluida = rs.getBoolean("concluida");

          Tarefa tarefa = new Tarefa(tarefaId, titulo, descricao, dataVencimento, concluida, usuarioId);
          tarefas.add(tarefa);
        }
        return tarefas;
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar tarefas por ID de usuário no banco de dados.", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Tarefa> findAllByUsuarioId(UUID usuarioId, Pageable pageable) {
    int pagina = pageable.getPageNumber();
    int tamanho = pageable.getPageSize();

    String sql = "SELECT * FROM ufn_BuscarTodasTarefasDeUmUsuarioComPaginacao(?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, usuarioId);
      ps.setInt(2, pagina);
      ps.setInt(3, tamanho);

      List<Tarefa> tarefas = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          UUID tarefaId = UUID.fromString(rs.getString("id"));
          String titulo = rs.getString("titulo");
          String descricao = rs.getString("descricao");
          LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
          boolean concluida = rs.getBoolean("concluida");

          Tarefa tarefa = new Tarefa(tarefaId, titulo, descricao, dataVencimento, concluida, usuarioId);
          tarefas.add(tarefa);
        }
      }

      long totalElementos = countByUsuarioId(usuarioId);

      return new PageImpl<>(tarefas, pageable, totalElementos);

    } catch (SQLException e) {
      throw new RuntimeException("Erro ao buscar tarefas paginadas por ID de usuário no banco de dados.", e);
    }
  }

  // Método auxiliar para contar o total de tarefas de um usuário.
  public long countByUsuarioId(UUID usuarioId) {
    String sql = "SELECT count(*) FROM tarefa WHERE usuario_id = ?";
    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setObject(1, usuarioId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erro ao contar tarefas por ID de usuário.", e);
    }
    return 0;
  }

}
