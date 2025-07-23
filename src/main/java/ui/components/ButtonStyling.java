package ui.components;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import lombok.Setter;


public class ButtonStyling implements InteractableRenderer<Button> {

    @Setter
    TextColor bgColor;
    @Setter
    TextColor fgColor;

    @Override
    public TerminalSize getPreferredSize(Button component) {
        return new TerminalSize(component.getLabel().length() + 5, 1);
    }

    @Override
    public void drawComponent(TextGUIGraphics graphics, Button button) {
        boolean focused = button.isFocused();

        graphics.setBackgroundColor(bgColor);
        graphics.setForegroundColor(fgColor);

        if (focused) {
            graphics.enableModifiers(SGR.BOLD);
        } else {
            graphics.disableModifiers(SGR.BOLD);
        }

        String label = button.getLabel();
        String actualLabel = "< " + button.getLabel() + " >";
        if (focused) {
            actualLabel = " > " + label;
        }

        graphics.putString(0, 0, actualLabel);
    }

    @Override
    public TerminalPosition getCursorLocation(Button button) {
        return null;
    }
}

