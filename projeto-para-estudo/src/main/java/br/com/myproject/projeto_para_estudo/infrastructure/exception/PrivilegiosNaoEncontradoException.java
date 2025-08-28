package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PrivilegiosNaoEncontradoException extends RuntimeException {
  public PrivilegiosNaoEncontradoException(String message) {
    super(message);
  }

}
