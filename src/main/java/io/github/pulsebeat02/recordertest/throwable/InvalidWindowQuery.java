package io.github.pulsebeat02.recordertest.throwable;

import java.io.Serial;
import org.jetbrains.annotations.NotNull;

public final class InvalidWindowQuery extends RecorderException {

  @Serial private static final long serialVersionUID = -1806608989377760192L;

  public InvalidWindowQuery(@NotNull final String message) {
    super("Invalid Window %s".formatted(message));
  }
}
