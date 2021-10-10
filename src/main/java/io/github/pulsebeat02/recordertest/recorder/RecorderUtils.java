package io.github.pulsebeat02.recordertest.recorder;

import com.sun.jna.platform.DesktopWindow;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import oshi.driver.mac.WindowInfo;

public final class RecorderUtils {

  private RecorderUtils() {}

  public static @NotNull List<DesktopWindow> getAllWindows() {
    return WindowInfo.queryDesktopWindows(true).stream()
        .map(
            window ->
                new DesktopWindow(
                    null, window.getTitle(), window.getCommand(), window.getLocAndSize()))
        .collect(Collectors.toList());
  }
}
