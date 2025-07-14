package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.controller.UIGameController;
import ui.game.UIRoomFactory;
import ui.audio.SoundPlayer;
import java.util.*;

public class UIITRoom implements UIRoom {

    private UICommands commands;

    @Override
    public String getName() {
        return "it room";
    }

    public UIITRoom(UICommands commands) {
        this.commands = commands;
    }


    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();

        if (!player.hasFlag("was_it")) {
            player.setFlag("was_it");

            text.append("The ancient computers, which normally sound like airplane turbines,\ndon't make a single sound.")
                    .append(" Only one seems to be running, strangely enough.\n")
                    .append("It shows a weird message, but I can't decipher it, not from here.\n")
                    .append("Somehow I have the feeling that I'm about to be attacked\nfrom behind when I inspect it...\n\n");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();

        if (player.hasFlag("awaiting_memory_run_confirm")) {
            actions.add("Run memory leak");
            actions.add("Cancel memory leak");
            return actions;
        }
        if (!player.hasFlag("inspected_message")) {
            actions.add("Inspect message");
        }
        if (player.hasFlag("turned_on_power") && player.hasFlag("phone_taken") && !player.hasFlag("turned_on_wlan")) {
            actions.add("Turn on Wlan");
        }
        if (player.hasFlag("turned_on_wlan")) {
            actions.add("Call the police");
        }

        actions.add("Leave");
        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "run memory leak":
                if (player.hasFlag("awaiting_memory_run_confirm")) {
                    player.clearFlag("awaiting_memory_run_confirm");
                    player.setFlag("confirmed_memory_leak");

                    String camFeeds = """
            [PROCESSING...]

            Accessing archived camera feeds...

            [CAM_01 - Music Room]
            * A shadow in the corner, not there before.
            * It moves when you're not watching. It leaves the room.

            [CAM_02 - Hallway]
            * Lights flicker. The hallway stretches too far.
            * You can hear footsteps through the camera, but no person was seen.

            [CAM_03 - Teacher Room]
            * Two people arguing. Then, they both walk out.\nAfter some minutes, only one returns.

            >> Feed lost. Signal corrupted.

            [RETURNING TO PROMPT...]

            I should check the Teacher's Room...
            """;
                    result.append(camFeeds);
                    outputArea.setText(outputArea.getText() + "\n\n" + camFeeds);
                    result.append("Camera feeds displayed.");
                    SoundPlayer.playSound("/sounds/Camera.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else {
                    result.append("No memory leak is waiting to be confirmed.");
                }
                break;

            case "cancel memory leak":
                if (player.hasFlag("awaiting_memory_run_confirm")) {
                    player.clearFlag("awaiting_memory_run_confirm");
                    result.append("You decide not to run the memory leak sequence.");
                    SoundPlayer.playSound("/sounds/ShuttingDownPC.wav", 0, 2000, outputArea, UIGameController.getGuiInstance(), true);
                } else {
                    result.append("There is nothing to cancel.");
                }
                break;
            case "1":
            case "inspect":
            case "inspect message":
                if (!player.hasFlag("inspected_message")) {
                    player.setFlag("inspected_message");
                    player.setFlag("ran_memory_leak");
                    String logo = """
                    [ACCESSING TERMINAL...]
                    
                       ███████╗██╗███╗   ██╗██████╗ ███████╗███╗   ███╗
                       ██╔════╝██║████╗  ██║██╔══██╗██╔════╝████╗ ████║
                       █████╗  ██║██╔██╗ ██║██║  ██║█████╗  ██╔████╔██║
                       ██╔══╝  ██║██║╚██╗██║██║  ██║██╔══╝  ██║╚██╔╝██║
                       ██║     ██║██║ ╚████║██████╔╝███████╗██║ ╚═╝ ██║
                       ╚═╝     ╚═╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝     ╚═╝
                    """;
                    outputArea.setText(logo);
                    String terminalText = """
                    [WARNING: USER PRESENCE DETECTED]
                    [MESSAGE RECEIVED 00:03:66]
                    
                    > YOU SHOULDN’T BE HERE.
                    > THEY’RE LISTENING THROUGH THE WIRES.
                    
                    > █ █ █  ▄▄▄▄▄▄▄▄▄  █ █ █
                    
                    > She never left the music room.
                      Why did you?
                    
                    [SYSTEM ERROR: MEMORY LEAK - RUNNING AUTOMATICALLY...]
                    Would you like to execute [RUN]? (Y/N)
                    """;
                    result.append(logo);
                    result.append(terminalText);
                    outputArea.setText(logo);
                    SoundPlayer.playSound("/sounds/Computer.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    SoundPlayer.playSound("/sounds/Computer.wav", 9500, 0, outputArea, UIGameController.getGuiInstance(), false);
                    SoundPlayer.playSound("/sounds/Computer.wav", 19000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    SoundPlayer.playSound("/sounds/Computer.wav", 28500, 0, outputArea, UIGameController.getGuiInstance(), false);
                    outputArea.setText(outputArea.getText() + terminalText);
                    player.setFlag("awaiting_memory_run_confirm");
                    result.append("Awaiting input: 'Y' to continue...");
                } else {
                    result.append("You've already inspected the message.");
                }
                break;
            case "2":
            case "turn":
            case "turn on wlan":
                if (!player.hasFlag("turned_on_wlan")) {
                    player.setFlag("turned_on_wlan");
                    result.append("You successfully turn on the Wlan. Surprisingly,\nit still works. But for how long?");
                } else {
                    result.append("The Wlan is already on.");
                }
                break;
            case "call":
            case "call the police":
                if (player.hasFlag("police_number_taken") && player.hasFlag("turned_on_wlan")) {
                    Endings.happyEnding(player);
                    result.append("You don't hesitate and grab the note with the\npolice's number out of your backpack. Calling now...");
                    SoundPlayer.playSound("/audio/HappyEnding.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                } else if (!player.hasFlag("turned_on_wlan")) {
                    result.append("You need to turn on the Wlan before making a call.");
                } else {
                    result.append("What's the Police's number again?\nI think I saw a note in the Secretary.");
                }
                break;
            case "leave":
                commands.checkInputCommands("-r", player, outputArea);
                return "";
            default:
                if (lowerAction.startsWith("go to ")) {
                    String roomChangeResult = handleRoomChange(player, lowerAction.substring(6).trim());
                    result.append(roomChangeResult);
                } else {
                    result.append("Invalid action.");
                }
                break;
        }
        outputArea.setText(outputArea.getText() + "\n\n" + result.toString());
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("main entrance hall", new Exit("main entrance hall", null));
        if (player.hasFlag("watched_song_notes")) {
            exits.put("cafeteria", new Exit("cafeteria", null));
        }
        if (player.hasFlag("ran_memory_leak")) {
            exits.put("teacher room", new Exit("teacher room", null));
        }
        return exits;
    }
}