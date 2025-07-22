package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.controller.UIGameController;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIMusicRoom implements UIRoom {

    @Override
    public String getName() {
        return "music room";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_music")) {
            player.setFlag("was_music");
            text.append("The music stands are all in one row, creepy.\n")
                    .append("Some notes were left behind. Suddenly, you notice a cracked sound,\nalmost impossible to notice.\n")
                    .append("You try to identity the source and see a cassette next to the violin,\nwhich is part of the instrument collection.");
        } else {
            text.append("You enter the Music Room\n");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        actions.add("Check out Instruments");
        actions.add("Watch the notes");
        actions.add("Listen to the cassette");
        actions.add("leave");

        return actions;
}

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "check out instruments":
                if (!player.hasFlag("has_checked_instruments")) {
                    player.setFlag("has_checked_instruments");
                    result.append("\nThe collection of instruments on the stage looks\nas if it had been abandoned years ago.\n")
                            .append("This has been the standard image of this school ever\nsince the state cut our funding. Sad story...");
                } else {
                    result.append("\nYou already checked out the instruments.");
                }
                break;

            case "watch the notes":
                if (!player.hasFlag("watched_song_notes")) {
                    player.setFlag("watched_song_notes");
                    SoundPlayer.playSound("/sounds/MusicRoom.wav", 10000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("\nAmong the faded compositions lies a single, timeworn sheet of paper.\n")
                            .append("Its edges are torn, the ink faded almost to invisibility.\n")
                            .append("It appears to be a song… surprisingly enthusiastic, and all about food.\n\n")
                            .append("[Song Lyrics – Title: “Symphony of the Feast”]\n\n")
                            .append("    Verse 1:\n")
                            .append("    In the pantry of my dreams, where the cheese wheels roll,\n")
                            .append("    And the bread sings soft in a buttery soul,\n")
                            .append("    I stir my hopes in a bubbling stew,\n")
                            .append("    Every flavor a note, every spice something new.\n\n")
                            .append("I didn't expect to find something happy today, this is it.\nYou unlocked a new Room, the Cafeteria.");
                } else {
                    result.append("\nYou already took a look at the song notes.\nBesides, are you really sure you want to touch this\nold piece of paper again?");
                }
                break;

            case "listen to the cassette":
                if (!player.hasFlag("listened_to_cassette")) {
                    result.append("\nYou get closer to the cassette, step by step\nThe message you're hearing becomes clearer...\nBut then, suddenly the cassette shuts down. No battery.\n");
                    if (player.hasFlag("flashlight_taken")) {
                        player.setFlag("listened_to_cassette");
                        result.append("Luckily, you have your flashlight. The batteries fit the same.\nAfter inserting your flashlight's batteries into the cassette,\nthe message continues:\n\n");
                        SoundPlayer.playSound("/sounds/StartCassette.wav", 12000, 500, outputArea, UIGameController.getGuiInstance(), false);
                        SoundPlayer.playSound("/sounds/Clara.wav", 18000, 0, outputArea, UIGameController.getGuiInstance(), false);
                        result.append("'I'm sorry. For the last three years I've tried everything\nto become the best, to help my family and myself.\n")
                                .append("I had to keep my reputation up and now...\nThey are gone. I killed them.\n")
                                .append("And I'll be next... \n\n")
                                .append("This voice reminds me of somebody, but I can't figure out who.\nSomeone from the band? I have to stop this!");
                    } else if(player.hasFlag("entered_electricity")){
                        result.append("Sadly your flashlight died. You cannot use the batteries anymore.\n");
                    }
                    else {
                        result.append("You don't have any batteries. Whatever this cassette wanted to tell you,\nthe truth will remain hidden.");
                    }
                } else {
                    result.append("\nYou already listened to the audio on the cassette.");
                }
                break;

            case "leave":
                return "";

            default:
                result.append("Invalid action.");
                break;
        }

        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
    }

    public String handleRoomChange(Player player, String roomName) {
        UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
        player.setCurrentUIRoom(targetRoom);
        return "";
    }

@Override
public Map<String, Exit> getAvailableExits(Player player) {
    Map<String, Exit> exits = new HashMap<>();
    exits.put("main entrance hall", new Exit("main entrance hall", null));
    if (player.hasFlag("watched_song_notes")) {
        exits.put("cafeteria", new Exit("cafeteria", null));
    }
    exits.put("teacher room", new Exit("teacher room", null));
    return exits;
}
}