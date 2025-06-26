package rooms;

import game.*;
import java.util.*;

public class MainEntranceRoom implements Room {
    private boolean hasHalfMap = false;
    private boolean lightSwitchChecked = false;

    @Override
    public String getName() {
        return "Main Entrance Hall";
    }

    @Override
    public void enter(Player player) {
        System.out.printf("Your story starts in the main building. " +
                        "There’s not much to find here. You take a " +
                        "look around the place and notice a few objects. " +
                        "Although it's pretty dark in here, you see a few tables, " +
                        "the School's boring Pinboard, a light switch and chairs," +
                        " some of which seem to have been knocked over. " +
                        "What’s your first step? " +
                        "Available actions:\n " +
                        "1. Try to turn on the light\n" +
                        "2. Sit down at a table\n" +
                        "3. Examine the pinboard\n");

        if (player.hasFlag("hasHalfMap")) {
            System.out.println("\nYou can now go to:");
            System.out.println("- Music Room");
            System.out.println("- Teacher Room");
            System.out.println("- IT Room");
            System.out.println("(Use 'go to X' to enter a room)");
        }
    }

    @Override
    public String performAction(Player player, String action) {
        if (action.toLowerCase().startsWith("go to ")) {
            return handleRoomChange(player, action.substring(6).trim());
        }

        switch (action.toLowerCase()) {
            case "turn on the light":
            case "light":
            case "1":
                if (!lightSwitchChecked) {
                    lightSwitchChecked = true;
                    return "The lights don't work. Maybe you need a flashlight?";
                }
                return "The power is still out.";

            case "sit down at a table":
            case "sit":
            case "2":
                if (!player.hasFlag("hasNote")) {
                    player.setFlag("hasNote");
                    return "You find a note under the table: '\n\n\"It doesn't start with the light.\n" +
                            "It never starts with the light.\n" +
                            "\n" +
                            "They say it's just an exercise.\n" +
                            "A test.\n" +
                            "A simulation.\n" +
                            "\n" +
                            "But why doesn't anyone talk about those who are no longer there?\n" +
                            "Why is the room always empty, but the feeling never?\n" +
                            "\n" +
                            "I've seen the door. The real one.\n" +
                            "Not the wooden one. The other one - the living one.\n" +
                            "\n" +
                            "If you're reading this:\n" +
                            "Go. Now.\n" +
                            "Or stay... and become like us\"`\n" +
                            "~ Leano\n\n" +
                            "Leano B. was known for his rebellation against the system. He was the first to" +
                            "question the student's disappearing, especially since his friend vanished." +
                            "One day, we were told by our class leader Klara, that he was kicked out of school. " +
                            "Haven't heard from him ever since.";
                }
                return "There's nothing else under the table.";

            case "examine the pinboard":
            case "pinboard":
            case "3":
                if (!hasHalfMap) {
                    hasHalfMap = true;
                    player.setFlag("hasHalfMap");
                    return "You take the torn half of the school map from the pinboard. New rooms unlocked! (Music Room, Teacher Room, IT Room)";
                }
                return "The pinboard is empty.";

            default:
                return "Invalid action. Try 'turn on the light', 'sit down at a table', 'examine the pinboard', or 'go to X'.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        if (!player.hasFlag("hasHalfMap")) {
            return "You don't know where to go yet. Find the map first!";
        }
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
        if (player.hasFlag("hasHalfMap")) {
            exits.put("music room", new Exit("Music Room", null));
            exits.put("teacher room", new Exit("Teacher Room", null));
            exits.put("it room", new Exit("IT Room", null));
        }
        return exits;
    }
}