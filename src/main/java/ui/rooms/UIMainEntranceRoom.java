package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.controller.UIGameController;
import ui.game.UICommands;
import ui.game.UIRoom;
import console.game.*;
import ui.game.UIRoomFactory;

import java.io.IOException;
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
    public List<String> getAvailableActions(Player player   ) {
        List<String> actions = new ArrayList<>();
        if (!player.hasFlag("turned_on_power")) {
            actions.add("Turn on the Light");
        }
        actions.add("Sit Down at a Table");
        actions.add("Examine the Pinboard");
        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        action = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (action) {
            case "turn on the light":
                SoundPlayer.playSound("/sounds/Lightswitch.wav", 0, 500, outputArea, UIGameController.getGuiInstance(), true);
                if (!player.hasFlag("lights_tried")) {
                    player.setFlag("lights_tried");
                    result.append("You flip the switch. Nothing happens. The power must be out.\nMaybe you need to restore it elsewhere.");
                } else {
                    result.append("Still no power. The switch is unresponsive.");
                }
                break;
            case "sit down at a table":
                if (!player.hasFlag("hasReadNote")) {
                    player.setFlag("hasReadNote");
                    String text = "You sit and notice a folded piece of paper under the table:\n\n" +
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
                                  "Leano B. was known for rebelling against the system.\n" +
                                  "He questioned the disappearances, especially after his friend vanished.\n" +
                                  "One day, Klara—the class leader—told everyone he'd been expelled.\n" +
                                  "No one's heard from him since.";
                    TypingEffect.typeWithSound(outputArea, text, UIGameController.getGuiInstance(), null);
                    new Thread(() -> {
                        try {
                            Thread.sleep(2800);
                            SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    TypingEffect.typeWithSound(outputArea, "You've already read the note. There's nothing else under the table.", UIGameController.getGuiInstance(), null);
                }
                return "";
            case "examine the pinboard":
                if (!player.hasFlag("half_map_taken")) {
                    player.setFlag("half_map_taken");
                    player.getInventory().addItem("Schools half map");

                    result.append("Among the generic school announcements, you find something useful:\n")
                            .append("half of a torn school map.\n\n")
                            .append("New rooms unlocked!\n")
                            .append("(You can now go to: Music Room, Teacher Room, IT Room)");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 3500, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else {
                    result.append("You already took the half map from the pinboard.");
                }
                break;
            case "leave":
                commands.checkInputCommands("-r", player, outputArea);
                return "";
            default:
                result.append("Invalid action. Try one of the available buttons.");
                break;
        }
        return result.toString();
    }


    public String handleRoomChange(Player player, String roomName) {
        if (!player.hasFlag("half_map_taken")) {
            return "You don't know where to go yet. Find a map first.";
        }
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
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
