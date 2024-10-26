package dev.animals.exception.helper;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String getKey();

  HttpStatus getStatus();
}
