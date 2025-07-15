package ui.rooms;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextBox;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import console.game.*;
import ui.game.UICommands;
import ui.game.UIEndings;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.*;

public class UIElectricityRoom implements UIRoom {

    private UIEndings ending;
    private MultiWindowTextGUI gui;
    private UICommands commands;
    private TextPrinter printer;

    public UIElectricityRoom(UIGameController controller, UICommands commands, MultiWindowTextGUI gui, TextPrinter printer) {
        this.gui = gui;
        this.ending = new UIEndings(controller, gui);
        this.commands = commands;
        this.printer = printer;
    }

    @Override
    public String getName() {
        return "electricity room";
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_electricity")) {
            player.setFlag("was_electricity");
            text.append("You look inside, but all you see is darkness.\n");
            text.append("And so it's clear, the only way forward is to jump.\n");
            text.append("You pause, thinking about everything that brought you here.\n");
            text.append("The risks, the choices, the distance you've come. And all\nthat just to turn around and leave?\n");
            text.append("No. Not after all of this. I mean it's just a jump right?\n");
            text.append("You try and climb out of the shaft, barely getting into the\nright position to just let go and jump.\n");
            text.append("Just a few seconds later, you feel the hard ground beneath\nyour feet. You tried to land upright, but your legs buckle\nand you drop to your knees with a sharp jolt of pain.\n");
            text.append("They're sore. Nothing broken, but it hurts.\n");
            text.append("You stand up and try to figure out, what you can do.\nAt the far end of the room, a green emergency exit light flickers.\nHoping to see something useful, you decide to walk toward it.\n");
            text.append("You manage to take the first few steps, until suddenly,\nthe hard ground meets your knees once more.\n");
            text.append("Out of frustration and the need for light you manage to\nget up and continue walking, ignoring whatever it was that you stumbled over.\n");
            text.append("You keep walking, step after step, until you finally reach the light.\n");
            text.append("You glance around, and through the dimness,you make out the\nshape of the reactor and next to it, the exit.\n");
            text.append("While you keep observing your surroundings you notice that\nweird irony smell once again.\n");
            text.append("You wonder and wonder what it is, but just can't figure it out.\nIt smells like mold and blood or just like death.\n");
            text.append("Then you remember the weird thing you stumbled over earlier.");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if (player.hasFlag("await_body_check_phone")) {
            if (!player.hasFlag("phone_taken")) {
                actions.add("Take phone");
            }
            return actions;
        }
        if (player.hasFlag("await_body_check_note")) {
            if (!player.hasFlag("read_third_note")) {
                actions.add("Read note");
            }
            return actions;
        }
        if (!player.hasFlag("door_opened")) {
            actions.add("Open the door");
        }
        if (!player.hasFlag("body_checked")) {
            actions.add("Inspect unknown object");
        } else if (!player.hasFlag("body_inspected")) {
            actions.add("Inspect body");
        }
        if (!player.hasFlag("turned_on_power")) {
            actions.add("Enable radiator");
        }
        if (player.hasFlag("door_opened")) {
            actions.add("leave");
        }

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();
        switch (lowerAction) {
            case "open the door":
                if (player.hasFlag("keys_taken")) {
                    if (!player.hasFlag("door_opened")) {
                        player.setFlag("door_opened");
                        String input = "You successfully open the door to freedom. Maybe you could call the police?";
                        TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                    }
                    return "";

                } else if (player.hasFlag("door_failed")) {
                    ending.badEnding(player, outputArea);
                } else {
                    player.setFlag("door_failed");
                    String input = "You try to open the door but rapidly notice that you are missing the one key that might give you your freedom.";
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                    break;
                }
                break;
            case "enable radiator":
                if (player.hasFlag("turned_on_power")) {
                    String input = "You already turned on the power";
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                } else {
                    player.setFlag("turned_on_power");
                    SoundPlayer.playSound("/sounds/Radiator.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    SoundPlayer.playSound("/sounds/LightsFlickering.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    String input =
                            "You walk over to the radiator.\n" +
                                    "It’s not your first time in this room, so you’re not completely lost.\n" +
                                    "Still, in this darkness, fixing the lights will be a challenge. \n" +
                                    "You manage to open the switchboard but quickly find yourself disoriented. \n" +
                                    "You can’t see a thing, so you have to rely on your memory and senses instead. \n" +
                                    "You touch the switches while remembering the course you had in this\n" +
                                    "room a couple of months ago. \n" +
                                    "You carefully touch the switches, recalling the IT course you\n" +
                                    "took here a few months ago.\n" +
                                    "The janitor, Toby, had explained the different switches and their functions on the radiator in front of you.\n" +
                                    "You try to remember how it used to look, comparing it to what you feel now.\n" +
                                    "Wait a minute, you think to yourself. Suddenly you remember the main\n" +
                                    "switch, and where it should be located.\n" +
                                    "You hope and pray that you're correct and decide to pull it. Nothing happens,\n" +
                                    "at least for the first couple of seconds, as you suddenly get blinded by the light. \n" +
                                    "You made it, you turned the lights and the electricity back on. \n" +
                                    "At first you're excited, until you see the object you fell over earlier,\n" +
                                    "now laying in front of you, clear as day.";

                    player.setFlag("body_checked");
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);

                }
                break;
            case "inspect unknown object":
            case "inspect body":
                if (!player.hasFlag("body_checked")) {
                    String input = "You try and inspect the unknown object, but sadly fail.\nDue to the darkness you can't recognize it.\n" +
                            "Maybe try turning the power back on first";

                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);

                } else {
                    if (!player.hasFlag("body_inspected")) {
                        player.setFlag("body_inspected");
                        player.setFlag("await_body_check_phone");
                        player.setFlag("await_body_check_note");
                        String input =
                                "You stare at the body, the identity of the person is\n" +
                                        "impossible to make out from this distance.\n" +
                                        "It's the same body you just tripped over.\n" +
                                        "A chill runs through you. Your hands start to shake,\n" +
                                        "and nausea rises in your throat. Suddenly,\n" +
                                        "you understand where that awful smell from earlier was\n" +
                                        "coming from. Blood. Rot. Death.\n" +
                                        "It's all here, and it's real.\n" +
                                        "Despite the sickness twisting in your gut, something\n" +
                                        "stronger takes hold: curiosity. Or maybe it's dread.\n" +
                                        "Either way, you have to know who it is.\n" +
                                        "So you move. One hesitant step at a time.\n" +
                                        "Closer.\n" +
                                        "And closer.\n" +
                                        "Until finally... a face.\n" +
                                        "Familiar.\n" +
                                        "Way too familiar.\n";

                        if (player.hasFlag("second_try")) {
                            String text = "It's your friend " + player.oldName;
                            input += text;
                        } else {
                            String text = "It's the janitor Toby";
                            input += text;
                        }
                        TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                    }
                }
                break;

            case "take phone":
                if (!player.hasFlag("phone_taken")) {
                    player.setFlag("phone_taken");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    String input = "Phone taken";
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                } else {
                    String input = "You already picked up the phone.";
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                }
                player.clearFlag("await_body_check_phone");
                break;
            case "read note":
                if (!player.hasFlag("read_third_note")) {
                    player.setFlag("read_third_note");
                    String input = """
                            I was here before they introduced these... 'methods' were introduced. 
                            Back then it was about heaters. Today it's about children. 
                            I've seen the power box. The real one no one knows about.
                            The one under the office, with the cables that aren't in the plan.
                            I opened it. And then closed it again. I should have...
                            I should have said something straight away." 
                            (crossed out several times: "Power... cable... door... light...") 
                            "You've been watching me since I started asking questions.
                            Suddenly my key no longer works.
                            Suddenly orders come in the mail that I've never seen. 
                            And then... this flickering in the Teacher’s room.
                            The camera that records even though the monitor is black." 
                            "Someone was shouting. I think it was the boy with the curly hair.
                            They said he was signed out.
                            Who voluntarily signs out of a room without a door?" 
                            (below, written in a shaky hand) 
                            "If you find this: 
                            Don't trust anyone who knows what MindScale is and still smiles. 
                            The code is NOT in the safe! 
                            And if you can... Take this outside.
                            Show them that we didn't just... disappear.""";

                    printer.textPrinter(input, outputArea);
                    SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);

                } else {
                    String input = "You already read the note";
                    TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
                }
                player.clearFlag("await_body_check_note");
                break;
            case "leave":
                player.setFlag("leaving");
                return "";
            default:
                String input = "Invalid action.";
                TypingEffect.typeWithSound(outputArea, input, UIGameController.getGuiInstance(), null);
        }
        outputArea.setText(outputArea.getText() + "\n\n" + result);
        return result.toString();
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
        String roomKey = roomName.toLowerCase();
        if (exits.containsKey(roomKey)) {
            UIRoom targetRoom = UIRoomFactory.createRoom(roomName);
            player.setCurrentUIRoom(targetRoom);
            return targetRoom.enter(player);
        } else {
            return "There is no room called '" + roomName + "' here.";
        }
    }
}
