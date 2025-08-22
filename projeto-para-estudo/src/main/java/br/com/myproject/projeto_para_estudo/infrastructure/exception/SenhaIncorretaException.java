package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SenhaIncorretaException extends RuntimeException {
  public SenhaIncorretaException(String message) {
    super(message);
  }
}
