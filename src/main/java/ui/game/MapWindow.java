package ui.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import console.game.Exit;
import console.game.Player;
import ui.components.ButtonStyling;
import ui.components.Themes;
import ui.controller.UIGameController;

import java.util.Map;
import java.util.Set;

public class MapWindow {

    private final MultiWindowTextGUI gui;
    private final UIGameController controller;
    private final Player player;
    private final ButtonStyling buttonStyling;

    public MapWindow(MultiWindowTextGUI gui, UIGameController controller, Player player, ButtonStyling buttonStyling) {
        this.gui = gui;
        this.player = player;
        this.buttonStyling = buttonStyling;
        this.controller = controller;
    }

    public void showMap() {
        BasicWindow mapWindow = new BasicWindow("Map");
        Panel panel = new Panel(new GridLayout(3));

        Map<String, Exit> exits = controller.currentRoom.getAvailableExits(player);
        Exit[] exitPositions = new Exit[6];
        int idx = 0;

        for (Map.Entry<String, Exit> entry : exits.entrySet()) {
            if (idx < 6) {
                exitPositions[idx++] = entry.getValue();
            }
        }

        for (int i = 0; i < 3; i++) {
            panel.addComponent(themedBox(exitPositions[i]));
        }

        panel.addComponent(themedBox(exitPositions[3]));
        panel.addComponent(new NonFocusableTextBox(controller.currentRoom.getName(), 20, 3));
        panel.addComponent(themedBox(exitPositions[4]));

        panel.addComponent(createEmptyTextBox());
        panel.addComponent(themedBox(exitPositions[5]));
        panel.addComponent(createEmptyTextBox());

        panel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        Button closeButton = new Button("Close", mapWindow::close);
        closeButton.setRenderer(buttonStyling);
        panel.addComponent(closeButton);

        mapWindow.setComponent(panel);
        mapWindow.setHints(Set.of(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING));
        gui.addWindowAndWait(mapWindow);
    }

    private TextBox themedBox(Exit exit) {
        if (exit == null) return createEmptyTextBox();
        String name = exit.getTargetRoom();
        TextBox box = createReadOnlyTextBox(name);
        box.setTheme(player.hasFlag("visited_" + name) ? Themes.defaultTheme : Themes.greenTheme);
        return box;
    }

    private TextBox createReadOnlyTextBox(String text) {
        return new NonFocusableTextBox(text, 20, 3);
    }

    private TextBox createEmptyTextBox() {
        return new NonFocusableTextBox("", 20, 1);
    }

    public static class NonFocusableTextBox extends TextBox {
        public NonFocusableTextBox(String text, int width, int height) {
            super(centerText(text, width, height));
            setReadOnly(true);
            setPreferredSize(new TerminalSize(width, height));
            setCaretPosition(-1);
        }

        @Override
        public boolean isFocusable() {
            return false;
        }

        private static String centerText(String text, int width, int height) {
            int paddingLeft = Math.max(0, (width - text.length()) / 2);
            int paddingRight = Math.max(0, width - text.length() - paddingLeft);
            String paddedLine = " ".repeat(paddingLeft) + text + " ".repeat(paddingRight);

            int emptyLinesAbove = (height - 1) / 2;
            int emptyLinesBelow = height - 1 - emptyLinesAbove;

            return "\n".repeat(emptyLinesAbove) + paddedLine + "\n".repeat(emptyLinesBelow);
        }
    }
}