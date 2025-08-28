package br.com.myproject.projeto_para_estudo.infrastructure.adapter.out;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

import br.com.myproject.projeto_para_estudo.core.entity.Roles;
import br.com.myproject.projeto_para_estudo.core.entity.Usuario;
import br.com.myproject.projeto_para_estudo.core.port.out.UsuarioPortOut;
import lombok.RequiredArgsConstructor;

/*
 * - infrastructure.adapter.out (Adaptadores de Saída)
 * O que é?
    * Classes que implementam as interfaces das portas de saída (domain.port.out).
 * Qual seu trabalho?
    * Conectar o contrato abstrato da porta com uma tecnologia concreta.
    * persistence.repository: Implementa UserRepository usando JPA/Hibernate para se comunicar com um banco de dados SQL.
    * random: Implementa RandomPort usando Math.random() para gerar um número aleatório.
 */

@Repository
@RequiredArgsConstructor
public class UsuarioAdapterOut implements UsuarioPortOut {

   private final DataSource dataSource;
   private final JdbcTemplate jdbcTemplate;

   @Override
   @Transactional
   public Usuario save(Usuario user) {
      String sql = "{call sp_SalvarUsuario(?,?,?,?,?)}";

      List<UUID> roleIds = user.getRoles().stream()
            .map(Roles::getId)
            .collect(Collectors.toList());

      List<SqlParameter> declaredParams = Arrays.asList(
            new SqlParameter("p_nome", java.sql.Types.VARCHAR),
            new SqlParameter("p_email", java.sql.Types.VARCHAR),
            new SqlParameter("p_senha_hash", java.sql.Types.VARCHAR),
            new SqlParameter("p_roles", microsoft.sql.Types.STRUCTURED, "ListaDeIds"),
            new SqlOutParameter("p_usuario_id", java.sql.Types.VARCHAR));

      Map<String, Object> resultMap = jdbcTemplate.call(conn -> {
         CallableStatement cs = conn.prepareCall(sql);
         cs.setString(1, user.getNome());
         cs.setString(2, user.getEmail());
         cs.setString(3, user.getSenha());

         SQLServerDataTable rolesTable = new SQLServerDataTable();
         rolesTable.addColumnMetadata("id", java.sql.Types.CHAR);

         for (UUID roleId : roleIds) {
            rolesTable.addRow(roleId.toString());
         }

         cs.setObject(4, rolesTable);
         cs.registerOutParameter(5, java.sql.Types.VARCHAR);

         return cs;
      }, declaredParams);

      String novoIdString = (String) resultMap.get("p_usuario_id");
      UUID novoId = UUID.fromString(novoIdString);
      user.setId(novoId);

      return user;
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<Usuario> findByEmail(String email) {
      String sql = "{call sp_BuscarUsuarioComRolesPorEmail(?)}";

      Usuario usuarioCompleto = jdbcTemplate.execute(sql, (CallableStatementCallback<Usuario>) cs -> {
         cs.setString(1, email);
         cs.execute();

         Usuario usuario = null;

         // Processa o primeiro resultado (os dados do usuário)
         try (ResultSet rs = cs.getResultSet()) {
            if (rs.next()) {
               usuario = new Usuario();
               usuario.setId(rs.getObject("id", UUID.class));
               usuario.setNome(rs.getString("nome"));
               usuario.setEmail(rs.getString("email"));
               usuario.setSenha(rs.getString("senha"));
               usuario.setRoles(new ArrayList<>());
            }
         }

         // Se um usuário foi encontrado, processa o segundo resultado (as roles)
         if (usuario != null && cs.getMoreResults()) {
            try (ResultSet rs = cs.getResultSet()) {
               while (rs.next()) {
                  Roles role = new Roles();
                  role.setId(rs.getObject("id", UUID.class));
                  role.setNome(rs.getString("nome"));
                  usuario.getRoles().add(role);
               }
            }
         }

         return usuario;
      });

      return Optional.ofNullable(usuarioCompleto);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<Usuario> findById(UUID id) {
      String sql = "SELECT * FROM ufn_BuscarUsuarioPorId(?)";

      try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

         ps.setObject(1, id);

         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
               UUID usuarioId = UUID.fromString(rs.getString("id"));
               String usuarioNome = rs.getString("nome");
               String usuarioEmail = rs.getString("email");
               String usuarioSenha = rs.getString("senha");

               return Optional.of(new Usuario(usuarioId, usuarioNome, usuarioEmail, usuarioSenha));
            } else {
               return Optional.empty();
            }
         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao buscar usuário por email no banco de dados.", e);
      }
   }

   @Override
   @Transactional
   public void deleteById(UUID id) {
      String sql = "{call sp_DeleteUsuarioPorId(?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.setObject(1, id);
         cs.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao deletar usuário por ID no banco de dados.", e);
      }
   }

   @Override
   @Transactional
   public void delete(Usuario user) {
      String sql = "{call sp_DeleteUsuarioPorId(?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.setObject(1, user.getId());
         cs.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao deletar usuário no banco de dados.", e);
      }
   }

   @Override
   @Transactional
   public Optional<Usuario> update(Usuario user) {
      String sql = "{call sp_AtualizarDadosUsuario(?, ?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.setObject(1, user.getId());
         cs.setString(2, user.getNome());

         cs.execute();

         return findById(user.getId());
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao atualizar usuário no banco de dados.", e);
      }
   }

   @Override
   @Transactional
   public void update(String senhaAtual, String novaSenha, String confirmacaoSenha) {
      String sql = "{call sp_AtualizarSenhaUsuario(?,?,?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.setString(1, senhaAtual);
         cs.setString(2, novaSenha);
         cs.setString(3, confirmacaoSenha);

         cs.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao atualizar senha do usuário no banco de dados.", e);
      }
   }

   @Override
   @Transactional(readOnly = true)
   public boolean existsByEmail(String email) {
      String sql = "{? = call ufn_UsuarioExistePorEmail(?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.registerOutParameter(1, Types.BIT);

         cs.setString(2, email);

         cs.execute();

         return cs.getBoolean(1);

      } catch (SQLException ex) {
         throw new RuntimeException("Erro ao verificar existência de usuário por email no banco de dados.", ex);
      }
   }

   @Override
   @Transactional(readOnly = true)
   public boolean existsById(UUID id) {
      String sql = "{? = call ufn_UsuarioExistePorId(?)}";

      try (Connection conn = dataSource.getConnection();
            CallableStatement cs = conn.prepareCall(sql)) {

         cs.registerOutParameter(1, Types.BIT);

         cs.setObject(2, id);

         cs.execute();

         return cs.getBoolean(1);
      } catch (SQLException ex) {
         throw new RuntimeException("Erro ao verificar existência de usuário por ID no banco de dados.", ex);
      }
   }

   @Override
   @Transactional(readOnly = true)
   public List<Usuario> findAll() {
      String sql = "SELECT * FROM ufn_BuscarTodosUsuarios()";

      try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

         List<Usuario> usuarios = new ArrayList<>();

         try (ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
               Usuario usuario = new Usuario();
               usuario.setId(rs.getObject("id", UUID.class));
               usuario.setNome(rs.getString("nome"));
               usuario.setEmail(rs.getString("email"));
               usuarios.add(usuario);
            }
         }

         return usuarios;
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao buscar todos os usuários no banco de dados.", e);
      }
   }

   @Override
   @Transactional(readOnly = true)
   public Page<Usuario> findAll(Pageable pageable) {
      int pagina = pageable.getPageNumber();
      int tamanho = pageable.getPageSize();

      String sql = "SELECT * FROM ufn_BuscarTodosUsuariosComPaginacao(?, ?)";

      try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

         ps.setInt(1, pagina);
         ps.setInt(2, tamanho);

         List<Usuario> usuarios = new ArrayList<>();

         try (ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
               Usuario usuario = new Usuario();
               usuario.setId(rs.getObject("id", UUID.class));
               usuario.setNome(rs.getString("nome"));
               usuario.setEmail(rs.getString("email"));
               usuarios.add(usuario);
            }
         }

         long totalElementos = countUsuarios();

         return new PageImpl<>(usuarios, pageable, totalElementos);
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao buscar todos os usuários no banco de dados.", e);
      }
   }

   // Método auxiliar para contar o total de usuários.
   public long countUsuarios() {
      String sql = "SELECT count(*) FROM usuario";
      try (Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
               return rs.getLong(1);
            }
         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao contar usuários no banco de dados.", e);
      }
      return 0;
   }

}
