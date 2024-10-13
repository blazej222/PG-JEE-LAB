package org.example.controller.servlet.exception;

public class AlreadyExistsException extends HttpRequestException {

  private static final int RESPONSE_CODE = 409;

  public AlreadyExistsException(String message) {
    super(RESPONSE_CODE);
  }

  public AlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace, RESPONSE_CODE);
  }

  public AlreadyExistsException(String message, Throwable cause) {
    super(message, cause, RESPONSE_CODE);
  }

  public AlreadyExistsException(Throwable cause) {
    super(cause, RESPONSE_CODE);
  }

}
