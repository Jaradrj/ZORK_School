package rooms;

import game.*;
import java.util.*;

public class MainEntranceRoom implements Room {
    private boolean lightSwitchChecked = false;

    @Override
    public String getName() {
        return "Main Entrance Hall";
    }

    @Override
    public void enter(Player player) {
        if(!player.hasFlag("was_main")) {
            player.setFlag("was_main");
            System.out.println("You enter the Main Entrance Hall.");
            System.out.println("Your story starts in the main building. There’s not much to find here. You take a look around and notice a few objects. Although it's pretty dark, you can still make out some tables, the school's dull pinboard, a light switch, and chairs—some of which have been knocked over.");
            System.out.println("What do you want to do?");
        }
        System.out.println("Actions:");
        System.out.println("- Turn on the Light");
        System.out.println("- Sit Down at a Table");
        System.out.println("- Examine the Pinboard");

        if (player.hasFlag("half_map_taken")) {
            System.out.println("\nYou can now go to:");
            System.out.println("- Music Room");
            System.out.println("- Teacher Room");
            System.out.println("- IT Room");
            System.out.println("(Use: go to X)");
        }
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        if (action.startsWith("go to ")) {
            return handleRoomChange(player, action.substring(6).trim());
        }

        switch (action) {
            case "turn on the light":
            case "light":
                if (!lightSwitchChecked) {
                    lightSwitchChecked = true;
                    return "You flip the switch. Nothing happens. The power must be out. Maybe you need to restore it elsewhere.";
                }
                return "Still no power. The switch is unresponsive.";

            case "sit down at a table":
            case "sit":
                if (!player.hasFlag("hasReadNote")) {
                    player.setFlag("hasReadNote");
                    return "You sit and notice a folded piece of paper under the table:\n\n" +
                            "\"It doesn't start with the light.\n" +
                            "It never starts with the light.\n\n" +
                            "They say it's just an exercise.\n" +
                            "A test.\n" +
                            "A simulation.\n\n" +
                            "But why doesn't anyone talk about those who are no longer there?\n" +
                            "Why is the room always empty, but the feeling never?\n\n" +
                            "I've seen the door. The real one.\n" +
                            "Not the wooden one. The other one – the living one.\n\n" +
                            "If you're reading this:\n" +
                            "Go. Now.\n" +
                            "Or stay... and become like us\"\n\n" +
                            "~ Leano\n\n" +
                            "Leano B. was known for rebelling against the system. He questioned the disappearances, especially after his friend vanished. One day, Klara—the class leader—told everyone he'd been expelled. No one's heard from him since.";
                }
                return "You’ve already read the note. There's nothing else under the table.";

            case "examine the pinboard":
            case "pinboard":
                if (!player.hasFlag("half_map_taken")) {
                    player.setFlag("half_map_taken");
                    return "Among the generic school announcements, you find something useful: half of a torn school map. New rooms unlocked!\n(You can now go to: Music Room, Teacher Room, IT Room)";
                }
                return "You already took the half map from the pinboard.";

            default:
                return "Invalid action. Try 'turn on the light', 'sit down at a table', 'examine the pinboard', or 'go to X'.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        if (!player.hasFlag("half_map_taken")) {
            return "You don't know where to go yet. Find a map first.";
        }
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName.toLowerCase())) {
            player.setCurrentRoom(roomName);
            return "You enter the " + roomName + ".";
        } else {
            return "There's no room called '" + roomName + "'.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("half_map_taken")) {
            exits.put("music room", new Exit("Music Room", null));
            exits.put("teacher room", new Exit("Teacher Room", null));
            exits.put("it room", new Exit("IT Room", null));
        }
        return exits;
    }
}