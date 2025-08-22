package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class SqlStateErrorMapping {

  private final Map<Integer, Function<String, ? extends RuntimeException>> errorMap = new HashMap<>();

  public SqlStateErrorMapping() {
    // Erros de Usuario
    errorMap.put(50001, EmailDuplicadoException::new);
    errorMap.put(50002, UsuarioNaoEncontradoException::new);
    errorMap.put(50003, SenhaNaoCoincidemException::new);
    errorMap.put(50004, SenhaIncorretaException::new);

    // Erros de Tarefa
    errorMap.put(50005, TarefaNaoEncontradaException::new);
  }

  public Optional<? extends RuntimeException> getExceptionFor(Integer sqlState, String message) {
    if (errorMap.containsKey(sqlState)) {
      return Optional.of(errorMap.get(sqlState).apply(message));
    }
    return Optional.empty();
  }
}