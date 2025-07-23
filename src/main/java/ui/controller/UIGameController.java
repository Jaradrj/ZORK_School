package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.gui2.Label;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import lombok.Getter;
import lombok.Setter;
import ui.UIMain;
import ui.audio.SoundPlayer;
import ui.components.*;
import ui.game.*;
import console.game.*;
import ui.audio.TypingEffect;


import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.lanterna.input.KeyStroke;

import javax.swing.*;

public class UIGameController {

    @Getter
    private Player player;
    public UIRoom currentRoom;
    private boolean isChoosingRoom = false;

    private TerminalScreen screen;
    private BasicWindow window;
    private Panel mainPanel;
    private Panel hintPanel;
    private TextBox outputArea;
    private Panel actionPanel;
    private boolean showingEndingPrompt = false;

    private static Label enterHint;
    private static Label skipHint;

    private static volatile boolean enterHintCanceled = false;
    private static volatile boolean skipHintCanceled = false;

    private ButtonStyling buttonStyling;

    private static final Map<Label, Boolean> canceledHints = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private static UIGameController current;

    @Setter
    @Getter
    private static MultiWindowTextGUI guiInstance;

    public UIGameController(Player player) throws IOException {
        this.buttonStyling = new ButtonStyling();
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

        guiInstance = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));
        UIGameController.setGuiInstance(guiInstance);

        UIRoom room = UIRoomFactory.createRoom("main entrance hall");

        this.currentRoom = room;
        player.setCurrentUIRoom(currentRoom);

        this.mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.outputArea = new TextBox(new TerminalSize(200, 30), TextBox.Style.MULTI_LINE)
                .setReadOnly(true);
        this.mainPanel.addComponent(outputArea);

        this.mainPanel.addComponent(new EmptySpace().setPreferredSize(new TerminalSize(0, 1)));

        this.actionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.mainPanel.addComponent(actionPanel);

        this.window = new BasicWindow();
        this.window.setTitle(currentRoom.getName());
        this.window.setComponent(mainPanel);
        window.setHints(Set.of(Window.Hint.NO_POST_RENDERING));

        this.hintPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));

        enterHint = new Label(" ");
        skipHint = new Label(" ");
        hintPanel.addComponent(enterHint, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        hintPanel.addComponent(skipHint, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        mainPanel.addComponent(hintPanel, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        mainPanel.addComponent(new Label("Press [H] to show help menu"), LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning));

        guiInstance.setTheme(Themes.defaultTheme);
        buttonStyling.setBgColor(Themes.TextColors.defaultBg);
        buttonStyling.setFgColor(Themes.TextColors.defaultText);

        player.getHelpActions().addAction("I", "Inventory");
        player.getHelpActions().addAction("TAB", "Select text or actions");
        player.getHelpActions().addAction("MOUSE UP/DOWN", "Select switch actions or scroll");
        player.getHelpActions().addAction("SPACE", "Skip text");
        player.getHelpActions().addAction("ENTER", "Skip ending/letters");
        player.getHelpActions().addAction("S", "Styling Options");


        Button redStyle = new Button("Red Theme", () -> {
            guiInstance.setTheme(Themes.redTheme);
            this.buttonStyling.setFgColor(Themes.TextColors.redText);
            this.buttonStyling.setBgColor(Themes.TextColors.redBg);
        });

        Button yellowStyle = new Button("Yellow Theme", () -> {
            guiInstance.setTheme(Themes.yellowTheme);
            this.buttonStyling.setFgColor(Themes.TextColors.yellowText);
            this.buttonStyling.setBgColor(Themes.TextColors.yellowBg);
        });

        Button greenStyle = new Button("Green Theme", () -> {
            guiInstance.setTheme(Themes.greenTheme);
            this.buttonStyling.setFgColor(Themes.TextColors.greenText);
            this.buttonStyling.setBgColor(Themes.TextColors.greenBg);
        });

        Button blueStyle = new Button("Blue Theme", () -> {
            guiInstance.setTheme(Themes.blueTheme);
            this.buttonStyling.setFgColor(Themes.TextColors.blueText);
            this.buttonStyling.setBgColor(Themes.TextColors.blueBg);
        });

        Button defaultTheme = new Button("Default Theme", () -> {
            guiInstance.setTheme(Themes.defaultTheme);
            this.buttonStyling.setFgColor(Themes.TextColors.defaultText);
            this.buttonStyling.setBgColor(Themes.TextColors.defaultBg);
        });

        redStyle.setRenderer(buttonStyling);
        yellowStyle.setRenderer(buttonStyling);
        blueStyle.setRenderer(buttonStyling);
        greenStyle.setRenderer(buttonStyling);
        defaultTheme.setRenderer(buttonStyling);

        player.getStylingDecision().addStyle(redStyle, "Red text with dark background");
        player.getStylingDecision().addStyle(blueStyle, "Blue text with dark background");
        player.getStylingDecision().addStyle(greenStyle, "Green text with dark background");
        player.getStylingDecision().addStyle(yellowStyle, "Yellow text with dark background");
        player.getStylingDecision().addStyle(defaultTheme, "Default Style");


        window.addWindowListener(new WindowListenerAdapter() {
            @Override
            public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {
                if (keyStroke.getKeyType() == KeyType.Character && keyStroke.getCharacter() == ' ') {
                    TypingEffect.skipTyping();
                    hasBeenHandled.set(true);
                } else if (keyStroke.getKeyType() == KeyType.Character &&
                           (keyStroke.getCharacter() == 'i' || keyStroke.getCharacter() == 'I')) {
                    showInventory();
                    hasBeenHandled.set(true);
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    TypingEffect.stopWaiting();
                    hasBeenHandled.set(true);
                } else if (keyStroke.getKeyType() == KeyType.Character &&
                           (keyStroke.getCharacter() == 'h' || keyStroke.getCharacter() == 'H')) {
                    showHelpActions();
                    hasBeenHandled.set(true);
                }
                else if (keyStroke.getKeyType() == KeyType.Character &&
                         (keyStroke.getCharacter() == 's' || keyStroke.getCharacter() == 'S')) {
                    showStylingChoices();
                    hasBeenHandled.set(true);
                }
                else if (keyStroke.getKeyType() == KeyType.Character &&
                         (keyStroke.getCharacter() == 'm' || keyStroke.getCharacter() == 'M')) {
                    showMap();
                    hasBeenHandled.set(true);
                }
            }
        });

        System.out.println(player.oldName);
        System.out.println(player.name);
        updateUI();
    }

    private static void setHintVisibilityWithDelay(Label hintLabel, String textToShow, boolean show) {
        if (hintLabel == null) return;

        canceledHints.put(hintLabel, !show);


        int delay;

        if (UIEndings.enteredEndings) {
            delay = 1000;
        } else {
            delay = 3000;
        }

        if (show) {
            new Thread(() -> {
                try {
                    Thread.sleep(delay);
                    if (!canceledHints.getOrDefault(hintLabel, false)) {
                        guiInstance.getGUIThread().invokeLater(() -> {
                            hintLabel.setText(textToShow);
                            try {
                                guiInstance.updateScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            hintLabel.setText(" ");
            try {
                guiInstance.updateScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setShowEnterHint(boolean show) {
        enterHintCanceled = !show;
        setHintVisibilityWithDelay(enterHint, "Press ENTER to continue", show && !enterHintCanceled);
    }

    public static void setShowSkipHint(boolean show) {
        skipHintCanceled = !show;
        setHintVisibilityWithDelay(skipHint, "Press SPACE to skip", show && !skipHintCanceled);
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
        SimpleTheme lightsOnTheme = SimpleTheme.makeTheme(
                true,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE_BRIGHT,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.WHITE,
                TextColor.ANSI.BLACK_BRIGHT,
                TextColor.ANSI.WHITE
        );
        if (player.hasFlag("died_to_lights")) {
            guiInstance.setTheme(lightsOnTheme);

        }

        if (showingEndingPrompt) {
            window.invalidate();
            return;
        }

        actionPanel.removeAllComponents();

        if (isChoosingRoom) {

            Map<String, Exit> exits = currentRoom.getAvailableExits(player);
            for (String roomName : exits.keySet()) {
                Button b = new Button(roomName, () -> {
                    player.setFlag("visited_" + roomName);
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
                b.setRenderer(buttonStyling);
                actionPanel.addComponent(b);
            }

            if (player.getLastUIRoom() != null &&
                player.getCurrentUIRoom().getName().equalsIgnoreCase("chemistry room") &&
                player.hasFlag("acid_taken") &&
                !player.hasFlag("corrosed_door")) {

                Button electricityButton = new Button("Electricity Room", () -> {
                    SoundPlayer.stopSound();
                    player.setFlag("corrosed_door");
                    SoundPlayer.playSound("/sounds/Sizzling.wav", 1000, 0, outputArea, guiInstance, false);
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
                electricityButton.setRenderer(buttonStyling);
                actionPanel.addComponent(electricityButton);
            }

            Button returnButton = new Button("Return", () -> {
                isChoosingRoom = false;
                outputArea.setText(outputArea.getText() + "\n\nCanceled room selection.");
                refreshActionButtons();
            });
            returnButton.setRenderer(buttonStyling);
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
                b.setRenderer(buttonStyling);
                actionPanel.addComponent(b);
            }
        }

        window.invalidate();
    }

    public void showEndingPrompt(boolean happyEnding) {
        showingEndingPrompt = true;
        actionPanel.removeAllComponents();
        enableActionPanel();

        if (happyEnding) {

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

            Button yesButton = new Button("Yes", () -> {
                player.clearFlags();
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
            });

            actionPanel.addComponent(yesButton);
            if (player.hasFlag("died_to_lights")) {
                buttonStyling.setFgColor(Themes.TextColors.blackText);
                buttonStyling.setBgColor(Themes.TextColors.lightBg);
                yesButton.setRenderer(buttonStyling);
            } else {
                yesButton.setRenderer(buttonStyling);
            }

            Button noButton = new Button("No", () -> System.exit(0));
            actionPanel.addComponent(noButton);
            if (player.hasFlag("died_to_lights")) {
                buttonStyling.setFgColor(Themes.TextColors.blackText);
                buttonStyling.setBgColor(Themes.TextColors.lightBg);
                noButton.setRenderer(buttonStyling);
            } else {
                noButton.setRenderer(buttonStyling);
            }

            window.invalidate();

        }
    }

    private void showInventory() {

        ShowInventory inventoryView = new ShowInventory(guiInstance, player.getInventory(), buttonStyling);
        inventoryView.showInventory();

    }

    private void showHelpActions() {

        ShowHelpActions helpView = new ShowHelpActions(guiInstance, player.getHelpActions(), buttonStyling);
        helpView.showHelpActions();

    }

    private void showStylingChoices() {

        ShowStylingDecisions stylingView = new ShowStylingDecisions(guiInstance, player.getStylingDecision(), buttonStyling);
        stylingView.showStyleActions();

    }

    public void showMap() {

        MapWindow mapWindow = new MapWindow(guiInstance, getCurrent(), player, buttonStyling);
        mapWindow.showMap();
    }


    public void run() {
        guiInstance.addWindowAndWait(window);
    }

}