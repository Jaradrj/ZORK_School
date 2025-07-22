package console.game;

import lombok.Getter;
import lombok.Setter;
import ui.game.HelpActions;
import ui.game.UIRoom;

import java.util.*;

public class Player {
    private Set<String> flags = new HashSet<>();
    @Getter
    public Inventory inventory;
    @Getter
    public HelpActions helpActions;
    @Setter
    public String name;
    public String oldName;
    @Getter
    private Room currentRoom;
    @Getter
    private UIRoom currentUIRoom;
    @Getter
    @Setter
    private UIRoom lastUIRoom;

    @Getter
    @Setter
    private Room lastRoom;

    public Player() {
        this.inventory = new Inventory();
        this.helpActions = new HelpActions();
    }

    public void setCurrentRoom(Room newRoom) {
        if (this.currentRoom != null) {
            this.lastRoom = this.currentRoom;
        }
        this.currentRoom = newRoom;
    }
    public void setCurrentUIRoom(UIRoom newRoom) {
        if (this.currentUIRoom != null) {
            this.lastUIRoom = this.currentUIRoom;
        }
        this.currentUIRoom = newRoom;
    }

    public void setFlag(String flag) {
        flags.add(flag);
    }

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    public void clearFlags() {
        flags.clear();
    }

    public void clearFlag(String flag) {
        flags.remove(flag);
    }
}
