package io.github.pulsebeat02.recordertest.recorder;

import io.github.pulsebeat02.recordertest.ApplicationConstants;
import io.github.pulsebeat02.recordertest.throwable.InvalidWindowQuery;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WindowRecorder {

  private final String search;
  private final Robot robot;
  private final AtomicBoolean running;
  private final Function<Rectangle, BufferedImage> function;
  private CompletableFuture<Void> process;

  public WindowRecorder(@NotNull final String query) throws AWTException {
    this.search = query.toLowerCase(Locale.ROOT);
    this.robot = new Robot();
    this.running = new AtomicBoolean(false);
    this.function =
        ApplicationConstants.OPERATING_SYSTEM_VERSION.contains("Sur")
            ? this::bigSurCapture0
            : this::basicCapture0;
  }

  public void record(@NotNull final Consumer<BufferedImage> consumer, final long delay) {
    this.setRunning(true);
    this.process =
        CompletableFuture.runAsync(
            () -> {
              while (this.running.get()) {
                consumer.accept(this.function.apply(RecorderUtils.getAllWindows().stream()
                    .filter(window -> window.getTitle().toLowerCase(Locale.ROOT).contains(
                        this.search))
                    .findAny().orElseThrow(() -> new InvalidWindowQuery(this.search)).getLocAndSize()));
                try {
                  Thread.sleep(delay);
                } catch (final InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
  }

  private @NotNull BufferedImage bigSurCapture0(@NotNull final Rectangle rectangle) {
    this.robot.keyPress(KeyEvent.VK_META);
    this.robot.keyPress(KeyEvent.VK_SHIFT);
    this.robot.keyPress(KeyEvent.VK_3);
    this.robot.keyRelease(KeyEvent.VK_3);
    this.robot.keyRelease(KeyEvent.VK_SHIFT);
    this.robot.keyRelease(KeyEvent.VK_META);
    return this.basicCapture0(rectangle);
  }

  private @NotNull BufferedImage basicCapture0(@NotNull final Rectangle rectangle) {
    return this.robot.createScreenCapture(rectangle);
  }

  public @NotNull Function<Rectangle, BufferedImage> getFunction() {
    return this.function;
  }

  public @Nullable CompletableFuture<Void> getProcess() {
    return this.process;
  }

  public boolean isRunning() {
    return this.running.get();
  }

  public void setRunning(final boolean status) {
    this.running.set(status);
  }

}
