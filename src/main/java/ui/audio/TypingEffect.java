package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class TypingEffect {

    public static void typeWithSound(TextBox textBox, String text, WindowBasedTextGUI gui) {
        int delayMillis = 40;

        new Thread(() -> {
            StringBuilder currentText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                currentText.append(c);
                gui.getGUIThread().invokeLater(() -> textBox.setText(currentText.toString()));
                if (i % 2 == 0 && (Character.isLetterOrDigit(c) || Character.isWhitespace(c))) {
                    playSound("/sounds/Terminal.wav");
                }

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private static void playSound(String resourcePath) {
        try (InputStream audioSrc = TypingEffect.class.getResourceAsStream(resourcePath);
             InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc)) {

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
