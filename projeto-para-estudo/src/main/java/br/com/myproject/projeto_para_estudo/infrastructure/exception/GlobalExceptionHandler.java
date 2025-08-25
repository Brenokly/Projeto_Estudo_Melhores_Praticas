package br.com.myproject.projeto_para_estudo.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.myproject.projeto_para_estudo.infrastructure.dto.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Manipulador para os erros de validação do @Valid nos DTOs
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String errors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    ErrorResponseDTO error = new ErrorResponseDTO(errors, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  // Manipulador genérico para qualquer outra exceção não tratada
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex) {
    ErrorResponseDTO error = new ErrorResponseDTO("Ocorreu um erro interno no servidor.",
        HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Manipulador para credenciais inválidas durante o login
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO("Usuário ou senha inválidos.", HttpStatus.UNAUTHORIZED.value(),
        LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  // Manipulador para violações de integridade de dados
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    String message = "Operação não pode ser concluída devido a uma violação de integridade de dados. "
        + "O recurso pode estar sendo referenciado por outros registros.";
    ErrorResponseDTO error = new ErrorResponseDTO(message, HttpStatus.CONFLICT.value(), LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  // Manipulador para acesso negado
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value(),
        LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  // Manipulador para estados ilegais/erros internos no servidor
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalStateException(IllegalStateException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
        "Ocorreu um erro interno crítico no servidor.",
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        LocalDateTime.now());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}