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

public class UIElectricityRoom implements UIRoom {

    private UIEndings ending;
    private MultiWindowTextGUI gui;
    private UICommands commands;
    public UIElectricityRoom(UIGameController controller, UICommands commands, MultiWindowTextGUI gui) {
        this.gui = gui;
        this.ending = new UIEndings(controller, gui);
        this.commands = commands;
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
            text.append("You glance around, and through the dimness,you make out the\n shape of the reactor and next to it, the exit.\n");
            text.append("While you keep observing your surroundings you notice that\nweird irony smell once again.\n");
            text.append("You wonder and wonder what it is, but just can't figure it out.\nIt smells like mold and blood or just like death.\n");
            text.append("Then you remember the weird thing you stumbled over earlier.");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        if(player.hasFlag("await_body_check_phone")){
            if (!player.hasFlag("phone_taken")) {
                actions.add("Take phone");
            }
            return actions;
        }
        if(player.hasFlag("await_body_check_note")){
            if (!player.hasFlag("read_third_note")) {
                actions.add("Read note");
            }
            return actions;
        }
        actions.add("Open the door");
        if (!player.hasFlag("body_checked")) {
            actions.add("Inspect unknown object");
        } else {
            actions.add("Inspect body");
        }
        if (!player.hasFlag("turned_on_power")) {
            actions.add("Enable radiator");
        }
        actions.add("leave");

        return actions;
    }

    @Override
    public String performAction(Player player, String action, TextBox outputArea) {
        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();
        switch (lowerAction) {
            case "1":
            case "open the door":
                if (player.hasFlag("keys_taken")) {
                    if (!player.hasFlag("door_opened")) {
                        player.setFlag("door_opened");
                        result.append("You successfully open the door to freedom. Maybe you could call the police?");
                    }
                    if (player.hasFlag("door_opened")){
                        commands.checkInputCommands("-r", player, outputArea);
                    }
                    return "";
                } else if(player.hasFlag("door_failed")){
                    ending.badEnding(player, outputArea);
                }
                else {
                    player.setFlag("door_failed");
                    result.append("You try to open the door but rapidly notice that you are missing the one key that might give you your freedom.");
                    break;
                }
                break;
            case "2":
            case "enable radiator":
                if (player.hasFlag("turned_on_power")) {
                    result.append("You already turned on the power");
                } else {
                    player.setFlag("turned_on_power");
                    SoundPlayer.playSound("/sounds/Radiator.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    SoundPlayer.playSound("/sounds/LightsFlickering.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("You walk over to the radiator.\n");
                    result.append("It’s not your first time in this room, so you’re not completely lost.\nStill, in this darkness, fixing the lights will be a challenge. \n");
                    result.append("You manage to open the switchboard but quickly find yourself disoriented. \n");
                    result.append("You can’t see a thing, so you have to rely on your memory and senses instead. \n");
                    result.append("You touch the switches while remembering the course you had in this\nroom a couple of months ago. \n");
                    result.append("You carefully touch the switches, recalling the IT course you\ntook here a few months ago.\n");
                    result.append("The janitor, Toby, had explained the different switches and their functions on the radiator in front of you.\n");
                    result.append("You try to remember how it used to look, comparing it to what you feel now.\n");
                    result.append("Wait a minute, you think to yourself. Suddenly you remember the main\nswitch, and where it should be located.\n");
                    result.append("You hope and pray that you're correct and decide to pull it. Nothing happens,\nat least for the first couple of seconds, as you suddenly get blinded by the light. \n");
                    result.append("You made it, you turned the lights and the electricity back on. \n");
                    result.append("At first you're excited, until you see the object you fell over earlier,\nnow laying in front of you, clear as day.");
                    player.setFlag("body_checked");
                }
                break;
            case "3":
            case "inspect unknown object":
            case "inspect body":
                if (!player.hasFlag("body_checked")) {
                    result.append("You try and inspect the unknown object, but sadly fail.\nDue to the darkness you can't recognize it. ")
                            .append("Maybe try turning the power back on first");
                } else {
                    if(!player.hasFlag("body_inspected")){
                        player.setFlag("body_inspected");
                        player.setFlag("await_body_check_phone");
                        player.setFlag("await_body_check_note");
                        result.append("You stare at the body, the identity of the person is\nimpossible to make out from this distance.\nIt's the same body you just tripped over.\n");
                        result.append("A chill runs through you. Your hands start to shake,\nand nausea rises in your throat. Suddenly,\nyou understand where that awful smell from earlier was\ncoming from. Blood. Rot. Death.\n");
                        result.append("It's all here, and it's real.\n");
                        result.append("Despite the sickness twisting in your gut, something\nstronger takes hold: curiosity. Or maybe it's dread.\nEither way, you have to know who it is.\n");
                        result.append("So you move. One hesitant step at a time.\n");
                        result.append("Closer.\n");
                        result.append("And closer.\n");
                        result.append("Until finally... a face.\n");
                        result.append("Familiar.\n");
                        result.append("Way Too familiar.");

                        if(player.hasFlag("second_try")){
                            result.append("It's your friend ").append(player.oldName);
                        } else {
                            result.append("It's the janitor Toby");
                        }
                    }
                }
                break;

            case "get phone":
            case "take phone":
                if (!player.hasFlag("phone_taken")) {
                    player.setFlag("phone_taken");
                    SoundPlayer.playSound("/sounds/TakeItem.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("Phone taken");
                } else {
                    result.append("You already picked up the phone.");
                }
                player.clearFlag("await_body_check_phone");
                break;
            case "read note":
                if (!player.hasFlag("read_third_note")) {
                    player.setFlag("read_third_note");
                    SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                    result.append("\"I was here before they introduced these... 'methods' were introduced. \n");
                    result.append("Back then it was about heaters. Today it's about children. \n");
                    result.append("I've seen the power box. The real one no one knows about.\nThe one under the office, with the cables that aren't in the plan.\n");
                    result.append("I opened it. And then closed it again. I should have...\nI should have said something straight away. \" \n");
                    result.append("(crossed out several times: \"Power... cable... door... light...\") \n");
                    result.append("\"You've been watching me since I started asking questions.\nSuddenly my key no longer works.\nSuddenly orders come in the mail that I've never seen. \n");
                    result.append("And then... this flickering in the Teacher’s room.\nThe camera that records even though the monitor is black.\" \n");
                    result.append("\"Someone was shouting. I think it was the boy with the curly hair.\nThey said he was signed out.\nWho voluntarily signs out of a room without a door?\" \n");
                    result.append("(below, written in a shaky hand) \n");
                    result.append("\"If you find this: \n");
                    result.append("Don't trust anyone who knows what MindScale is and still smiles. \n");
                    result.append("The code is NOT in the safe! \n");
                    result.append("And if you can... Take this outside.\nShow them that we didn't just... disappear.");

                } else {
                    result.append("You already read the note");
                }
                player.clearFlag("await_body_check_note");
                break;
            case "leave":
                player.setFlag("leaving");
                commands.checkInputCommands("-r", player, outputArea);
                return "";
            default:
                result.append("Invalid action.");
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
