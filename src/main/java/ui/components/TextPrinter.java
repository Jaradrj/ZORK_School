package ui.components;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;


public final class TextPrinter {

    public void textPrinter(String input, TextBox outputArea) {

        outputArea.addLine("");
        for (String line : input.split("\n")) {
            outputArea.addLine(line);
        }
        outputArea.invalidate();
    }
}