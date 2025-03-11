package dev.animals.exception.handler;

import dev.animals.exception.ExceptionResponse;
import dev.animals.exception.LogicException;
import dev.animals.exception.helper.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

  @ExceptionHandler(LogicException.class)
  public ResponseEntity<ExceptionResponse> handleLogicException(LogicException e, HttpServletRequest request) {
    log(e);
    return ResponseEntity
      .status(ObjectUtils.defaultIfNull(determineStatus(e), HttpStatus.INTERNAL_SERVER_ERROR))
      .contentType(MediaType.APPLICATION_JSON)
      .body(new ExceptionResponse(e.getCode(),
        Objects.isNull(e.getMessage()) ? "No message available" : e.getMessage(),
        request.getRequestURI()));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) throws MethodArgumentNotValidException {
    throw ex;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleOtherException(Exception e, HttpServletRequest request) {
    log(e);
    return ResponseEntity
      .status(ObjectUtils.defaultIfNull(determineStatus(e), HttpStatus.INTERNAL_SERVER_ERROR))
      .contentType(MediaType.APPLICATION_JSON)
      .body(new ExceptionResponse(CommonErrorCode.JAVA_ERROR,
        Objects.isNull(e.getMessage()) ? "No message available" : e.getMessage(),
        request.getRequestURI()));
  }

  protected HttpStatus determineStatus(Throwable ex) {
    ResponseStatus ann = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
    if (ann != null) {
      return ann.code();
    }

    if (ex instanceof LogicException)
      return ((LogicException) ex).getCode().getStatus();
    return null;
  }

  protected void log(Exception e) {
    log.error("{} message: {} stacktrace: {}", e, e.getMessage(), StringUtils.join(e.getStackTrace(), "\n"));
  }
}
