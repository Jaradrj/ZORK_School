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
    private StartMenu start;

    private Screen screen;
    private MultiWindowTextGUI gui;
    private BasicWindow window;
    private Panel mainPanel;
    private TextBox outputArea;
    private Panel actionPanel;

    public UIGameController(Commands command) throws IOException {
        this.start = new StartMenu();
        this.screen = start.getScreen();
        this.gui = start.getGui();

        this.command = command;
        this.player = new Player();
        UIRoomFactory.setController(this);
        this.startRoom = RoomFactory.createRoom("main entrance hall");

        this.window = new BasicWindow("MindScale");
        this.mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.outputArea = new TextBox(new TerminalSize(50, 20), TextBox.Style.MULTI_LINE).setReadOnly(true);
        this.mainPanel.addComponent(outputArea);

        this.actionPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.mainPanel.addComponent(actionPanel);

        this.window.setComponent(mainPanel);
    }

    public void run() {
        start.showStartMenu();
        player.setCurrentRoom(startRoom);
        gui.addWindowAndWait(window);
    }

}
