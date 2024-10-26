package dev.animals.exception;

import dev.animals.exception.helper.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExceptionResponse {
  private final ErrorCode type;
  private final String error;
  private final String error_description;
  private final String message;
  private final String path;

  public ExceptionResponse(ErrorCode type, String message) {
    this(type, type.toString(), message, message, null);
  }

  public ExceptionResponse(ErrorCode type, String message, String path) {
    this(type, type.toString(), message, message, path);
  }
}