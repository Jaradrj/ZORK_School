package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.MouseCaptureMode;
import lombok.Getter;
import ui.game.*;
import console.game.*;
import ui.audio.TypingEffect;

import java.io.IOException;
import java.util.Map;

public class UIGameController {

    @Getter
    private Player player;
    private UIRoom currentRoom;
    private UICommands command;
    private boolean isChoosingRoom = false;

    private Screen screen;
    @Getter
    private MultiWindowTextGUI gui;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;
    private Panel inventoryPanel;
    private boolean showingEndingPrompt = false;

    private ShowInventory showInventory;

    public UIGameController(UICommands commands, Player player) throws IOException {
        this.command = commands;
        this.player = player;
        UIRoomFactory.setController(this);

        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setMouseCaptureMode(MouseCaptureMode.CLICK_RELEASE_DRAG_MOVE);
        screen = factory.createScreen();
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

    private void updateUI() {
        outputArea.setText("");
        String enterText = currentRoom.enter(player);
        TypingEffect.typeWithSound(outputArea, enterText, gui);
        refreshActionButtons();
    }

    private void refreshActionButtons() {

        if (showingEndingPrompt) {
            window.invalidate();
            return;
        }

        actionPanel.removeAllComponents();

        Button inventoryButton = new Button("Inventory", () -> {
            ShowInventory inventoryView = new ShowInventory(gui, player.getInventory());
            inventoryView.showInventory();
            refreshActionButtons();
        });
        actionPanel.addComponent(inventoryButton);

        if (isChoosingRoom) {

            Map<String, Exit> exits = currentRoom.getAvailableExits(player);
            for (String roomName : exits.keySet()) {
                Button b = new Button(roomName, () -> {
                    String result = currentRoom.handleRoomChange(player, roomName);
                    outputArea.setText(outputArea.getText() + "\n\n" + result);
                    currentRoom = player.getCurrentUIRoom();
                    outputArea.setText(currentRoom.enter(player));
                    isChoosingRoom = false;
                    refreshActionButtons();
                });
                actionPanel.addComponent(b);
            }
            Button returnButton = new Button("Return", () -> {
                isChoosingRoom = false;
                outputArea.setText(outputArea.getText() + "\n\nCanceled room selection.");
                refreshActionButtons();
            });
            actionPanel.addComponent(returnButton);
        } else {

            for (String action : currentRoom.getAvailableActions(player)) {
                Button b = new Button(action, () -> {
                    String result = currentRoom.performAction(player, action.toLowerCase().trim(), outputArea);
                    if (player.getCurrentUIRoom() != currentRoom) {
                        currentRoom = player.getCurrentUIRoom();
                        outputArea.setText(currentRoom.enter(player));
                    }

                    if ("leave".equalsIgnoreCase(action)) {
                        isChoosingRoom = true;
                    }

                    refreshActionButtons();
                });
                actionPanel.addComponent(b);
            }
        }

        window.invalidate();
    }

    public void showEndingPrompt() {
        showingEndingPrompt = true;
        actionPanel.removeAllComponents();

        actionPanel.addComponent(new Button("Yes", () -> {
            player.clearFlags();
            player.setFlag("second_try");
            showingEndingPrompt = false;
            refreshActionButtons();
        }));

        actionPanel.addComponent(new Button("No", () -> {
            System.exit(0);
        }));

        window.invalidate();
    }

    public void run() {
        gui.addWindowAndWait(window);
    }

}