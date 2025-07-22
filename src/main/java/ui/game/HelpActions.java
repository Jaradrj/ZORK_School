package ui.game;

import java.util.HashSet;
import java.util.Set;

public class HelpActions {
    private final Set<HelpAction> actions;

    public HelpActions() {
        this.actions = new HashSet<>();
    }

    public void addAction(String key, String description) {
        actions.add(new HelpAction(key, description));
    }

    public Set<HelpAction> getActions() {
        return new HashSet<>(actions);
    }

    // ✅ Inner class
    public static class HelpAction {
        private final String key;
        private final String description;

        public HelpAction(String key, String description) {
            this.key = key;
            this.description = description;
        }

        public String getKey() {
            return key;
        }

        public String getDescription() {
            return description;
        }
    }
}
