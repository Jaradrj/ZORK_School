package rooms;

import game.*;

import java.util.*;

public class Garage implements Room {

    @Override
    public String getName() {
        return "garage";
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_garage")) {
            player.setFlag("was_garage");

            if (!player.hasFlag("keys_taken")) {
                System.out.print("While watching ");
                System.out.print(player.hasFlag("knows_teacher_name") ? "Mrs. Hamps " : "the woman ");
                System.out.println("go to her car, you're hiding behind a big pillar.\n" +
                        "Suddenly, she stops, tries to grad something from her pocket,\n" +
                        "turns around and is now moving towards you. You manage to keep hidden. It seems that she forgot something.\n" +
                        "What does she have in that car?");
            }
        }
        System.out.println("Actions:");

        if (!player.hasFlag("keys_taken")) System.out.println("- Break into her car");
        System.out.println("- Return to teacher room");
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        switch (action) {
            case "1":
            case "break":
            case "break into her car":
                if (!player.hasFlag("keys_taken")) {
                    player.setFlag("keys_taken");
                    player.getInventory().addItem("keys (to what?)");
                    System.out.println("\nYou need to know what she's hiding. Thank god she didn't suspect anyone to sneak around and left her car unlocked.\n" +
                            "Easy game for you to get inside and start searching quick. You don't find much, just some mints, money, a coke and.. keys!\n" +
                            "But what are they for?");
                } else {
                    System.out.println("\bnYou already broke in to her car, time to leave!");
                }
                return "";
            case "return":
            case "return to teacher room":
                player.setFlag("teacher_room_loot_ready");
                return handleRoomChange(player, "teacher room");

            default:
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
        exits.put("teacher room", new Exit("teacher room", null));
        return exits;
    }
}