package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import lombok.Getter;
import ui.game.*;
import console.game.*;

import java.io.IOException;

public class UIGameController {

    @Getter
    private Player player;
    private UIRoom currentRoom;
    private UICommands command;

    private Screen screen;
    private MultiWindowTextGUI gui;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;

    public UIGameController(UICommands commands, Player player) throws IOException {
        this.command = commands;
        this.player = player;
        UIRoomFactory.setController(this);

        this.screen = new DefaultTerminalFactory().createScreen();
        this.screen.startScreen();
        this.gui = new MultiWindowTextGUI(screen);

        UIRoom room = UIRoomFactory.createRoom("main entrance hall");
        System.out.println("Created room: " + room + ", type: " + room.getClass());


        this.currentRoom = room;
        player.setCurrentUIRoom(currentRoom);

        this.window = new BasicWindow("MindScale");
        this.mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.outputArea = new TextBox(new TerminalSize(100, 15), TextBox.Style.MULTI_LINE)
                .setReadOnly(true);
        this.mainPanel.addComponent(outputArea);

        this.actionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.mainPanel.addComponent(actionPanel);

        this.window.setComponent(mainPanel);

        updateUI();
    }

    private void updateUI() throws IOException {
        outputArea.setText(currentRoom.enter(player));
        refreshActionButtons();
    }

    private void refreshActionButtons() throws IOException {
        actionPanel.removeAllComponents();
        for (String action : currentRoom.getAvailableActions(player)) {
            Button actionButton = new Button(action, () -> {
                String result = currentRoom.performAction(player, action.toLowerCase().trim());
                outputArea.setText(outputArea.getText() + "\n\n" + result);

                if (player.getCurrentRoom() != currentRoom) {
                    currentRoom = (UIRoom) player.getCurrentRoom();
                    outputArea.setText(currentRoom.enter(player));
                }
                try {
                    refreshActionButtons();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            actionPanel.addComponent(actionButton);
        }
        gui.updateScreen();
    }

    public void run() {
        gui.addWindowAndWait(window);
    }

}