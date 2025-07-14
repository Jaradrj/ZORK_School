package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {
    public static void playSound(String path, int delayBefore, int delayAfter, TextBox outputArea, WindowBasedTextGUI gui, boolean block) {
        Runnable playLogic = () -> {
            try {
                if (outputArea != null && gui != null) {
                    outputArea.invalidate();
                    gui.updateScreen();
                }

                if (delayBefore > 0) Thread.sleep(delayBefore);

                InputStream audioSrc = SoundPlayer.class.getResourceAsStream(path);
                if (audioSrc == null) {
                    System.err.println("Sound not found: " + path);
                    return;
                }

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                if (delayAfter > 0) Thread.sleep(delayAfter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (block) {
            playLogic.run();
        } else {
            new Thread(playLogic).start();
        }
    }
}
