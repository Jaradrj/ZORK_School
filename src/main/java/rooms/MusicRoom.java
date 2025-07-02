package rooms;

import game.Player;
import game.Room;
import java.util.HashMap;
import java.util.Map;
import game.Exit;

public class MusicRoom implements Room {

    @Override
    public String getName() {
        return "Music Room";
    }

    @Override
    public void enter(Player player) {
        if(!player.hasFlag("was_music")) {
            player.setFlag("was_music");
            System.out.println("You enter the Music Room.");
            System.out.println("There's only a tiny bit of light reflecting on the drums in the back of the rotten wooden stage. The music stands are all in one row, creepy.\nSome notes were left behind. Suddenly, you notice a cracked sound, almost impossible to notice. You try to identity the source and see a cassette next to the violin, which is part of the instrument collection.");
        }
        System.out.println("Actions:");

        System.out.println("- Check out Instruments");
        System.out.println("- Watch the notes");
        System.out.println("- Listen to the cassette");
        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "check":
            case "check out instruments":
                if (!player.hasFlag("has_checked_instruments")) {
                    player.setFlag("has_checked_instruments");
                    return "The collection of instruments on the stage looks as if it had been abandoned years ago. This has been the standard image of this school ever since the state cut our funding. Sad story...";
                }
                return "You already checked out the instruments.";
            case "2":
            case "watch":
            case "watch the notes":
                if (!player.hasFlag("watched_song_notes")) {
                    player.setFlag("watched_song_notes");
                    return "    Among the faded compositions lies a single, timeworn sheet of paper.\n" +
                            "    Its edges are torn, the ink faded almost to invisibility.\n" +
                            "    It appears to be a song… surprisingly enthusiastic, and all about food.\n" +
                            "\n" +
                            "[Song Lyrics – Title: “Symphony of the Feast”]\n" +
                            "\n" +
                            "    Verse 1:\n" +
                            "    In the pantry of my dreams, where the cheese wheels roll,\n" +
                            "    And the bread sings soft in a buttery soul,\n" +
                            "    I stir my hopes in a bubbling stew,\n" +
                            "    Every flavor a note, every spice something new.\n\n " +
                            "I didn't expect to find something happy today, this is it. You unlocked a new Room, the Cafeteria.";
                }
                return "You already took a look at the song notes. Besides, are you really sure you want to touch this old piece of paper again?";
            case "3":
            case "listen":
            case "listen to the cassette":
                if (!player.hasFlag("listened_to_cassette")) {
                    player.setFlag("listened_to_cassette");
                    return "You get closer to the cassette, step by step. The message you're hearing becomes more and more clear. But then, suddenly the cassette shuts down. No battery.";
                    if (player.hasFlag("flashlight_taken")) {
                        return "Luckily, you have your flashlight. The batteries fit the same. After inserting your flashlight's batteries into the cassette, the message continues:\n\n 'I'm sorry. For the last three years I've tried everything to become the best, to help my family and myself.\nI had to keep my reputation up and now... They are gone. I killed them. And I'll be next... If they ever offer you a drink, don't drink it.\nIt's called Scopolamine, makes you hallucinate, lose your memory. I'm so sorry.'\n\nThis voice reminds me of somebody, but I can't figure out who. Someone from the band? I have to stop this!";
                        player.setFlag("listened_to_cassette");
                    }else if (!player.hasFlag("flashlight_taken")) {
                        return "You don't have any batteries. Whatever this cassette wanted to tell you, the truth will remain hidden.";
                    }
                }
                return "You already listened to the audio on the cassette";
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    public String handleRoomChange(Player player, String roomName) {
            Map<String, Exit> exits = getAvailableExits(player);
            if (exits.containsKey(roomName.toLowerCase())) {
                player.setCurrentRoom(roomName);
                return "You enter the " + roomName + ".";
            } else {
                return "There is no room called '" + roomName + "' here.";
            }
        }

        @Override
        public Map<String, Exit> getAvailableExits (Player player){
            Map<String, Exit> exits = new HashMap<>();
            exits.put("main entrance hall", new Exit("Main Entrance Hall", null));
            if (player.hasFlag("watched_song_notes")) {
                exits.put("cafeteria", new Exit("Cafeteria", null));
            }
            return exits;
        }
    }