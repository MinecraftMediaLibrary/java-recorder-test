package io.github.pulsebeat02.recordertest.throwable;

import java.io.Serial;
import org.jetbrains.annotations.NotNull;

public class RecorderException extends AssertionError {

  @Serial private static final long serialVersionUID = 8826026664927464017L;

  public RecorderException(@NotNull final String message) {
    super(message);
  }

  @Override
  public synchronized Throwable getCause() {
    return this;
  }

  @Override
  public synchronized Throwable initCause(@NotNull final Throwable cause) {
    return this;
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
