package ui.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.TextColor;
import console.game.Player;
import lombok.Getter;
import ui.components.ButtonStyling;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class StartMenu {

    private Screen screen;
    private MultiWindowTextGUI gui;
    private Player player;

    public StartMenu() throws IOException {
        this.screen = new DefaultTerminalFactory().createScreen();
        this.screen.startScreen();
        this.gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK_BRIGHT));
    }

    public void showStartMenu() {
        final BasicWindow window = new BasicWindow("Start Menu");

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
            player.name = enterName();

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
            }
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
