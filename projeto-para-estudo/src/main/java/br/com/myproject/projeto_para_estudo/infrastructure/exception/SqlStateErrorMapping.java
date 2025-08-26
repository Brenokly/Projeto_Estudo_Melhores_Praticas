package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class SqlStateErrorMapping {

  private final Map<String, Function<String, ? extends RuntimeException>> errorMap = new HashMap<>();

  public SqlStateErrorMapping() {
    errorMap.put("O e-mail informado já está cadastrado no sistema.", EmailDuplicadoException::new);
    errorMap.put("Usuário não encontrado.", UsuarioNaoEncontradoException::new);
    errorMap.put("A nova senha e a confirmação da senha não coincidem.", SenhaNaoCoincidemException::new);
    errorMap.put("A senha atual está incorreta.", SenhaIncorretaException::new);
    errorMap.put("Tarefa não encontrada.", TarefaNaoEncontradaException::new);
  }

  public Optional<? extends RuntimeException> getExceptionForMessage(String message) {
    return errorMap.entrySet().stream()
        .filter(e -> message != null && message.contains(e.getKey()))
        .findFirst()
        .map(e -> e.getValue().apply(message));
  }
}
