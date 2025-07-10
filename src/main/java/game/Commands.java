package game;

import java.util.Map;
import java.util.Set;
import rooms.*;
import game.*;

public class Commands {

    private Map<String, Room> rooms;

    public Commands(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public void checkInputCommands(String input, Player player) {

        switch (input.toLowerCase().trim()) {
            case "-h":
            case "h":
                printCommands();
                break;
            case "-i":
            case "i":
                printInventory(player.getInventory().getItems());
                break;
            case "-r":
            case "r":
                printAvailableRooms(player);
                break;
            default:
                System.out.println("Unknown command " + input);
                System.out.println("Enter \"-h\" for help");
                break;
        }
    }

    public void printCommands() {
        System.out.println(
                        "Commands:\n" +
                        "-h\t\tview all commands\n" +
                        "-i\t\topen inventory\n" +
                        "-r\t\tavailable rooms\n" +
                        "-go to …\tchange rooms\n"
        );
    }

    public void printInventory(Set<String> inventory) {
        System.out.println("\nInventory:");
        if (inventory.isEmpty()) {
            System.out.println("- (empty)");
            return;
        }
        for (String item : inventory) {
            System.out.println("- " + item);
        }
    }

    public void printAvailableRooms(Player player) {
        String currentRoomName = player.getCurrentRoom().getName();
        Room currentRoom = rooms.get(currentRoomName);
        Map<String, Exit> exits = currentRoom.getAvailableExits(player);

        System.out.println("\nAvailable Rooms:");
        if (exits.isEmpty()) {
            System.out.println("(empty))");
            return;
        }
        for (String roomName : exits.keySet()) {
            System.out.println("- " + roomName);
        }
    }
}
