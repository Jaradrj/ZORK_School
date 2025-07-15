package ui.components;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import ui.controller.UIGameController;

import java.io.IOException;


public class TextPrinter {

    public void textPrinter(String input, TextBox outputArea) {

        outputArea.addLine("");
        for (String line : input.split("\n")) {
            outputArea.addLine(line);
        }
        outputArea.invalidate();
        try {
            UIGameController.getGuiInstance().updateScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logoPrinter(String input, TextBox outputArea) {

        outputArea.addLine("");
        for (String line : input.split("\n")) {
            outputArea.addLine(line);
        }
    }

    public void printBanner(TextBox outputArea) {
        outputArea.addLine("""
             \s
              ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗\s
             ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗
             ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝
             ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗
             ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║
              ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝
           \s
           \s""");
    }
}