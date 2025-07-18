package ui.audio;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import ui.controller.UIGameController;
import ui.game.UIEndings;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TypingEffect {

    public static boolean isSkipped = false;

    public static boolean isWaiting = true;

    public static void typeWithSound(TextBox textBox, String text, WindowBasedTextGUI gui, String soundPath) {

        isSkipped = false;

        int delayMillis = 55;

        if (soundPath == null || soundPath.isEmpty()) {
            soundPath = "/sounds/Terminal.wav";
        }

        String finalSoundPath = soundPath;

        new Thread(() -> {
            UIGameController.getCurrent().disableActionPanel();

            StringBuilder currentText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (isSkipped) {
                    SoundPlayer.stopSound();
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
            });

        }).start();
    }

    public static void typeWithBanner(TextBox textBox, String text, WindowBasedTextGUI gui, String soundPath, boolean sound, boolean first, Runnable onComplete) {

        isSkipped = false;
        isWaiting = true;

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

                    while (TypingEffect.isWaiting) {
                        Thread.sleep(100);
                    }

                } else {
                    gui.getGUIThread().invokeAndWait(() -> {
                        UIGameController.getCurrent().disableActionPanel();
                    });
                }

                StringBuilder currentText = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    if (isSkipped) {
                        gui.getGUIThread().invokeLater(() -> textBox.setText(text));
                        SoundPlayer.stopSound();
                        while (isWaiting) {
                            textBox.setText(text);
                        }
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

                        Thread.sleep(delayMillis);
                    }
                }


                if (!first && onComplete != null) {
                    gui.getGUIThread().invokeAndWait(onComplete::run);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!UIEndings.enteredEndings) {
                    gui.getGUIThread().invokeLater(() -> {
                        UIGameController.getCurrent().enableActionPanel();
                    });
                }
            }
        }).start();
    }

    public static void typeText(TextBox output, String text, WindowBasedTextGUI gui, int delay) {
        for (char c : text.toCharArray()) {
            output.setText(output.getText() + c);
            try {
                Thread.sleep(delay);
                gui.updateScreen();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
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
        isSkipped = true;
    }

    public static void stopWaiting() {
        isWaiting = false;
    }

}
