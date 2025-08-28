package br.com.myproject.projeto_para_estudo.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.myproject.projeto_para_estudo.core.entity.Roles;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.privilegios.PrivilegiosResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.roles.OnlyRolesResponse;
import br.com.myproject.projeto_para_estudo.infrastructure.dto.roles.RolesResponse;

@Component
public class RolesMapper {
  public RolesResponse toResponse(Roles rol) {
    if (rol == null) {
      return null;
    }

    RolesResponse response = new RolesResponse(rol.getId(), rol.getNome(), rol.getPrivilegios()
        .stream().map(priv -> new PrivilegiosResponse(priv.getId(), priv.getNome())).toList());
    return response;
  }

  public OnlyRolesResponse toResponseOnly(Roles rol) {
    if (rol == null) {
      return null;
    }

    return new OnlyRolesResponse(rol.getId(), rol.getNome());
  }

}
