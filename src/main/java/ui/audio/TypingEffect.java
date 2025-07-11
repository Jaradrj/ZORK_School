package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class TypingEffect {

    private static Clip clickSound;

    public static void typeWithSound(TextBox textBox, String text, WindowBasedTextGUI gui) {
        int delayMillis = 40;
        new Thread(() -> {
            StringBuilder currentText = new StringBuilder();
            for (char c : text.toCharArray()) {
                currentText.append(c);
                char finalC = c;
                gui.getGUIThread().invokeLater(() -> textBox.setText(currentText.toString()));

                if (Character.isLetterOrDigit(finalC) || Character.isWhitespace(finalC)) {
                    playSound();
                }

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public static void loadSound(String resourcePath) {
        try (InputStream audioSrc = TypingEffect.class.getResourceAsStream(resourcePath);
             InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc)) {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clickSound = AudioSystem.getClip();
            clickSound.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error loading sound: " + e.getMessage());
        }
    }

    public static void playSound() {
        if (clickSound != null) {
            if (clickSound.isRunning()) {
                clickSound.stop();
            }
            clickSound.setFramePosition(0);
            clickSound.start();
        }
    }
}
