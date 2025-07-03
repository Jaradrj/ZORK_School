package game;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Player {
    private Set<String> flags = new HashSet<>();
    @Getter
    private Set<String> inventory = new HashSet<>();
    public String name;
    public String oldName;

    @Getter
    @Setter
    private String currentRoom;

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
