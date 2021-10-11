package io.github.pulsebeat02.recordertest.recorder;

import com.sun.jna.Native;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.WindowByReference;
import com.sun.jna.platform.unix.X11.XTextProperty;
import com.sun.jna.platform.unix.X11.XWindowAttributes;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import io.github.pulsebeat02.recordertest.ApplicationConstants;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import oshi.driver.mac.WindowInfo;

public final class RecorderUtils {

  private RecorderUtils() {
  }

  public static @NotNull List<DesktopWindow> getAllWindows() {
    switch (ApplicationConstants.OPERATING_SYSTEM) {
      case MAC -> {
        return getWindowsMac();
      }
      case WINDOWS -> {
        return getWindowsWindows();
      }
      case LINUX -> {
        return getWindowsLinux();
      }
    }
    return List.of();
  }

  private static @NotNull List<DesktopWindow> getWindowsMac() {
    return WindowInfo.queryDesktopWindows(true).stream()
        .map(
            window ->
                new DesktopWindow(
                    null, window.getTitle(), window.getCommand(), window.getLocAndSize()))
        .collect(Collectors.toList());
  }

  private static @NotNull List<DesktopWindow> getWindowsWindows() {
    return WindowUtils.getAllWindows(true);
  }

  private static @NotNull List<DesktopWindow> getWindowsLinux() {
    final X11 x11 = X11.INSTANCE;
    final Display display = x11.XOpenDisplay(null);
    final List<DesktopWindow> windows = new ArrayList<>();
    final LinkedList<Window> list = new LinkedList<>();
    list.add(x11.XDefaultRootWindow(display));
    while (!list.isEmpty()) {
      final Window root = list.poll();
      final WindowByReference windowRef = new X11.WindowByReference();
      final WindowByReference parentRef = new X11.WindowByReference();
      final PointerByReference childrenRef = new PointerByReference();
      final IntByReference childCountRef = new IntByReference();
      x11.XQueryTree(display, root, windowRef, parentRef, childrenRef, childCountRef);
      if (childrenRef.getValue() == null) {
        return List.of();
      }
      final long[] ids = getLinuxWindowIDs(childrenRef, childCountRef);
      for (final long id : ids) {
        final Window window = new Window(id);
        final XTextProperty name = new XTextProperty();
        final XWindowAttributes attributes = new XWindowAttributes();
        x11.XGetWMName(display, window, name);
        x11.XGetWindowAttributes(display, window, attributes);
        windows.add(new DesktopWindow(null, name.value, "Unknnown (Linux)",
            new Rectangle(attributes.x, attributes.y, attributes.width, attributes.height)));
        x11.XFree(name.getPointer());
        list.add(window);
      }
    }
    return windows;
  }

  private static long @NotNull [] getLinuxWindowIDs(@NotNull final PointerByReference childrenRef,
      @NotNull final IntByReference childCountRef) {
    long[] ids = null;
    switch (Native.LONG_SIZE) {
      case Long.BYTES -> ids = childrenRef.getValue().getLongArray(0, childCountRef.getValue());
      case Integer.BYTES -> {
        final int[] intIds = childrenRef.getValue().getIntArray(0, childCountRef.getValue());
        ids = new long[intIds.length];
        for (int i = 0; i < intIds.length; i++) {
          ids[i] = intIds[i];
        }
      }
    }
    if (ids == null) {
      ids = new long[0];
    } else {
      ids = Arrays.stream(ids).filter(id -> id != 0).toArray();
    }
    return ids;
  }
}
