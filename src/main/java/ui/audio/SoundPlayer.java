package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SoundPlayer {

    private static final List<Clip> activeClips = Collections.synchronizedList(new ArrayList<>());
    private static final List<Thread> activeThreads = Collections.synchronizedList(new ArrayList<>());
    private static Clip currentClip;

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

                currentClip = AudioSystem.getClip();
                currentClip.open(audioStream);
                currentClip.start();

                activeClips.add(currentClip);

                if (delayAfter > 0) Thread.sleep(delayAfter);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread soundThread;
        if (block) {
            soundThread = Thread.currentThread();
            activeThreads.add(soundThread);
            playLogic.run();
        } else {
            soundThread = new Thread(playLogic);
            soundThread.start();
            activeThreads.add(soundThread);
        }
    }

    public static void stopSound() {
        synchronized (activeClips) {
            for (Clip clip : activeClips) {
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                    clip.close();
                }
            }
            activeClips.clear();
        }

        synchronized (activeThreads) {
            for (Thread thread : activeThreads) {
                if (thread != null && thread.isAlive()) {
                    thread.interrupt();
                }
            }
            activeThreads.clear();
        }
    }
}
