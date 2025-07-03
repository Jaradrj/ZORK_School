package rooms;

import game.*;

import java.util.*;

public class TeacherRoom implements Room {

    private Endings ending;

    @Override
    public String getName() {
        return "teacher room";
    }

    @Override
    public void enter(Player player) {
        Room lastRoom = player.getLastRoom();
        boolean wasHere = player.hasFlag("was_teacher_room");

        boolean sawTeacherLeave = player.hasFlag("saw_teacher_leave");
        boolean followedTeacher = player.hasFlag("has_followed_teacher");
        boolean teacherPresent = !sawTeacherLeave && !followedTeacher && lastRoom.getName().equals("main entrance hall");
        boolean lightsOn = Objects.equals(lastRoom.getName(), "main entrance hall");
        boolean decideToLeave = player.hasFlag("leaving");

        if (!wasHere) {
            System.out.println("You enter the Teacher Room.");
            System.out.println(lightsOn ? "Some candles are lit, illuminating the room." : "The room is dark.");
            player.setFlag("was_teacher_room");
        }


        if (teacherPresent) {
            System.out.println("A woman sits at the desk, sipping something from a steaming mug. She looks a lot like Mrs. Hamps, your school psychologist.\nPoor woman, must have been working really hard helping all the missing students' friends and family.");
            System.out.println("Do you want to talk to her? (Y/N)");
            System.out.println("- Leave");
            return;
        }

        if (!followedTeacher && !wasHere && !lastRoom.getName().equals("teacher room")) {
            System.out.println("You see a faint silhouette disappearing into the Garage. Someone just left. Perhaps you should follow? Or stay safe?");
        }

         else if (wasHere && !decideToLeave && ( player.hasFlag("coffee_taken") || player.hasFlag("found_trash_id") || player.hasFlag("read_email") || player.hasFlag("flashlight_taken"))) {
            System.out.println("The room is empty. A hot cup sits on the table. A laptop screen glows faintly. Papers are scattered all over the Head Teacher’s desk. Some were also tossed in the trash bin. A science award diploma is proudly displayed. Next to it, you see a Flashlight.");
        }

        if(!decideToLeave) {
            System.out.println("Actions:");
        }

        if (!followedTeacher && !player.hasFlag("teacher_room_loot_ready") && !decideToLeave) {
            player.setFlag("saw_teacher_leave");
            System.out.println("- Follow Her");
            System.out.println("- Stay hidden");
            System.out.println("- Leave");
        }

        if (player.hasFlag("teacher_room_loot_ready") && !decideToLeave) {
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
            case "y":
            case "talk to her":
                if (!player.hasFlag("saw_teacher_leave") && !player.hasFlag("has_followed_teacher")) {
                    ending.teacherEnding(player);
                }
                return "There’s no one here to talk to.";

            case "n":
                player.setLastRoom(this);
                return "You choose to take a look around the room, as you slowly notice that the teacher stands up and starts to slowly walk away. " +
                        "Should you follow her?";
            case "follow":
            case "follow her":
                if (player.hasFlag("saw_teacher_leave") && !player.hasFlag("has_followed_teacher")) {
                    player.setFlag("has_followed_teacher");
                    handleRoomChange(player, "garage");
                    return "You quietly follow her through the dim hallway, into the Garage.";
                }
                return "There's no one to follow.";

            case "stay hidden":
                player.setFlag("teacher_room_loot_ready");
                return "You decide to stay hidden in hope to find something in the teacher room. You wait a couple of minutes and slowly start to look around the room. " +
                        "What would you like to do?";

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
                    return "\nThe laptop is short-circuited. Nothing works anymore.";
                } else if (!player.hasFlag("read_email")) {
                    player.setFlag("read_email");
                    return "\nYou open the laptop. A strange draft email catches your eye:\n\n" +
                            "\"To: ???@district.edu\nSubject: MindScale Test Distribution\nMessage: 'Make sure Room 302 is ventilated this time. The last trial was... messy. \nAlso, please print out the test attached.'\"\n\n" +
                            "Disturbing...\nYou unlocked a new Room: the Printer Room.";
                } else {
                    return "\nThere’s nothing new on the laptop.";
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
                player.setFlag("leaving");
                System.out.println("You decide to leave. Where do you want to go? (Use: go to X)");
                System.out.println("You can now go to: ");
                System.out.println("- Main Entrance Hall");
                System.out.println("- IT Room");
                System.out.println("- Music Room");
                if (player.hasFlag("found_trash_id")) {
                    System.out.println("- Secretary");
                }
                if (player.hasFlag("read_email")) {
                    System.out.println("- Printer Room");
                }
                return "";

            default:
                if (action.startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "";
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
