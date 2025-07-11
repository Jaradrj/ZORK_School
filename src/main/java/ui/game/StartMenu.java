package ui.game;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.TextColor;
import lombok.Getter;
import ui.components.ButtonStyling;

import java.io.IOException;

@Getter
public class StartMenu {

    private Screen screen;
    private MultiWindowTextGUI gui;

    public StartMenu() throws IOException {
        this.screen = new DefaultTerminalFactory().createScreen();
        this.screen.startScreen();
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK_BRIGHT));
    }

    public void showStartMenu() {
        final BasicWindow window = new BasicWindow("MindScale");

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

        Button startButton = new Button("Start Game", window::close);
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
        buttonPanel.addComponent(new EmptySpace(new TerminalSize(0,1)));
        buttonPanel.addComponent(exitButton);

        panel.addComponent(buttonPanel.setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        window.setComponent(panel);
        window.setHints(java.util.Collections.singletonList(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
    }

}
