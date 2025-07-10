package ui.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class TypingEffect {

    private static Clip clickSound;

    public static void typeWithSound(String text, int delayMillis) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                playSound();
            }
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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
