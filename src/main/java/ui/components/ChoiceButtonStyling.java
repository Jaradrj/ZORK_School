package ui.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class ChoiceButtonStyling implements InteractableRenderer<Button> {

    @Override
    public TerminalSize getPreferredSize(Button component) {
        return new TerminalSize(component.getLabel().length(), 1);
    }

    @Override
    public void drawComponent(TextGUIGraphics graphics, Button button) {

        TextColor fg = button.isFocused() ? TextColor.ANSI.BLACK : TextColor.ANSI.WHITE;
        TextColor bg = button.isFocused() ? TextColor.ANSI.WHITE : TextColor.ANSI.DEFAULT;

        graphics.setBackgroundColor(bg);
        graphics.setForegroundColor(fg);

        graphics.fill(' ');
        graphics.putString(0, 0, button.getLabel());

    }

    @Override
    public TerminalPosition getCursorLocation(Button button) {
        return null;
    }

}
