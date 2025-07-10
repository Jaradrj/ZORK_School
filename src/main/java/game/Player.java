package game;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Player {
    private Set<String> flags = new HashSet<>();
    @Getter
    public Inventory inventory;
    public String name;
    public String oldName;

    @Getter
    private Room currentRoom;
    @Getter
    @Setter
    private Room lastRoom;

    public void setCurrentRoom(Room newRoom) {
        if (this.currentRoom != null) {
            this.lastRoom = this.currentRoom;
        }
        this.currentRoom = newRoom;
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

    public void addItem(String item) {
        inventory.add(item);
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }

}
