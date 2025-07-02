package rooms;

import game.*;
import java.util.*;

public class TeacherRoom implements Room {

    @Override
    public String getName() {
        return "Teacher Room";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the Teacher Room.");
        Room lastRoom = player.getLastRoom();

        boolean sawTeacherLeave = player.hasFlag("saw_teacher_leave");
        boolean followedTeacher = player.hasFlag("has_followed_teacher");
        boolean teacherPresent = !sawTeacherLeave && !followedTeacher;
        boolean lightsOn = Objects.equals(lastRoom.getName(), "Main Entrance Hall");

        System.out.println(lightsOn ? "The lights are on, illuminating the room." : "The room is dark.");

        if (teacherPresent) {
            System.out.println("A woman sits at the desk, sipping something from a steaming mug. She looks a lot like Mrs. Hamps, your school psychologist.\nPoor woman, must have been working really hard helping all the missing students' friends and family.");
            System.out.println("Actions:");
            System.out.println("- Talk to Her");
            System.out.println("- Leave");
        } else if (sawTeacherLeave && !followedTeacher) {
            System.out.println("You see a faint silhouette disappearing into the Garage. Someone just left. Perhaps you should follow? Or stay safe?");
            System.out.println("Actions:");
            System.out.println("- Follow Her");
            System.out.println("- Leave");
        } else {
            if(!player.hasFlag("was_teacher_room")) {
                player.setFlag("was_teacher_room");
                System.out.println("The room is empty. A hot cup sits on the table. A laptop screen glows faintly. Papers are scattered all over the Head Teacher’s desk. Some were also tossed in the trash bin. A science award diploma is proudly displayed. Next to it, you see a Flashlight.");
            }
            System.out.println("Actions:");
            if (!player.hasFlag("coffee_taken")) System.out.println("- Drink Coffee");
            if (!player.hasFlag("coffee_taken") && !player.hasFlag("read_email")) System.out.println("- Use Laptop");
            if (!player.hasFlag("found_trash_id")) System.out.println("- Search Trash Bin");
            if (!player.hasFlag("flashlight_taken")) System.out.println("- Take Flashlight");
            System.out.println("- Leave");
        }
    }

    @Override
    public String performAction(Player player, String action) {
        action = action.toLowerCase().trim();

        switch (action) {
            case "talk":
            case "talk to her":
                if (!player.hasFlag("saw_teacher_leave") && !player.hasFlag("has_followed_teacher")) {
                    player.setFlag("game_ended");
                    return """
                        She turns slowly, her eyes glinting in the dim light.
                        'You found me... I didn’t think anyone would get this far.'
                        A pause. The room feels colder now.
                        'This place — it was never meant for you. Or anyone. We tried to bury it, erase it.'
                        She looks past you, as if seeing something distant. Or remembering.
                        'I stayed to make sure no one would open the door again. But now it's too late.'
                        Her voice drops to a whisper. 'They’re already after us.'
                        The ground trembles. Lights flicker. 
                        'I’m sorry.'
                        Everything fades.
                        GAME OVER.
                    """;
                }
                return "There’s no one here to talk to.";

            case "follow":
            case "follow her":
                if (player.hasFlag("saw_teacher_leave") && !player.hasFlag("has_followed_teacher")) {
                    player.setFlag("has_followed_teacher");
                    handleRoomChange(player, "Garage");
                    return "You quietly follow her through the dim hallway, into the Garage.";
                }
                return "There's no one to follow.";

            case "drink coffee":
            case "coffee":
                if (!player.hasFlag("coffee_taken")) {
                    player.setFlag("coffee_taken");
                    return "You take a sip... and accidentally spill it all over the laptop. It's fried. Whatever was on it is lost.";
                }
                return "The cup is empty.";

            case "use laptop":
            case "laptop":
                if (player.hasFlag("coffee_taken")) {
                    return "The laptop is short-circuited. Nothing works anymore.";
                } else if (!player.hasFlag("read_email")) {
                    player.setFlag("read_email");
                    return "You open the laptop. A strange draft email catches your eye:\n\n" +
                            "\"To: ???@district.edu\nSubject: MindScale Test Distribution\nMessage: 'Make sure Room 302 is ventilated this time. The last trial was... messy. \nAlso, please print out the test attached.'\"\n\n" +
                            "Disturbing...\nYou unlocked a new Room: the Printer Room.";
                } else {
                    return "There’s nothing new on the laptop.";
                }

            case "search trash":
            case "trash":
            case "search trash bin":
                if (!player.hasFlag("found_trash_id")) {
                    player.setFlag("found_trash_id");
                    player.addItem("Student ID");
                    return "You rummage through the bin and find a half-burned Student ID card. The name on it is barely readable: Klara Price. It's starting to get weird now.\nNew room unlocked! This ID will help you enter the Secretary.";
                }
                return "You’ve already searched the trash.";

            case "take flashlight":
            case "flashlight":
                if (!player.hasFlag("flashlight_taken")) {
                    player.setFlag("flashlight_taken");
                    player.addItem("Flashlight");
                    return "You pick up a sturdy flashlight. Might come in handy.";
                }
                return "You already took the flashlight.";

            case "leave":
                return "You decide to leave. Where do you want to go? (Use: go to X)";

            default:
                if (action.startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName.toLowerCase())) {
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
        exits.put("main entrance hall", new Exit("Main Entrance Hall", null));
        exits.put("music room", new Exit("Music Room", null));
        exits.put("it room", new Exit("IT Room", null));

        if (player.hasFlag("has_followed_teacher")) {
            exits.put("garage", new Exit("Garage", null));
        }
        if (player.hasFlag("read_email")) {
            exits.put("printer room", new Exit("Printer Room", null));
        }
        if (player.hasFlag("found_trash_id")) {
            exits.put("secretary", new Exit("Secretary", null));
        }
        return exits;
    }
}
