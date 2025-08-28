package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Privilegios;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.privilegios.PrivilegiosResponse;

@Component
public class PrivilegiosMapper {

  public PrivilegiosResponse toResponse(Privilegios priv) {
    if (priv == null) {
      return null;
    }

    PrivilegiosResponse response = new PrivilegiosResponse(priv.getId(), priv.getNome());
    return response;
  }

}
