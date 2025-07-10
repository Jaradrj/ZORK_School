package game;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private Set<String> items;

    public Inventory() {
        this.items = new HashSet<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public boolean hasItem(String item) {
        return items.contains(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public Set<String> getItems() {
        return new HashSet<>(items); // defensive copy
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
