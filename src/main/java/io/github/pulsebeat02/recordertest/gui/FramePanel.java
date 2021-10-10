package io.github.pulsebeat02.recordertest.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class FramePanel extends JPanel {

  @Serial private static final long serialVersionUID = -4627460988960565968L;

  private final JFrame frame;

  public FramePanel(@NotNull final VideoFrame frame) {
    this.frame = frame.getFrame();
    this.setSize(this.frame.getSize());
    this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.setBackground(Color.LIGHT_GRAY);
    this.setVisible(true);
  }

  public void consume(@NotNull final BufferedImage image) {
    this.frame.getContentPane().removeAll();
    this.frame.add(new JLabel("", new ImageIcon(image), JLabel.CENTER));
    this.frame.repaint();
    this.frame.revalidate();
  }
}
