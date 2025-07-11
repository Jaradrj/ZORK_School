package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import ui.audio.TypingEffect;
import ui.game.UICommands;
import ui.game.UIRoom;
import console.game.*;

import java.util.*;

public class UIMainEntranceRoom implements UIRoom {

    private final UICommands commands;

    public UIMainEntranceRoom(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "main entrance hall";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_main")) {
            player.setFlag("was_main");
            text.append("Your story starts in the main building. \nThere’s not much to find here. \nYou take a look around and notice a few objects.\n")
                    .append("Although it's pretty dark, you can still make out some tables, \nthe school's dull pinboard, a light switch,\nand chairs, some of which have been knocked over.\n")
                    .append("What do you want to do?\n");
        } else {
            text.append("You are in the main entrance hall.\n");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (!player.hasFlag("turned_on_power")) {
            actions.add("Turn on the Light");
        }
        actions.add("Sit Down at a Table");
        actions.add("Examine the Pinboard");

        if (player.hasFlag("half_map_taken")) {
            actions.add("Go to Music Room");
            actions.add("Go to Teacher Room");
            actions.add("Go to IT Room");
        }
        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        action = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        if (action.startsWith("go to ")) {
            String roomChangeResult = handleRoomChange(player, action.substring(6).trim());
            result.append(roomChangeResult);
        } else {
            switch (action) {
                case "turn on the light":
                    if (!player.hasFlag("lights_tried")) {
                        player.setFlag("lights_tried");
                        result.append("You flip the switch. Nothing happens. The power must be out. Maybe you need to restore it elsewhere.");
                    } else {
                        result.append("Still no power. The switch is unresponsive.");
                    }
                    break;

                case "sit down at a table":
                    if (!player.hasFlag("hasReadNote")) {
                        player.setFlag("hasReadNote");
                        result.append("You sit and notice a folded piece of paper under the table:\n\n")
                                .append("\"It doesn't start with the light.\n")
                                .append("It never starts with the light.\n\n")
                                .append("They say it's just an exercise.\n")
                                .append("A test.\n")
                                .append("A simulation.\n\n")
                                .append("But why doesn't anyone talk about those who are no longer there?\n")
                                .append("Why is the room always empty, but the feeling never?\n\n")
                                .append("I've seen the door. The real one.\n")
                                .append("Not the wooden one. The other one – the living one.\n\n")
                                .append("If you're reading this:\n")
                                .append("Go. Now.\n")
                                .append("Or stay... and become like us\"\n\n")
                                .append("~ Leano\n\n")
                                .append("Leano B. was known for rebelling against the system. He questioned the disappearances, especially after his friend vanished.\n")
                                .append("One day, Klara—the class leader—told everyone he'd been expelled. No one's heard from him since.");
                    } else {
                        result.append("You’ve already read the note. There's nothing else under the table.");
                    }
                    break;

                case "examine the pinboard":
                    if (!player.hasFlag("half_map_taken")) {
                        player.setFlag("half_map_taken");
                        player.getInventory().addItem("Schools half map");
                        result.append("Among the generic school announcements, you find something useful: half of a torn school map. New rooms unlocked!\n")
                                .append("(You can now go to: Music Room, Teacher Room, IT Room)");
                    } else {
                        result.append("You already took the half map from the pinboard.");
                    }
                    break;

                default:
                    result.append("Invalid action. Try one of the available buttons.");
                    break;
            }
        }
        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        if (!player.hasFlag("half_map_taken")) {
            return "You don't know where to go yet. Find a map first.";
        }
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There's no room called '" + roomName + "'.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("half_map_taken")) {
            exits.put("music room", new Exit("music room", null));
            exits.put("teacher room", new Exit("teacher room", null));
            exits.put("it room", new Exit("it room", null));
        }
        return exits;
    }
}
