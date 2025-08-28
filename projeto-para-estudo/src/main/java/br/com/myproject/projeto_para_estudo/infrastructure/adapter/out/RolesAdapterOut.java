package br.com.myproject.projeto_para_estudo.infrastructure.adapter.out;

import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.myproject.projeto_para_estudo.core.entity.Roles;
import br.com.myproject.projeto_para_estudo.core.port.out.RolesPortOut;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RolesAdapterOut implements RolesPortOut {
  private final DataSource dataSource;
  private final JdbcTemplate jdbcTemplate;

    @Override
    public Roles save(Roles role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Roles> findById(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Roles> findByNome(String nome) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Roles role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addRoleToUser(UUID userId, UUID roleId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeRoleFromUser(UUID userId, UUID roleId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
