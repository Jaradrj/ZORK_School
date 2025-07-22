package ui.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import console.game.Player;
import lombok.Getter;
import ui.UIMain;
import ui.audio.SoundPlayer;
import ui.components.ButtonStyling;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;

@Getter
public class StartMenu {

    private Screen screen;
    private MultiWindowTextGUI gui;
    private final Player player;

    public StartMenu(Player player) throws IOException {

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
        screen.startScreen();
        if (terminal instanceof SwingTerminalFrame swingFrame) {
            swingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            swingFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK_BRIGHT));
        this.player = player;
    }

    public void showStartMenu() {
        final BasicWindow window = new BasicWindow("Start Menu");
        SoundPlayer.playSound("/sounds/Soundtrack.wav", 0, 0, null, gui, false);
        Panel panel = new Panel();
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("░▒▓██████████████▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓███████▓▒░ ░▒▓███████▓▒░░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░      ░▒▓████████▓▒░ \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░      ░▒▓████████▓▒░▒▓█▓▒░      ░▒▓██████▓▒░   \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓███████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓████████▓▒░ \n" +
                "                                                                                                                       \n" +
                "                                                                                                                       \n" +
                "\n"));
        panel.addComponent(new EmptySpace(TerminalSize.ONE)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Fill)));

        Panel buttonPanel = new Panel();
        buttonPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        Button startButton = new Button("Start Game", () -> {
            window.close();
            String name = enterName();
            player.setName(name);
            if(!player.hasFlag("second_try")) {
                UIMain.playerName = name;
            }
        });
        startButton.setRenderer(new ButtonStyling());

        Button exitButton = new Button("Exit", () -> {
            try {
                screen.stopScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        });
        exitButton.setRenderer(new ButtonStyling());

        startButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        exitButton.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        buttonPanel.addComponent(startButton);
        buttonPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        buttonPanel.addComponent(exitButton);

        panel.addComponent(buttonPanel.setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        window.setComponent(panel);
        window.setHints(Collections.singletonList(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
    }

    public String enterName() {
        final BasicWindow window = new BasicWindow("Enter Name");
        Panel panel = new Panel();

        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        panel.addComponent(new Label("Please enter you name: "));
        TextBox nameBox = new TextBox().setPreferredSize(new TerminalSize(20, 1));
        panel.addComponent(nameBox);

        Label errorLabel = new Label("");
        errorLabel.setForegroundColor(TextColor.ANSI.RED);
        panel.addComponent(errorLabel);

        String[] name = new String[1];
        Button ok = new Button("OK", () -> {
            String input = nameBox.getText().trim();
            if (input.isEmpty()) {
                errorLabel.setText("Name cannot be empty");
            } else {
                name[0] = input;
                window.close();
                try {
                    screen.stopScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            SoundPlayer.stopSound();
        });

        ok.setRenderer(new ButtonStyling());
        ok.setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        panel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        TextColor fg = TextColor.ANSI.WHITE;
        TextColor bg = TextColor.ANSI.BLACK;
        nameBox.setTheme(new SimpleTheme(fg, bg));
        panel.addComponent(ok);

        window.setComponent(panel);
        window.setHints(Collections.singletonList(Window.Hint.CENTERED));

        gui.addWindowAndWait(window);

        return name[0];
    }
}
