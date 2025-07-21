package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import lombok.Getter;
import lombok.Setter;
import ui.UIMain;
import ui.audio.SoundPlayer;
import ui.game.*;
import console.game.*;
import ui.audio.TypingEffect;


import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import com.googlecode.lanterna.input.KeyStroke;

import javax.swing.*;

public class UIGameController {

    @Getter
    private Player player;
    private UIRoom currentRoom;
    private boolean isChoosingRoom = false;

    private Screen screen;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;
    private boolean showingEndingPrompt = false;

    @Getter
    @Setter
    private static UIGameController current;

    @Setter
    @Getter
    private static MultiWindowTextGUI guiInstance;

    public UIGameController(Player player) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        screen.startScreen();
        if (terminal instanceof SwingTerminalFrame swingFrame) {
            swingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            swingFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
        this.player = player;
        current = this;
        UIRoomFactory.setController(this);

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

        guiInstance.setTheme(customTheme);

        UIRoom room = UIRoomFactory.createRoom("main entrance hall");

        this.currentRoom = room;
        player.setCurrentUIRoom(currentRoom);

        this.mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.outputArea = new TextBox(new TerminalSize(200, 30), TextBox.Style.MULTI_LINE)
                .setReadOnly(true);
        this.mainPanel.addComponent(outputArea);

        this.actionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.mainPanel.addComponent(actionPanel);

        this.window = new BasicWindow();
        this.window.setTitle(currentRoom.getName());
        this.window.setComponent(mainPanel);

        window.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == ' ') {
                    TypingEffect.skipTyping();
                    hasBeenHandled.set(true);
                }
            }
        });

        window.addWindowListener(new WindowListenerAdapter() {
            @Override
            public  void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Enter){
                    TypingEffect.stopWaiting();
                    hasBeenHandled.set(true);
                }
            }
        });

        updateUI();
    }


    public void disableActionPanel() {
        actionPanel.setVisible(false);
        window.invalidate();
    }

    public void enableActionPanel() {
        actionPanel.setVisible(true);
        refreshActionButtons();
    }

    private void updateUI() {
        outputArea.setText("");
        currentRoom = player.getCurrentUIRoom();
        window.setTitle(currentRoom.getName());
        String enterText = currentRoom.enter(player);
        TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
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
                    try {
                        guiInstance.updateScreen();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    SoundPlayer.stopSound();
                    String result = currentRoom.handleRoomChange(player, roomName);
                    outputArea.setText(outputArea.getText() + result);
                    currentRoom = player.getCurrentUIRoom();
                    window.setTitle(currentRoom.getName());
                    String enterText = currentRoom.enter(player);
                    TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
                    isChoosingRoom = false;
                    refreshActionButtons();
                });
                actionPanel.addComponent(b);
            }

            if (player.getLastUIRoom() != null &&
                    player.getCurrentUIRoom().getName().equalsIgnoreCase("chemistry room") &&
                    player.hasFlag("acid_taken") &&
                    !player.hasFlag("corrosed_door")) {

                Button electricityButton = new Button("Electricity Room", () -> {
                    SoundPlayer.stopSound();
                    player.setFlag("corrosed_door");
                    SoundPlayer.playSound("/sounds/Sizzling.wav", 1000,0, outputArea, guiInstance, false);
                    String msg = "You try to corrode the door.\nYou can hear the sizzling sound of the\nsulfuric acid oxidizing with the door.\n" +
                            "\nNevertheless, you still don't manage to open it.\nSad and defeated, you return to the chemistry room\nto try and cry.";
                    outputArea.setText(outputArea.getText() + "\n\n" + msg);
                    TypingEffect.typeWithSound(outputArea, msg, guiInstance, null);
                    isChoosingRoom = false;
                    outputArea.invalidate();
                    try {
                        getGuiInstance().updateScreen();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    refreshActionButtons();
                });

                actionPanel.addComponent(electricityButton);
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
                    SoundPlayer.stopSound();
                    String result = currentRoom.performAction(player, action.toLowerCase().trim(), outputArea);
                    if (!result.isEmpty()) {
                        TypingEffect.typeWithSound(outputArea, result, guiInstance, "/sounds/Terminal.wav");
                    }
                    if (player.getCurrentUIRoom() != currentRoom) {
                        currentRoom = player.getCurrentUIRoom();
                        window.setTitle(currentRoom.getName());
                        String enterText = currentRoom.enter(player);
                        TypingEffect.typeWithSound(outputArea, enterText, guiInstance, "/sounds/Terminal.wav");
                    }
                    if ("leave".equalsIgnoreCase(action)) {
                        isChoosingRoom = true;
                    }

                    refreshActionButtons();
                });
                actionPanel.addComponent(b);
            }
        }
        Button inventoryButton = new Button("Inventory", () -> {
            ShowInventory inventoryView = new ShowInventory(guiInstance, player.getInventory());
            inventoryView.showInventory();
            refreshActionButtons();
        });
        actionPanel.addComponent(inventoryButton);

        window.invalidate();
    }

    public void showEndingPrompt(boolean happyEnding) {
        showingEndingPrompt = true;
        actionPanel.removeAllComponents();
        enableActionPanel();

        if(happyEnding) {

            String ending =
                    "████████╗██╗  ██╗███████╗    ███████╗███╗   ██╗██████╗ \n" +
                    "╚══██╔══╝██║  ██║██╔════╝    ██╔════╝████╗  ██║██╔══██╗\n" +
                    "   ██║   ███████║█████╗      █████╗  ██╔██╗ ██║██║  ██║\n" +
                    "   ██║   ██╔══██║██╔══╝      ██╔══╝  ██║╚██╗██║██║  ██║\n" +
                    "   ██║   ██║  ██║███████╗    ███████╗██║ ╚████║██████╔╝\n" +
                    "   ╚═╝   ╚═╝  ╚═╝╚══════╝    ╚══════╝╚═╝  ╚═══╝╚═════╝ \n";

            outputArea.setText(ending);

            actionPanel.addComponent(new Button("Exit", () -> System.exit(0)));
        } else {

            SoundPlayer.playSound("/sounds/TryAgain.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);

            String ending = """
                    Do you want to give up, or try again?
                    
                    You already know your answer.
                    They already recorded it.
                    """;
            TypingEffect.typeWithSound(outputArea, ending, getGuiInstance(), null);

            actionPanel.addComponent(new Button("Yes", () -> {
                player.clearFlags();
                player.setFlag("second_try");
                showingEndingPrompt = false;

                SoundPlayer.stopSound();

                try {
                    guiInstance.removeWindow(window);
                    window.close();
                    screen.stopScreen();

                    UIMain.startGame();

                } catch (IOException e) {
                    System.err.println("Restart error: " + e.getMessage());
                    System.exit(1);
                }
            }));

            actionPanel.addComponent(new Button("No", () -> System.exit(0)));

            window.invalidate();

        }
    }

    public void run() {
        guiInstance.addWindowAndWait(window);
    }
}