package rooms;

import game.*;

import java.util.HashMap;
import java.util.Map;

public class ElectricityRoom implements Room {

    @Override
    public String getName() {
        return "Electricity Room";
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_electricity")) {
            player.setFlag("was_electricity");
            System.out.println("You look inside, but all you see is darkness.\n" +
                    "And so it's clear, the only way forward is to jump.\n" +
                    "You pause, thinking about everything that brought you here. The risks, the choices, the distance you've come. And all that just to turn around and leave?\n" +
                    "No. Not after all of this. I mean it's just a jump right?\n" +
                    "You try and climb out of the shaft, barely getting into the right position to just let go and jump. \n" +
                    "\n" +
                    "Just a few seconds later, you feel the hard ground beneath your feet. You tried to land upright, but your legs buckle and you drop to your knees with a sharp jolt of pain. They're sore. Nothing broken, but it hurts.\n" +
                    "\n" +
                    "You stand up and try to figure out, what you can do. At the far end of the room, a green emergency exit light flickers. Hoping to see something useful, you decide to walk toward it.\n" +
                    "\n" +
                    "You manage to take the first few steps, until suddenly, the hard ground meets your knees once more. Out of frustration and the need for light you manage to get up and continue walking, ignoring whatever it was that you stumbled over. \n" +
                    "\n" +
                    "You keep walking, step after step, until you finally reach the light. You glance around, and through the dimness, you make out the shape of the reactor and next to it, the exit." +
                    "While you keep observing your surroundings you notice that weird irony smell once again. You wonder and wonder what it is, but just can't figure it out. It smells like mold and blood or just like death. \n" +
                    "\n" +
                    "Then you remember the weird thing you stumbled over earlier.");

        }
        System.out.println("Actions: ");
        System.out.println(" - Open the door");
        if (!player.hasFlag("body_checked")) {
            System.out.println(" - Inspect unknown object");
        } else {
            System.out.println(" - Inspect bodies");
        }
        if (!player.hasFlag("turned_on_power")) {
            System.out.println(" - Enable radiator");
        }
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "open door":
                if (player.hasFlag("key_taken")) {
                    if (!player.hasFlag("door_opened")) {
                        player.setFlag("door_opened");
                        System.out.println("You successfully open the door.");
                    }
                    return "Where do you want to go? (Use: go to X)";
                } else {
                    return "You try to open the door but rapidly notice that you are missing the one key that might give you your freedom.";
                }
            case "2":
            case "enable radiator":
                if (player.hasFlag("turned_on_power")) {
                    return "You already turned on the power";
                } else {
                    player.setFlag("turned_on_power");
                    System.out.println("You walk over to the radiator.\n" +
                            "It’s not your first time in this room, so you’re not completely lost. Still, in this darkness, fixing the lights will be a challenge. \n" +
                            "You manage to open the switchboard but quickly find yourself disoriented. \n" +
                            "You can’t see a thing, so you have to rely on your memory and senses instead. \n" +
                            "You touch the switches while remembering the course you had in this room a couple of months ago. \n" +
                            "You carefully touch the switches, recalling the IT course you took here a few months ago. The janitor, Toby, had explained the different switches and their functions on the radiator in front of you.\n" +
                            "You try to remember how it used to look, comparing it to what you feel now.\n" +
                            "Wait a minute, you think to yourself. Suddenly you remember the main switch, and where it should be located. You hope and pray that you're correct and decide to pull it. Nothing happens, at least for the first couple of seconds, as you suddenly get blinded by the light. \n" +
                            "You made it, you turned the lights and the electricity back on. \n" +
                            "At first you're excited, until you see the object you fell over earlier, now laying in front of you, clear as day.");
                    player.setFlag("body_checked");
                    return "";
                }
            case "3":
            case "inspect body":
                if (!player.hasFlag("body_checked")) {
                    System.out.println("You try and inspect the unknown object, but sadly fail. Due to the darkness you can't recognize it. Maybe try turning the power back on first");
                } else {
                    player.setFlag("body_inspected");
                }
            case "get phone":
                if (player.hasFlag("body_inspected")) {
                    if (!player.hasFlag("phone_taken")) {
                        player.setFlag("phone_taken");
                    } else {
                        return "You already picked up the phone";
                    }
                } else {
                    return "You need to find the phone first";
                }
            case "read note":
                if (player.hasFlag("body_inspected")) {
                    return "note";
                } else {
                    return "You need to find the note first";
                }
            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "invalid action";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("found_trash_id")) {
            exits.put("secretary", new Exit("enter_secretary", null));
        }
        exits.put("it", new Exit("enter_it", null));
        return exits;
    }

    @Override
    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You entered the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }
}
