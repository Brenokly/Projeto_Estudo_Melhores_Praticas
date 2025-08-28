package br.com.myproject.projeto_para_estudo.infrastructure.adapter.out;

import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.myproject.projeto_para_estudo.core.entity.Privilegios;
import br.com.myproject.projeto_para_estudo.core.port.out.PrivilegiosPortOut;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PrivilegiosAdapterOut implements PrivilegiosPortOut {
  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;

  @Override
  public Privilegios save(Privilegios privilegio) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public Optional<Privilegios> findById(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public Optional<Privilegios> findByNome(String nome) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByNome'");
  }

  @Override
  public void deleteById(UUID id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
  }

  @Override
  public void delete(Privilegios privilegio) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public void addPrivilegioToRole(UUID roleId, UUID privilegioId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addPrivilegioToRole'");
  }

  @Override
  public void removePrivilegioFromRole(UUID roleId, UUID privilegioId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'removePrivilegioFromRole'");
  }
}
