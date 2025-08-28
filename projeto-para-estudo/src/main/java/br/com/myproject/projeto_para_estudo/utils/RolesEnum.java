package br.com.myproject.projeto_para_estudo.utils;

public enum RolesEnum {
  ROLE_ADMIN("ROLE_ADMIN"),
  ROLE_USER("ROLE_USER");

  private final String role;

  RolesEnum(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public static RolesEnum fromRoleString(String role) {
    for (RolesEnum r : RolesEnum.values()) {
      if (r.getRole().equals(role)) {
        return r;
      }
    }
    throw new IllegalArgumentException("Papel desconhecido: " + role);
  }

}
