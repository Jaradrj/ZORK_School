package ui.game;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameScreen {

    private static Screen screen;

    public static Screen getScreen() throws IOException {
        if (screen == null) {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            if (terminal instanceof SwingTerminalFrame swingFrame) {
                swingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                swingFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
            }
        }
        return screen;
    }
}

