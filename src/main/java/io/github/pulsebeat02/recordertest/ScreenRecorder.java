package io.github.pulsebeat02.recordertest;

import io.github.pulsebeat02.recordertest.gui.VideoFrame;
import java.awt.AWTException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ScreenRecorder {

  public static void main(final String @Nullable [] args) throws AWTException {
    new ScreenRecorder(new String[] {"discord", "30"});
  }

  private final String[] args;

  private ScreenRecorder(@NotNull final String[] args) throws AWTException {
    this.args = args;
    this.checkPreconditions();
    this.start();
  }

  private void checkPreconditions() {
    if (this.args.length < 2) {
      throw new IllegalArgumentException(
          "Must have at least two arguments for application! First one must be process name, second must be FPS!");
    }
  }

  private void start() throws AWTException {
    new VideoFrame(this.args[0], Integer.parseInt(this.args[1])).run();
  }
}
