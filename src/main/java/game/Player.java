package game;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Player {
    private Set<String> flags = new HashSet<>();
    private Set<String> inventory = new HashSet<>();

    @Getter
    @Setter
    private Room currentRoom;

    public void setFlag(String flag) {
        flags.add(flag);
    }

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }

}
