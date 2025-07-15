package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.MouseCaptureMode;
import lombok.Getter;
import ui.game.*;
import console.game.*;
import ui.audio.TypingEffect;
import lombok.Setter;

import java.io.IOException;
import java.util.Map;

public class UIGameController {

    @Getter
    private Player player;
    private UIRoom currentRoom;
    private UICommands command;
    private boolean isChoosingRoom = false;

    private Screen screen;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;
    private boolean showingEndingPrompt = false;

    @Setter
    @Getter
    private static MultiWindowTextGUI guiInstance;

    public UIGameController(UICommands commands, Player player) throws IOException {
        this.command = commands;
        this.player = player;
        UIRoomFactory.setController(this);

        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setMouseCaptureMode(MouseCaptureMode.CLICK_RELEASE_DRAG_MOVE);
        screen = factory.createScreen();
        this.screen.startScreen();
        guiInstance = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK_BRIGHT));
        UIGameController.setGuiInstance(guiInstance);


        SimpleTheme customTheme = SimpleTheme.makeTheme(
                true,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK_BRIGHT
        );

        this.guiInstance.setTheme(customTheme);

        UIRoom room = UIRoomFactory.createRoom("main entrance hall");
        System.out.println("Created room: " + room + ", type: " + room.getClass());


        this.currentRoom = room;
        player.setCurrentUIRoom(currentRoom);

        this.window = new BasicWindow("MindScale");
        this.mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.outputArea = new TextBox(new TerminalSize(200, 30), TextBox.Style.MULTI_LINE)
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
        TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
        refreshActionButtons();
    }

    private void refreshActionButtons() {

        SimpleTheme customButtonTheme = SimpleTheme.makeTheme(
                false,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.BLACK,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK_BRIGHT
        );

        if (showingEndingPrompt) {
            window.invalidate();
            return;
        }

        actionPanel.removeAllComponents();

        if (isChoosingRoom) {

            Map<String, Exit> exits = currentRoom.getAvailableExits(player);
            for (String roomName : exits.keySet()) {
                Button b = new Button(roomName, () -> {
                    String result = currentRoom.handleRoomChange(player, roomName);
                    outputArea.setText(outputArea.getText() + "\n\n" + result);
                    currentRoom = player.getCurrentUIRoom();
                    String enterText = currentRoom.enter(player);
                    TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
                    isChoosingRoom = false;
                    refreshActionButtons();
                });
                b.setTheme(customButtonTheme);
                actionPanel.addComponent(b);
            }

            if (player.getLastUIRoom() != null &&
                    player.getCurrentUIRoom().getName().equalsIgnoreCase("chemistry room")) {

                Button electricityButton = new Button("Electricity Room", () -> {
                    String msg = "You try to corrode the door.\nYou can hear the sizzling sound of the\nsulfuric acid oxidizing with the door.\n" +
                            "\nNevertheless, you still don't manage to open it.\nSad and defeated, you return to the chemistry room\nto try and cry.";
                    outputArea.setText(outputArea.getText() + "\n\n" + msg);
                    TypingEffect.typeWithSound(outputArea, msg, guiInstance, null);
                    isChoosingRoom = false;
                    refreshActionButtons();
                });
                actionPanel.addComponent(electricityButton);
            }

            Button returnButton = new Button("Return", () -> {
                isChoosingRoom = false;
                outputArea.setText(outputArea.getText() + "\n\nCanceled room selection.");
                refreshActionButtons();
            });
            returnButton.setTheme(customButtonTheme);
            actionPanel.addComponent(returnButton);
        } else {

            for (String action : currentRoom.getAvailableActions(player)) {
                Button b = new Button(action, () -> {
                    String result = currentRoom.performAction(player, action.toLowerCase().trim(), outputArea);
                    if (!result.isEmpty()) {
                        TypingEffect.typeWithSound(outputArea, "\n\n" + result, guiInstance, "/sounds/Terminal.wav");
                    }
                    if (player.getCurrentUIRoom() != currentRoom) {
                        currentRoom = player.getCurrentUIRoom();
                        String enterText = currentRoom.enter(player);
                        TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
                    }
                    if ("leave".equalsIgnoreCase(action)) {
                        isChoosingRoom = true;
                    }

                    refreshActionButtons();
                });
                b.setTheme(customButtonTheme);
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
        guiInstance.addWindowAndWait(window);
    }
}