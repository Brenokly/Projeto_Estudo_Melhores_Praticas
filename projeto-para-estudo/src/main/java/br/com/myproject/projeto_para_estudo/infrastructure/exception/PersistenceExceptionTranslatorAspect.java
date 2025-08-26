package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PersistenceExceptionTranslatorAspect {

  private final SqlStateErrorMapping errorMapping;

  public PersistenceExceptionTranslatorAspect(SqlStateErrorMapping errorMapping) {
    this.errorMapping = errorMapping;
  }

  @AfterThrowing(pointcut = "execution(* br.com.myproject.projeto_para_estudo.infrastructure.adapter.out.*.*(..))", throwing = "ex")
  public void translateException(Throwable ex) {
    Throwable root = findRootCause(ex);
    String message = root.getMessage();

    errorMapping.getExceptionForMessage(message)
        .ifPresent(businessException -> {
          throw businessException;
        });
  }

  private Throwable findRootCause(Throwable throwable) {
    Throwable root = throwable;
    while (root.getCause() != null && root.getCause() != root) {
      root = root.getCause();
    }
    return root;
  }
}
