package io.github.pulsebeat02.recordertest.gui;

import io.github.pulsebeat02.recordertest.recorder.WindowRecorder;
import java.awt.AWTException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;
import javax.swing.JFrame;
import org.jetbrains.annotations.NotNull;

public final class VideoFrame {

  private final WindowRecorder recorder;
  private final Consumer<BufferedImage> consumer;
  private final JFrame frame;
  private final long delay;

  public VideoFrame(@NotNull final String query, final long fps) throws AWTException {
    this.delay = 1000L / fps;
    this.recorder = new WindowRecorder(query);
    this.frame = new JFrame("Java Application Recorder Demo");
    this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final FramePanel panel = new FramePanel(this);
    this.frame.add(panel);
    this.consumer = panel::consume;
    this.frame.setVisible(true);
  }

  public void run() {
    this.recorder.record(this.consumer, this.delay);
  }

  public @NotNull JFrame getFrame() {
    return this.frame;
  }

  public long getDelay() {
    return this.delay;
  }

  public @NotNull WindowRecorder getRecorder() {
    return this.recorder;
  }

  public @NotNull Consumer<BufferedImage> getConsumer() {
    return this.consumer;
  }
}
