package ui.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import ui.game.*;
import console.game.*;

import java.io.IOException;

public class UIGameController {

    private Player player;
    private Room startRoom;
    private Commands command;

    private Screen screen;
    private MultiWindowTextGUI gui;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;

    public UIGameController(Commands command) throws IOException {
        this.command = command;
        this.player = new Player();
        UIRoomFactory.setController(this);
        this.startRoom = RoomFactory.createRoom("main entrance hall");

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        this.screen = terminalFactory.createScreen();
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen);
        window = new BasicWindow("MindScale");

        mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        outputArea = new TextBox(new TerminalSize(50, 20), TextBox.Style.MULTI_LINE)
                .setReadOnly(true);
        mainPanel.addComponent(outputArea);

        actionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        mainPanel.addComponent(actionPanel);

        window.setComponent(mainPanel);
    }

    public void run() {
        printStart();
        player.setCurrentRoom(startRoom);
    }

    private void appendOutput(String text) {
        outputArea.addLine(text);
    }

    private void printStart() {
        appendOutput("Welcome to MindScale!");
        appendOutput("press ENTER to start the game!");
    }

    public void stop() throws Exception {
        screen.stopScreen();
    }

}
