package dev.animals.exception.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  JAVA_ERROR("common.java.error", HttpStatus.INTERNAL_SERVER_ERROR),
  COMMON_OBJECT_NOT_EXISTS("common.object.not.exists", HttpStatus.NOT_FOUND),
  ILLEGAL_SAVING("common.illegal.saving", HttpStatus.NOT_ACCEPTABLE),
  AUTH_FAIL("incorrect.auth_header_passing", HttpStatus.FORBIDDEN),
  VALIDATION_ERROR("validation.error", HttpStatus.BAD_REQUEST),
  SERVICE_UNAVAILABLE("service.unavailable", HttpStatus.SERVICE_UNAVAILABLE),
  ILLEGAL_ALGORITHM("illegal.algorithm", HttpStatus.NOT_ACCEPTABLE),
  INCORRECT_CLIENT("illegal.client", HttpStatus.NOT_ACCEPTABLE),
  ILLEGAL_PROCESSOR_CALL("illegal.processor.call", HttpStatus.INTERNAL_SERVER_ERROR),
  RUNTIME_SERVER("failed.runtime.server", HttpStatus.INTERNAL_SERVER_ERROR),
  UNAUTHORIZED("unauthorized", HttpStatus.UNAUTHORIZED);

  private final String key;
  private final HttpStatus status;

  public String toString() {
    return this.key;
  }
}
