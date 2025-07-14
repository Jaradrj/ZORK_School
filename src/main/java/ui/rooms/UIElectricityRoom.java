package ui.rooms;
import ui.controller.UIGameController;
import console.game.*;
import ui.game.UIEndings;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UIElectricityRoom implements Room {

    private UIEndings ending;
    private Commands commands;
    public UIElectricityRoom(UIGameController controller, Commands commands) {
        this.ending = new UIEndings(controller);
        this.commands = commands;
    }

    @Override
    public String getName() {
        return "electricity room";
    }

    @Override
    public void enter(Player player) {
        if (!player.hasFlag("was_electricity")) {
            player.setFlag("was_electricity");
            System.out.println("You look inside, but all you see is darkness.\n" +
                    "And so it's clear, the only way forward is to jump.\n" +
                    "You pause, thinking about everything that brought you here.\n" +
                    "The risks, the choices, the distance you've come. And all that just to turn around and leave?\n" +
                    "No. Not after all of this. I mean it's just a jump right?\n" +
                    "You try and climb out of the shaft, barely getting into the right position to just let go and jump.\n" +
                    "\n" +
                    "Just a few seconds later, you feel the hard ground beneath your feet. You tried to land upright, but your legs buckle and you drop to your knees with a sharp jolt of pain.\n" +
                    "They're sore. Nothing broken, but it hurts.\n" +
                    "\n" +
                    "You stand up and try to figure out, what you can do. At the far end of the room, a green emergency exit light flickers. Hoping to see something useful, you decide to walk toward it.\n" +
                    "\n" +
                    "You manage to take the first few steps, until suddenly, the hard ground meets your knees once more.\n" +
                    "Out of frustration and the need for light you manage to get up and continue walking, ignoring whatever it was that you stumbled over.\n" +
                    "\n" +
                    "You keep walking, step after step, until you finally reach the light.\n" +
                    "You glance around, and through the dimness, you make out the shape of the reactor and next to it, the exit.\n" +
                    "While you keep observing your surroundings you notice that weird irony smell once again.\n" +
                    "You wonder and wonder what it is, but just can't figure it out. It smells like mold and blood or just like death.\n" +
                    "\n" +
                    "Then you remember the weird thing you stumbled over earlier.");

        }
        System.out.println("Actions: ");
        System.out.println("- Open the door");
        if (!player.hasFlag("body_checked")) {
            System.out.println("- Inspect unknown object");
        } else {
            System.out.println("- Inspect body");
        }
        if (!player.hasFlag("turned_on_power")) {
            System.out.println("- Enable radiator");
        }
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "open the door":
                if (player.hasFlag("keys_taken")) {
                    if (!player.hasFlag("door_opened")) {
                        player.setFlag("door_opened");
                        System.out.println("You successfully open the door.");
                    }
                    if (player.hasFlag("door_opened")){
                        commands.checkInputCommands("-r", player);
                    }
                    return "";
                } else if(player.hasFlag("door_failed")){
                    ending.badEnding(player);
                }
                else {
                    player.setFlag("door_failed");
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
                            "You carefully touch the switches, recalling the IT course you took here a few months ago.\n" +
                            "The janitor, Toby, had explained the different switches and their functions on the radiator in front of you.\n" +
                            "You try to remember how it used to look, comparing it to what you feel now.\n" +
                            "Wait a minute, you think to yourself. Suddenly you remember the main switch, and where it should be located.\n" +
                            "You hope and pray that you're correct and decide to pull it. Nothing happens, at least for the first couple of seconds, as you suddenly get blinded by the light. \n" +
                            "You made it, you turned the lights and the electricity back on. \n" +
                            "At first you're excited, until you see the object you fell over earlier, now laying in front of you, clear as day.");
                    player.setFlag("body_checked");
                    return "";
                }
            case "3":
            case "inspect unknown object":
            case "inspect body":
                if (!player.hasFlag("body_checked")) {
                    return "You try and inspect the unknown object, but sadly fail. Due to the darkness you can't recognize it. " +
                            "Maybe try turning the power back on first";
                } else {
                    if(!player.hasFlag("body_inspected")){
                        player.setFlag("body_inspected");
                        System.out.println("You stare at the body, the identity of the person is impossible to make out from this distance. It's the same body you just tripped over.\n" +
                                "A chill runs through you. Your hands start to shake, and nausea rises in your throat. Suddenly, you understand where that awful smell from earlier was coming from. Blood. Rot. Death.\n" +
                                "It's all here, and it's real.\n" +
                                "Despite the sickness twisting in your gut, something stronger takes hold: curiosity. Or maybe it's dread. Either way, you have to know who it is.\n" +
                                "So you move. One hesitant step at a time.\n" +
                                "Closer.\n" +
                                "And closer.\n" +
                                "Until finally... a face.\n" +
                                "Familiar.\n" +
                                "Way Too familiar.");
                        if(player.hasFlag("second_try")){
                            System.out.println("It's your friend " + player.oldName);
                        } else {
                            System.out.println("It's the janitor Toby");
                        } }
                    System.out.println("Actions: ");
                    if (!player.hasFlag("phone_taken")) {
                        System.out.println("- Take phone");
                    }
                    if (!player.hasFlag("read_third_note")) {
                        System.out.println("- Read note");
                    }
                    System.out.println("- Leave");

                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        String input = scanner.nextLine().trim().toLowerCase();

                        switch (input) {
                            case "get phone":
                            case "take phone":
                                if (!player.hasFlag("phone_taken")) {
                                    player.setFlag("phone_taken");
                                    return "Phone taken";
                                } else {
                                    return "You already picked up the phone.";
                                }
                            case "read note":
                                if (!player.hasFlag("read_third_note")) {
                                    player.setFlag("read_third_note");
                                    return "I was here before they introduced these... 'methods' were introduced. \n" +
                                            "\n" +
                                            "Back then it was about heaters. Today it's about children. \n" +
                                            "\n" +
                                            "I've seen the power box. The real one no one knows about. The one under the office, with the cables that aren't in the plan.\n" +
                                            "I opened it. And then closed it again. I should have... I should have said something straight away. \" \n" +
                                            "\n" +
                                            "(crossed out several times: \"Power... cable... door... light...\") \n" +
                                            "\n" +
                                            "\"You've been watching me since I started asking questions. Suddenly my key no longer works. Suddenly orders come in the mail that I've never seen. \n" +
                                            "\n" +
                                            "And then... this flickering in the Teacher’s room. The camera that records even though the monitor is black.\" \n" +
                                            "\n" +
                                            "\"Someone was shouting. I think it was the boy with the curly hair. They said he was signed out. Who voluntarily signs out of a room without a door?\" \n" +
                                            "\n" +
                                            "(below, written in a shaky hand) \n" +
                                            "\n" +
                                            "\"If you find this: \n" +
                                            "\n" +
                                            "Don't trust anyone who knows what MindScale is and still smiles. \n" +
                                            "\n" +
                                            "The code is NOT in the safe! \n" +
                                            "\n" +
                                            "And if you can... Take this outside. Show them that we didn't just... disappear.";
                                } else {
                                    return "You already read the note";
                                }
                            case "leave":
                                return "";
                            default:
                                System.out.println("Invalid action.");
                        }
                    }
                }

            default:
                if (action.toLowerCase().startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "Invalid action.";
        }
    }

    @Override
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        if (player.hasFlag("found_trash_id")) {
            exits.put("secretary", new Exit("secretary", null));
        }
        exits.put("it room", new Exit("it room", null));
        return exits;
    }

    @Override
    public String handleRoomChange(Player player, String roomName) {
        Map<String, Exit> exits = getAvailableExits(player);
        if (exits.containsKey(roomName)) {
            Room targetRoom = RoomFactory.createRoom(roomName);
            player.setCurrentRoom(targetRoom);
            return "You enter the " + roomName + ".";
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }
}
