package io.github.pulsebeat02.recordertest;

import java.util.Locale;

public final class ApplicationConstants {

  private ApplicationConstants() {}

  public static final String USER_DIR;
  public static final String OPERATING_SYSTEM_VERSION;
  public static final OS OPERATING_SYSTEM;

  static {
    USER_DIR = System.getProperty("user.dir");
    OPERATING_SYSTEM_VERSION = System.getProperty("os.version").toLowerCase(Locale.ROOT);
    final String osName = System.getProperty("os.name").toLowerCase();
    OPERATING_SYSTEM =
        osName.contains("win") ? OS.WINDOWS : osName.contains("mac") ? OS.MAC : OS.LINUX;
  }
}
