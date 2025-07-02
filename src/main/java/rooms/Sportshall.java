package rooms;

import game.Player;
import game.Room;

import java.util.HashMap;
import java.util.Map;

public class Sportshall implements Room {

    @Override
    public String getName() {
        return "Sportshall";
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_sports")) {
            player.setFlag("was_sports");
            System.out.println("You enter the Sportshall.");
            System.out.println("It smells weird in here. Not the type of weird you find in a boys wardrobe, but rather the type of weird you smell in basements. The smell seems like it's coming from the shaft.\nWhy is it open? There must be a way to get up there. ");
        }
        System.out.println("Actions: ");
        if (player.hasFlag("was_electricity")) {
            System.out.println(" - Use Bench");
        } else {
            System.out.println("- Move bench");
        }
        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "move":
            case "use":
            case "move bench":
            case "use bench":
                player.setFlag("entered_electricity");
                if (!player.hasFlag("was_electricity")) {
                    System.out.println("You move the bench. Luckily, it's not that far away from the shaft. By using your skill, you manage to climb into the shaft.\nWhile crawling through, you notice the smell getting worse and worse, to the point you almost have to throw up.\nYou start to hear a buzzing sound. That's the moment you realize, you made it to the Electricity Room. ");
                }
                return handleRoomChange(player, "Electricity Room");
            case "leave":
            case "2":
                return "You decide to leave. Where do you want to go? (Use: go to X)";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
        }
        return "Invalid action.";
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName.toLowerCase())) {
            player.setCurrentRoom(roomName);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("secretary", new Exit("Secretary", null));
        if (player.hasFlag("entered_electricity")) {
            exits.put("electricity room", new Exit("Electricity Room", null));
        }
        return exits;
    }
}