package ui.game;

import java.util.Map;
import java.util.Set;

import com.googlecode.lanterna.gui2.TextBox;
import console.rooms.*;
import console.game.*;

public class UICommands {

    private Map<String, UIRoom> rooms;

    public UICommands(Map<String, UIRoom> rooms) {
        this.rooms = rooms;
    }

    public void checkInputCommands(String input, Player player, TextBox outputArea) {
        switch (input.toLowerCase().trim()) {
            case "-h":
            case "h":
                printCommands(outputArea);
                break;
            case "-i":
            case "i":
                printInventory(player.getInventory().getItems(), outputArea);
                break;
            case "-r":
            case "r":

                break;
            default:
                outputArea.setText(outputArea.getText() + "\nUnknown command " + input);
                outputArea.setText(outputArea.getText() + "\nEnter \"-h\" for help");
                break;
        }
    }

    public void printCommands(TextBox outputArea) {
        String text =
                "Commands:\n" +
                        "-h\t\tview all commands\n" +
                        "-i\t\topen inventory\n" +
                        "-r\t\tavailable rooms\n" +
                        "-go to …\tchange rooms\n";

        outputArea.setText(outputArea.getText() + "\n" + text);
    }

    public void printInventory(Set<String> inventory, TextBox outputArea) {
        StringBuilder sb = new StringBuilder("\nInventory:\n");
        if (inventory.isEmpty()) {
            sb.append("- (empty)\n");
        } else {
            for (String item : inventory) {
                sb.append("- ").append(item).append("\n");
            }
        }
        outputArea.setText(outputArea.getText() + sb.toString());
    }

}

