package ui.rooms;


import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextBox;
import ui.audio.SoundPlayer;
import ui.controller.UIGameController;
import console.game.*;
import ui.game.UICommands;
import ui.game.UIEndings;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.*;

public class UITeacherRoom implements UIRoom {

    private UIEndings ending;
    private UICommands commands;
    private MultiWindowTextGUI gui;

    public UITeacherRoom(UIGameController controller, UICommands commands, MultiWindowTextGUI gui) {
        this.gui = gui;
        this.ending = new UIEndings(controller, gui);
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "teacher room";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();

        UIRoom lastRoom = player.getLastUIRoom();
        boolean sawTeacherLeave = player.hasFlag("saw_teacher_leave");
        boolean followedTeacher = player.hasFlag("has_followed_teacher");
        boolean teacherPresent = !sawTeacherLeave && !followedTeacher && lastRoom.getName().equals("main entrance hall");
        boolean lightsOn = Objects.equals(lastRoom.getName(), "main entrance hall");

        if (!player.hasFlag("was_teacher_room")) {
            text.append(lightsOn ? "Some candles are lit, illuminating the room.\n" : "The room is dark.\n");
            player.setFlag("was_teacher_room");
        }


        if (teacherPresent) {
            player.setFlag("knows_teacher_name");
            player.setFlag("await_choice_talk");
            text.append("A woman sits at the desk, sipping something from a steaming mug. ")
                    .append("She looks a lot like Mrs. Hamps, your school psychologist.\n")
                    .append("Poor woman, must have been working really hard helping all the missing students' friends and family.\n")
                    .append("Do you want to talk to her? (Y/N)\n");
        }

        if (!followedTeacher && !player.hasFlag("teacher_room_loot_ready")) {
            if (!lastRoom.getName().equalsIgnoreCase("teacher room") && !player.hasFlag("saw_teacher_leave") && !player.hasFlag("knows_teacher_name")) {
                text.append("You see a faint silhouette disappearing into the Garage. Someone just left.\n")
                        .append("Perhaps you should follow? Or stay safe?");
                player.setFlag("saw_teacher_leave");
            }
            if (player.hasFlag("leaving")) return "";
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {

        UIRoom lastRoom = player.getLastUIRoom();
        boolean sawTeacherLeave = player.hasFlag("saw_teacher_leave");
        boolean followedTeacher = player.hasFlag("has_followed_teacher");
        boolean teacherPresent = !sawTeacherLeave && !followedTeacher && lastRoom.getName().equals("main entrance hall");

        List<String> actions = new ArrayList<>();
        if (player.hasFlag("await_choice_talk")) {
            actions.add("Yes");
            actions.add("No");
            return actions;
        }
        if (!teacherPresent && player.hasFlag("had_follow_decision") && (player.hasFlag("teacher_room_loot_ready") || lastRoom.getName().equalsIgnoreCase("garage") ||
                lastRoom.getName().equalsIgnoreCase("music room") || lastRoom.getName().equalsIgnoreCase("it room") || lastRoom.getName().equalsIgnoreCase("main entrance hall") ||
                lastRoom.getName().equalsIgnoreCase("printer room") || lastRoom.getName().equalsIgnoreCase("secretary"))) {
            if (!player.hasFlag("coffee_taken")) actions.add("Drink Coffee");
            if (!player.hasFlag("coffee_taken") && !player.hasFlag("read_email")) actions.add("Use Laptop");
            if (!player.hasFlag("found_trash_id")) actions.add("Search Trash Bin");
            if (!player.hasFlag("flashlight_taken")) actions.add("Take Flashlight");
        }

        if (!followedTeacher && !player.hasFlag("teacher_room_loot_ready")) {
            actions.add("Follow Her");
            actions.add("Stay hidden");
            player.setFlag("had_follow_decision");
        }

        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {

        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "follow":
            case "follow her":
                if (player.hasFlag("saw_teacher_leave") && !player.hasFlag("has_followed_teacher")) {
                    player.setFlag("has_followed_teacher");
                    String roomChangeMsg = handleRoomChange(player, "garage");
                    return player.getCurrentUIRoom().enter(player);
                } else {
                    result.append("There's no one to follow.");
                }
                break;

            case "stay hidden":
                player.setFlag("teacher_room_loot_ready");
                result.append("You decide to stay hidden in hope to find something in the teacher room.\n");
                result.append("You wait a couple of minutes and slowly start to look around the room. ");
                result.append("It's empty.\n");
                result.append("A hot cup sits on the table. A laptop screen glows faintly.\n");
                result.append("What would you like to do?");

                break;

            case "drink coffee":
            case "coffee":
                if (!player.hasFlag("coffee_taken")) {
                    player.setFlag("coffee_taken");
                    SoundPlayer.playSound("/sounds/DrinkCoffee.wav", 1000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("You take a sip... and accidentally spill it all\nover the laptop. It's fried. Whatever was on it is lost.");
                }
                result.append("The cup is empty.");
                break;

            case "use laptop":
            case "laptop":
                if (player.hasFlag("coffee_taken")) {
                    result.append("\nThe laptop is short-circuited. Nothing works anymore.");
                } else if (!player.hasFlag("read_email")) {
                    player.setFlag("read_email");
                    SoundPlayer.playSound("/sounds/ReceiveEmail.wav", 4000, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("\nYou open the laptop. A strange draft email catches your eye:\n\n");
                    result.append("\"To: ???@district.edu\n");
                    result.append("Subject: MindScale Test Distribution\n");
                    result.append("Message: 'Make sure Room 302 is ventilated this time.\nThe last trial was... messy. \n");
                    result.append("Also, please print out the test attached.'\"\n\n");
                    result.append("Disturbing...\n");
                    result.append("You unlocked a new Room: the Printer Room.");

                } else {
                    result.append("\nThere’s nothing new on the laptop.");
                }
                break;

            case "search trash":
            case "trash":
            case "search trash bin":
                if (!player.hasFlag("found_trash_id")) {
                    player.setFlag("found_trash_id");
                    SoundPlayer.playSound("/sounds/SearchTrashBin.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    player.getInventory().addItem("Student ID");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("You rummage through the bin and find a half-burned Student ID card.\nThe name on it is barely readable: ")
                            .append("\nKlara Price. It's starting to get weird now.\nNew room unlocked! This ID will help you enter the Secretary.");
                } else {
                    result.append("You’ve already searched the trash.");
                }
                break;

            case "take flashlight":
            case "flashlight":
                if (!player.hasFlag("flashlight_taken")) {
                    player.setFlag("flashlight_taken");
                    player.getInventory().addItem("Flashlight");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("You pick up a sturdy flashlight. Might come in handy.");
                } else {
                    result.append("You already took the flashlight.");
                }
                break;

            case "y":
            case "yes":
            case "talk to her":
                if (player.hasFlag("await_choice_talk")) {
                    player.clearFlag("await_choice_talk");
                    ending.teacherEnding(outputArea);
                } else {
                    result.append("There’s no one here to talk to.");
                }
                break;

            case "n":
            case "no":
                player.clearFlag("await_choice_talk");
                player.setLastUIRoom(this);
                player.setFlag("saw_teacher_leave");
                result.append("You choose to take a look around the room, as you slowly\nnotice that the teacher stands up and starts to slowly walk away. ")
                        .append("\nShould you follow her?");
                SoundPlayer.playSound("/sounds/Footsteps.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                break;

            case "leave":
                player.setFlag("leaving");
                commands.checkInputCommands("-r", player, outputArea);
                return "";

            default:
                result.append("Invalid action.");
                break;
        }

        outputArea.setText(outputArea.getText() + "\n\n" + result);
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
        exits.put("music room", new Exit("music room", null));
        exits.put("it room", new Exit("it room", null));

        if (player.hasFlag("has_followed_teacher")) {
            exits.put("garage", new Exit("garage", null));
        }
        if (player.hasFlag("read_email")) {
            exits.put("printer room", new Exit("printer room", null));
        }
        if (player.hasFlag("found_trash_id")) {
            exits.put("secretary", new Exit("secretary", null));
        }
        return exits;
    }
}