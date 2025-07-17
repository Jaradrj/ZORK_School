package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import ui.game.UICommands;
import ui.game.UIRoom;
import console.game.*;
import ui.game.UIRoomFactory;

import java.io.IOException;
import java.util.*;

public class UIMainEntranceRoom implements UIRoom {

    private final UICommands commands;
    private TextPrinter printer;

    public UIMainEntranceRoom(UICommands commands, TextPrinter printer) {
        this.commands = commands;
        this.printer = printer;
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
                    String text = "You flip the switch. Nothing happens. The power must be out.\nMaybe you need to restore it elsewhere.";
                    TypingEffect.typeWithSound(outputArea, text, UIGameController.getGuiInstance(), null);
                } else {
                    TypingEffect.typeWithSound(outputArea, "Still no power. The switch is unresponsive.", UIGameController.getGuiInstance(), null);
                }
                return "";
            case "sit down at a table":
                outputArea.invalidate();

                if (!player.hasFlag("hasReadNote")) {
                    player.setFlag("hasReadNote");

                    SoundPlayer.playSound("/sounds/Leandro.wav", 2000, 0, outputArea, UIGameController.getGuiInstance(), false);

                    String note = """
                            You sit and notice a folded piece of paper under the table:
                            
                            "It doesn't start with the light.
                            It never starts with the light.
                            
                            They say it's just an exercise.
                            A test.
                            A simulation.
                            
                            But why doesn't anyone talk about those who are no longer there?
                            Why is the room always empty, but the feeling never?
                            
                            I've seen the door. The real one.
                            Not the wooden one. The other one – the living one.
                            
                            If you're reading this:
                            Go. Now.
                            Or stay... and become like us"
                            """;

                    printer.textPrinter(note, outputArea);
                    SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            String leandroInfo = """
                                    
                                    Leandro B. was known for rebelling against the system.
                                    He questioned the disappearances, especially after his friend vanished.
                                    One day, Klara—the class leader—told everyone he'd been expelled.
                                    No one's heard from him since.
                                    """;

                            TypingEffect.typeWithSound(outputArea, leandroInfo, UIGameController.getGuiInstance(), null);
                        }
                    }, 10000);

                } else {
                    TypingEffect.typeWithSound(outputArea, "You've already read the note. There's nothing else under the table.", UIGameController.getGuiInstance(), null);
                }

                return "";
            case "examine the pinboard":
                if (!player.hasFlag("half_map_taken")) {
                    player.setFlag("half_map_taken");
                    player.getInventory().addItem("Schools half map");

                    String text = "Among the generic school announcements, you find something useful:\n" +
                            "half of a torn school map.\n\n" +
                            "New rooms unlocked!\n" +
                            "(You can now go to: Music Room, Teacher Room, IT Room)";
                    TypingEffect.typeWithSound(outputArea, text, UIGameController.getGuiInstance(), null);
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 3500, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else {
                    TypingEffect.typeWithSound(outputArea, "You already took the half map from the pinboard.", UIGameController.getGuiInstance(), null);
                }
                return "";
            case "leave":
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
            return "";
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
