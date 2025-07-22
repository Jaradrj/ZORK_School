package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import console.game.Player;
import ui.controller.UIGameController;
import ui.game.UIEndings;
import ui.game.UIRoomFactory;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class TypingEffect {

    public static final AtomicBoolean isSkipped = new AtomicBoolean(false);
    public static final AtomicBoolean isWaiting = new AtomicBoolean(true);

    public static final Object waitLock = new Object();

    public static void typeWithSound(TextBox textBox, String text, WindowBasedTextGUI gui, String soundPath) {

        UIGameController.setShowSkipHint(true);

        isSkipped.set(false);

        int delayMillis = 55;

        if (soundPath == null || soundPath.isEmpty()) {
            soundPath = "/sounds/Terminal.wav";
        }

        String finalSoundPath = soundPath;

        new Thread(() -> {
            UIGameController.getCurrent().disableActionPanel();

            StringBuilder currentText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (isSkipped.get()) {
                    gui.getGUIThread().invokeLater(() -> textBox.setText(text));
                    break;
                }
                char c = text.charAt(i);
                currentText.append(c);
                gui.getGUIThread().invokeLater(() -> textBox.setText(currentText.toString()));
                if (i % 2 == 0 && (Character.isLetterOrDigit(c) || Character.isWhitespace(c))) {
                    playSound(finalSoundPath);
                }

                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            gui.getGUIThread().invokeLater(() -> {
                UIGameController.getCurrent().enableActionPanel();
                UIGameController.setShowSkipHint(false);
            });

        }).start();
    }

    public static void typeWithBanner(TextBox textBox, String text, WindowBasedTextGUI gui, String soundPath, boolean sound, boolean first, Runnable onComplete) {

        isSkipped.set(false);
        isWaiting.set(true);

        int delayMillis;

        if (soundPath == null || soundPath.isEmpty()) {
            soundPath = "/sounds/Terminal.wav";
        }

        if (sound) {
            delayMillis = 55;
        } else if (soundPath.equalsIgnoreCase("TeacherEnding.wav")) {
            delayMillis = 65;
        } else if (soundPath.equalsIgnoreCase("BadEnding.wav")) {
            delayMillis = 60;
        } else {
            delayMillis = 55;
        }

        String finalSoundPath = soundPath;

        new Thread(() -> {
            try {
                if (first && onComplete != null) {
                    UIGameController.setShowEnterHint(true);
                    try {
                        gui.getGUIThread().invokeAndWait(() -> {
                            UIGameController.getCurrent().disableActionPanel();
                            onComplete.run();
                            UIEndings.waitingForEnter(() -> {
                                try {
                                    gui.updateScreen();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    synchronized (TypingEffect.waitLock) {
                        while (isWaiting.get()) {
                            try {
                                TypingEffect.waitLock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }

                    isWaiting.set(true);
                    isSkipped.set(false);

                } else {
                    gui.getGUIThread().invokeAndWait(() -> {
                        UIGameController.getCurrent().disableActionPanel();
                    });
                }

                UIGameController.setShowSkipHint(true);
                StringBuilder currentText = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    if (isSkipped.get()) {
                        isWaiting.set(true);

                        gui.getGUIThread().invokeLater(() -> textBox.setText(text));

                        UIGameController.setShowEnterHint(true);

                        synchronized (TypingEffect.waitLock) {
                            while (isWaiting.get()) {
                                try {
                                    TypingEffect.waitLock.wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        }

                        break;
                    } else {
                        char c = text.charAt(i);
                        currentText.append(c);

                        gui.getGUIThread().invokeAndWait(() -> {
                            textBox.setText(currentText.toString());
                            try {
                                gui.updateScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        if (sound && i % 2 == 0 &&
                                (Character.isLetterOrDigit(c) || Character.isWhitespace(c))) {
                            playSound(finalSoundPath);
                        }

                        synchronized (TypingEffect.waitLock) {
                            TypingEffect.waitLock.wait(delayMillis);
                        }
                    }
                }

                UIGameController.setShowSkipHint(false);


                if (!first && onComplete != null) {
                    gui.getGUIThread().invokeAndWait(onComplete::run);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!UIEndings.enteredEndings) {
                    gui.getGUIThread().invokeLater(() -> {
                        UIGameController.getCurrent().enableActionPanel();
                        UIGameController.setShowEnterHint(false);
                    });
                }
            }
        }).start();
    }

    private static void playSound(String resourcePath) {
        new Thread(() -> {
            try {
                InputStream audioSrc = TypingEffect.class.getResourceAsStream(resourcePath);
                if (audioSrc == null) {
                    System.err.println("Sound not found: " + resourcePath);
                    return;
                }
                BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try {
                            audioStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }

    public static void skipTyping() {
        UIGameController.setShowSkipHint(false);
        isSkipped.set(true);
        isWaiting.set(true);
        SoundPlayer.stopSound();
        synchronized (waitLock) {
            waitLock.notifyAll();
        }
    }

    public static void stopWaiting() {
        UIGameController.setShowEnterHint(false);
        isWaiting.set(false);
        SoundPlayer.stopSound();
        synchronized (waitLock) {
            waitLock.notifyAll();
        }
    }

}
