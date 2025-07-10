package rooms;

import game.*;
import java.util.HashMap;
import java.util.Map;

public class Sportshall implements Room {

    private Commands commands;

    @Override
    public String getName() {
        return "sportshall";
    }

    public Sportshall(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_sports")) {
            player.setFlag("was_sports");
            System.out.println("It smells weird in here. Not the type of weird you find in a boys wardrobe, but rather the type of weird you smell in basements. " +
                    "\nThe smell seems like it's coming from the shaft.\nWhy is it open? There must be a way to get up there. ");
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
                    System.out.println("You move the bench. Luckily, it's not that far away from the shaft. " +
                            "By using your skill, you manage to climb into the shaft.\n" +
                            "While crawling through, you notice the smell getting worse and worse, to the point you almost have to throw up.\n" +
                            "You start to hear a buzzing sound. That's the moment you realize, you made it to the Electricity Room. ");
                }
                return handleRoomChange(player, "electricity room");
            case "leave":
            case "2":
                commands.checkInputCommands("-r", player);
                return "";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
        }
        return "Invalid action.";
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
        if (player.hasFlag("entered_electricity")) {
            exits.put("electricity room", new Exit("electricity room", null));
        }
        return exits;
    }
}