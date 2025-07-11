package ui.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.TextColor;

public class ButtonStyling implements InteractableRenderer<Button> {

    @Override
    public TerminalSize getPreferredSize(Button component) {
        return new TerminalSize(component.getLabel().length(), 1);
    }

    @Override
    public void drawComponent(TextGUIGraphics graphics, Button button) {

        TextColor bg = TextColor.ANSI.DEFAULT;
        TextColor fg = button.isFocused() ? TextColor.ANSI.WHITE_BRIGHT : TextColor.ANSI.WHITE;

        graphics.setBackgroundColor(bg);
        graphics.setForegroundColor(fg);

        graphics.putString(0, 0, button.getLabel());
    }

    @Override
    public TerminalPosition getCursorLocation(Button button) {
        return null;
    }
}