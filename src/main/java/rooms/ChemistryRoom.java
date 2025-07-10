package rooms;

import game.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import game.Exit;
import game.RoomFactory;

public class ChemistryRoom implements Room {

    private Commands commands;

    @Override
    public String getName() {
        return "chemistry room";
    }
    public ChemistryRoom(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void enter(Player player) {

        if (!player.hasFlag("was_chemistry")) {
            player.setFlag("was_chemistry");

            System.out.println("Science has never been your favorite subject. But it most definitely is Klara's.\n" +
                    "This whole room is just full of her awards. It seems like they cover up the yellow walls.\n" +
                    "The brewing station in the back of the room still has some chemicals opened. What is Scopolamine? I heard of that before...\n" +
                    "While trying to remember, your flashlight's battery suddenly dies.\n " +
                    "Maybe you should try to find a way into the Electricity Room?");
        }
        System.out.println("Actions:");
        System.out.println("- Look at Periodic Table");
        System.out.println("- Look at Formula Papers");
        if (!player.hasFlag("acid_taken") &&
                player.hasFlag("saw_formula_papers") &&
                player.hasFlag("saw_periodic_table")) {
            System.out.println("- Brew Acid");
        }
        System.out.println("- Leave");
    }


    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "look":
            case "look at periodic table":
                player.setFlag("saw_periodic_table");
                return "+----+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                        "| H  | He                                                         |\n" +
                        "+----+----+                                                    +--+\n" +
                        "| Li | Be |                      | B  | C  | N  | O  | F  | Ne |  |\n" +
                        "+----+----+                      +----+----+----+----+----+----+--+\n" +
                        "| Na | Mg |                      | Al | Si | P  | S  | Cl | Ar |\n" +
                        "+----+----+                      +----+----+----+----+----+----+\n" +
                        "| K  | Ca | Sc | Ti | V  | Cr | Mn | Fe | Co | Ni | Cu | Zn |\n" +
                        "+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                        "| Br | I  | Hg | Pb | Sn | As | Sb | Cd | Ba | Sr | Ra | U  |\n" +
                        "+----+----+----+----+----+----+----+----+----+----+----+----+\n";
            case "2":
            case "look at formula papers":
                player.setFlag("saw_formula_papers");
                return "╔════════════════════════════════════╗\n" +
                        "║     ADVANCED CHEMISTRY FORMULAS   ║\n" +
                        "╠════════════════════════════════════╣\n" +
                        "║ Water               -> H2O         ║\n" +
                        "║ Sulfuric Acid       -> H2SO4       ║\n" +
                        "║ Hydrochloric Acid   -> HCl         ║\n" +
                        "║ Nitric Acid         -> HNO3        ║\n" +
                        "║ Scopolamine (alk.)  -> C17H21NO4   ║\n" +
                        "╚════════════════════════════════════╝\n" +
                        "*Note: Handle with caution.\n";
            case "3":
            case "brew acid":
                if (player.hasFlag("acid_taken")) {
                    return "You’ve already brewed the acid. You can’t carry more.";
                }
                if (!player.hasFlag("saw_formula_papers") || !player.hasFlag("saw_periodic_table")) {
                    return "You don't understand the chemistry well enough yet. Maybe check the formula papers and periodic table first.";
                }
                Scanner scanner = new Scanner(System.in);
                System.out.println("Input chemical formula to brew sulfuric acid:");
                String formula = scanner.nextLine().trim();
                if (formula.equalsIgnoreCase("h2so4")) {
                    player.setFlag("acid_taken");
                    player.getInventory().addItem("Acid");

                    return "You mix the chemicals carefully. The solution bubbles violently.\n" +
                            "You now carry Sulfuric Acid. Maybe it can help melt the lock on the Electricity Room door.";
                } else {
                    return "You mix the chemicals, but nothing happens. Wrong combination?";
                }
            case "leave":
                commands.checkInputCommands("-r", player);
                return "";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    if (action.toLowerCase().equals("go to electricity room")) {

                        return "You try to corrose the door. You can hear the sizzling sound of the sulfuric acid oxidating with the door." +
                                "\nNever the less, you still don't manage to open it. Sad and defeated you return to the chemistry room to try and cry.";
                    }
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
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