package ui.rooms;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import console.game.*;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.*;

public class UIChemistryRoom implements UIRoom {

    private final WindowBasedTextGUI gui;
    private UICommands commands;

    @Override
    public String getName() {
        return "chemistry room";
    }

    public UIChemistryRoom(UICommands commands,
                           WindowBasedTextGUI gui) {
        this.commands = commands;
        this.gui = gui;
    }

    @Override
    public String enter(Player player) {

        StringBuilder text = new StringBuilder();

        if (!player.hasFlag("was_chemistry")) {
            player.setFlag("was_chemistry");

            text.append("Science has never been your favorite subject. But it most definitely is Klara's.\n")
                    .append("The brewing station in the back of the room still has some chemicals opened. What is Scopolamine? I heard of that before...")
                    .append("While trying to remember, your flashlight suddenly starts flickering.\n ")
                    .append("Maybe you should try to find a way into the Electricity Room?");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("awaiting_formula_check")) {
            actions.add("H2O");
            actions.add("H2SO4");
            actions.add("HCl");
            actions.add("HNO3");
            actions.add("C17H21NO4");
            return actions;
        }

        actions.add("Look at Periodic Table");
        actions.add("Look at Formula Papers");
        if (!player.hasFlag("acid_taken") &&
                player.hasFlag("saw_formula_papers") &&
                player.hasFlag("saw_periodic_table")) {
            actions.add("Brew Acid");
        }
        actions.add("leave");

        return actions;
    }


    @Override
    public String performAction(Player player, String action, TextBox outputArea) {

        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "1":
            case "look":
            case "look at periodic table":
                player.setFlag("saw_periodic_table");
                result.append("+----+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                        "| H  | He                                                         |\n" +
                        "+----+----+                                                    +--+\n" +
                        "| Li | Be |                      | B  | C  | N  | O  | F  | Ne |  |\n" +
                        "+----+----+                      +----+----+----+----+----+----+--+\n" +
                        "| Na | Mg |                      | Al | Si | P  | S  | Cl | Ar |\n" +
                        "+----+----+                      +----+----+----+----+----+----+\n" +
                        "| K  | Ca | Sc | Ti | V  | Cr | Mn | Fe | Co | Ni | Cu | Zn |\n" +
                        "+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                        "| Br | I  | Hg | Pb | Sn | As | Sb | Cd | Ba | Sr | Ra | U  |\n" +
                        "+----+----+----+----+----+----+----+----+----+----+----+----+\n");
                break;
            case "2":
            case "look at formula papers":
                player.setFlag("saw_formula_papers");
                result.append("╔════════════════════════════════════╗\n" +
                        "║     ADVANCED CHEMISTRY FORMULAS   ║\n" +
                        "╠════════════════════════════════════╣\n" +
                        "║ Water               -> H2O         ║\n" +
                        "║ Sulfuric Acid       -> H2SO4       ║\n" +
                        "║ Hydrochloric Acid   -> HCl         ║\n" +
                        "║ Nitric Acid         -> HNO3        ║\n" +
                        "║ Scopolamine (alk.)  -> C17H21NO4   ║\n" +
                        "╚════════════════════════════════════╝\n" +
                        "*Note: Handle with caution.\n");
                break;

            case "3":
            case "brew acid":
                if (player.hasFlag("acid_taken")) {
                    result.append("You’ve already brewed the acid. You can’t carry more.");
                    break;
                }
                if (!player.hasFlag("saw_formula_papers") || !player.hasFlag("saw_periodic_table")) {
                    result.append("You don't understand the chemistry well enough yet. Maybe check the formula papers and periodic table first.");
                    break;
                }
                player.setFlag("awaiting_formula_check");
                result.append("Input chemical formula to brew sulfuric acid:");

                break;


            case "h2so4":
                if (player.hasFlag("awaiting_formula_check")) {
                    player.clearFlag("awaiting_formula_check");
                }

                if (action.equalsIgnoreCase("h2so4")) {
                    player.setFlag("acid_taken");
                    player.getInventory().addItem("Acid");

                    result.append("You mix the chemicals carefully. The solution bubbles violently.\n")
                            .append("You now carry Sulfuric Acid. Maybe it can help melt the lock on the Electricity Room door.");
                } else {
                    result.append("You mix the chemicals, but nothing happens. Wrong combination?");
                }
                break;
            case "leave":
                commands.checkInputCommands("-r", player, outputArea);
                return "";
            default:
                if (lowerAction.startsWith("go to ")) {
                    String roomChangeResult = handleRoomChange(player, lowerAction.substring(6).trim());
                    result.append(roomChangeResult);
                } else {
                    result.append("Invalid action.");
                }
                break;
        }
        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("secretary", new Exit("secretary", null));
        return exits;
    }
}