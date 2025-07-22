package ui.rooms;

import com.googlecode.lanterna.gui2.*;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import ui.game.UIEndings;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.*;

public class UIChemistryRoom implements UIRoom {

    private TextPrinter printer;
    private UIEndings endings;
    @Override
    public String getName() {
        return "chemistry room";
    }

    public UIChemistryRoom(TextPrinter printer, UIGameController controller) {
        this.printer = printer;
        this.endings = new UIEndings(controller);
    }

    @Override
    public String enter(Player player) {

        StringBuilder text = new StringBuilder();

        if (!player.hasFlag("was_chemistry")) {
            player.setFlag("was_chemistry");

            text.append("Science has never been your favorite subject.\nBut it most definitely is Klara's.\n")
                    .append("The brewing station in the back of the\nroom still has some chemicals opened.\nWhat is Scopolamine? I heard of that before...\n")
                    .append("While trying to remember, your flashlight suddenly starts flickering.\n");
            if (!player.hasFlag("entered_electricity")) {
                text.append("Maybe you should try to find a way into the Electricity Room?");
            }
        } else {
            text.append("You enter the Chemistry Room");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("awaiting_formula_check")) {
            actions.add("H2O");
            actions.add(" H2SO4 ");
            actions.add("HCl");
            actions.add("HNO3");
            actions.add(" C17H21NO4 ");
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
            case "look at periodic table":
                player.setFlag("saw_periodic_table");
                String input = """                    
                        +----+----+----+----+----+----+----+----+----+----+----+----+----+\n
                        | H  | He                                                         |\n
                        +----+----+                                                    +--+\n
                        | Li | Be |                      | B  | C  | N  | O  | F  | Ne |  |\n
                        +----+----+                      +----+----+----+----+----+----+--+\n
                        | Na | Mg |                      | Al | Si | P  | S  | Cl | Ar |\n
                        +----+----+                      +----+----+----+----+----+----+\n
                        | K  | Ca | Sc | Ti | V  | Cr | Mn | Fe | Co | Ni | Cu | Zn |\n
                        +----+----+----+----+----+----+----+----+----+----+----+----+\n
                        | Br | I  | Hg | Pb | Sn | As | Sb | Cd | Ba | Sr | Ra | U  |\n
                        +----+----+----+----+----+----+----+----+----+----+----+----+\n
                        """;
                outputArea.setText("");
                printer.textPrinter(input, outputArea);
                SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);

                break;
            case "look at formula papers":
                player.setFlag("saw_formula_papers");
                String text = """
                        ╔════════════════════════════════════╗\n
                        ║     ADVANCED CHEMISTRY FORMULAS    ║\n
                        ╠════════════════════════════════════╣\n
                        ║ Water               -> H2O         ║\n
                        ║ Sulfuric Acid       -> H2SO4       ║\n
                        ║ Hydrochloric Acid   -> HCl         ║\n
                        ║ Nitric Acid         -> HNO3        ║\n
                        ║ Scopolamine (alk.)  -> C17H21NO4   ║\n
                        ╚════════════════════════════════════╝\n
                        *Note: Handle with caution.\n
                        """;
                outputArea.setText("");
                printer.textPrinter(text, outputArea);
                SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                break;

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
            case "h2o":
            case "hcl":
            case "hno3":
                result.append("You expect to hear a sizzling sound, but it doesn't happen. Wrong combination?");
                break;
            case "h2so4":
                if (player.hasFlag("awaiting_formula_check")) {
                    player.clearFlag("awaiting_formula_check");
                }

                if (action.equalsIgnoreCase("h2so4")) {
                    player.setFlag("acid_taken");
                    player.getInventory().addItem("Acid");
                    SoundPlayer.playSound("/sounds/Brewing.wav", 2000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("You mix the chemicals carefully. The solution bubbles violently.\n")
                            .append("You now carry Sulfuric Acid.\n");
                    if (!player.hasFlag("entered_electricity")) {
                        result.append("Maybe it can help melt the lock on the Electricity Room door");
                    }
                } else {
                    result.append("You mix the chemicals, but nothing happens. Wrong combination?");
                }
                break;

            case "c17h21no4":
                endings.brewingEnding(player, outputArea);
            case "leave":
                return "";
            default:
                result.append("Invalid action.");
                break;
        }
        TypingEffect.typeWithSound(outputArea, result.toString(), UIGameController.getGuiInstance(), null);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
        player.setCurrentUIRoom(targetRoom);
        return "";
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("secretary", new Exit("secretary", null));
        return exits;
    }
}