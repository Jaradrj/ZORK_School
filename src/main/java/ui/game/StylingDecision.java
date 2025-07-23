package ui.game;

import com.googlecode.lanterna.gui2.Button;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class StylingDecision {

    private final Set<ButtonDescription> styles;

    public StylingDecision() {
        this.styles = new HashSet<>();
    }

    public void addStyle(Button button, String description) {
        styles.add(new ButtonDescription(button, description));
    }

    public Set<ButtonDescription> getStyles() {
        return new HashSet<>(styles);
    }

    @Getter
    public static class ButtonDescription {
        private final Button button;
        private final String description;

        public ButtonDescription(Button button, String description) {
            this.button = button;
            this.description = description;
        }
    }
}