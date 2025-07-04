package rooms;

import game.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ITRoom implements Room {

    @Override
    public String getName() {
        return "it room";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the IT Room.");
        if (!player.hasFlag("was_it")) {
            player.setFlag("was_it");

            System.out.println("The ancient computers, which normally sound like airplane turbines, don't make a single sound. Only one seems to be running, strangely enough.\n It shows a weird message, but I can't decipher it, not from here. Somehow I have the feeling that I'm about to be attacked from behind when I inspect it...");
        }

        System.out.println("Actions:");
        if(!player.hasFlag("inspected_message")){
            System.out.println("- Inspect message");
        }
        if (player.hasFlag("turned_on_power") && player.hasFlag("phone_taken"))
            System.out.println("- Turn on Wlan");
        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "inspect":
            case "inspect message":
                if (!player.hasFlag("inspected_message")) {
                    System.out.println("[ACCESSING TERMINAL...]\n" +
                            "\n" +
                            "   ███████╗██╗███╗   ██╗██████╗ ███████╗███╗   ███╗\n" +
                            "   ██╔════╝██║████╗  ██║██╔══██╗██╔════╝████╗ ████║\n" +
                            "   █████╗  ██║██╔██╗ ██║██║  ██║█████╗  ██╔████╔██║\n" +
                            "   ██╔══╝  ██║██║╚██╗██║██║  ██║██╔══╝  ██║╚██╔╝██║\n" +
                            "   ██║     ██║██║ ╚████║██████╔╝███████╗██║ ╚═╝ ██║\n" +
                            "   ╚═╝     ╚═╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝     ╚═╝\n" +
                            "\n" +
                            "   [WARNING: USER PRESENCE DETECTED]\n" +
                            "   [MESSAGE RECEIVED 00:03:66]\n" +
                            "\n" +
                            "   > YOU SHOULDN’T BE HERE.\n" +
                            "   > THEY’RE LISTENING THROUGH THE WIRES.\n" +
                            "\n" +
                            "   > █ █ █  ▄▄▄▄▄▄▄▄▄  █ █ █\n" +
                            "\n" +
                            "   > She never left the music room.\n" +
                            "     Why did you?\n" +
                            "\n" +
                            "   [SYSTEM ERROR: MEMORY LEAK - RUN? Y/N]\n");

                    Scanner scanner = new Scanner(System.in);
                    String response = scanner.nextLine();
                    if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("run")) {
                        player.setFlag("inspected_message");
                        if (player.hasFlag("inspected_message") && !player.hasFlag("ran_memory_leak")) {
                            player.setFlag("ran_memory_leak");
                            return "[PROCESSING...]\n\n" +
                                    "Accessing archived camera feeds...\n" +
                                    "\n" +
                                    "[CAM_01 - Music Room]\n" +
                                    "* A still image. A shadow in the corner, not there before.\n" +
                                    "* It moves when you're not watching. It leaves the room.\n" +
                                    "\n" +
                                    "[CAM_02 - Hallway]\n" +
                                    "* Lights flicker. The hallway stretches too far.\n" +
                                    "* You can hear footsteps through the camera, but no person was seen.\n" +
                                    "\n" +
                                    "[CAM_03 - Teacher Room]\n" +
                                    "* Two people arguing. Then, they both walk out. After some minutes, only one returns.\n" +
                                    "\n" +
                                    ">> Feed lost. Signal corrupted.\n" +
                                    "\n" +
                                    "[RETURNING TO PROMPT...]\n\n\n" +
                                    "I should check the Teacher's Room...";
                        }
                        return "Nothing happens. Maybe you missed something?";
                    } else {
                        return "[PROCESS TERMINATED. RETURNING TO PROMPT...]\n";
                    }
                }
            case "2":
            case "turn":
            case "turn on wlan":
                if (!player.hasFlag("turned_on_wlan")) {
                    player.setFlag("turned_on_wlan");
                    return "You successfully turn on the Wlan. Surprisingly, it still works. But for how long?";
                }
                return "You already turned on the wlan\n\n" +
                        "Actions:\n" +
                        "- Call the police";
            case "call":
            case "call the police":
                if (player.hasFlag("police_number_taken") && player.hasFlag("turned_on_wlan")) {
                    System.out.println("\"You don't hesitate and grab the note with the polices number out of your backpack.");
                    Endings.happyEnding(player);
                    return "";
                } else if (!player.hasFlag("turned_on_wlan")) {
                    return "You need to turn on the Wlan before making a call";
                } else if (!player.hasFlag("police_number_taken")) {
                    return "What's the Police's number again? I think I saw a note in the Secretary.";
                }

            case "leave":
                System.out.println("You decide to leave. (Use: go to x)");
                System.out.println("You can now go to: ");
                System.out.println("- Main Entrance Hall");
                System.out.println("- Cafeteria");
                if (player.hasFlag("ran_memory_leak")) {
                    System.out.println("- Teacher Room");
                }
                return "";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
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
        if (player.hasFlag("watched_song_notes")) {
            exits.put("cafeteria", new Exit("cafeteria", null));
        }
        if (player.hasFlag("ran_memory_leak")) {
            exits.put("teacher room", new Exit("teacher room", null));
        }
        return exits;
    }
}