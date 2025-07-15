
package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.controller.UIGameController;
import ui.game.UICommands;
import ui.game.UIRoom;
import ui.game.UIRoomFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIPrinterRoom implements UIRoom {

    private UICommands commands;

    @Override
    public String getName() {
        return "printer room";
    }

    public UIPrinterRoom(UICommands commands) {
        this.commands = commands;
    }

    @Override
    public String enter(Player player) {
        StringBuilder text = new StringBuilder();
        if (!player.hasFlag("was_printer")) {
            player.setFlag("was_printer");
            text.append("The printer seems to have been used very recently. The exams were already printed.");
        }
        return text.toString();
    }

    @Override
    public List<String> getAvailableActions(Player player) {
        List<String> actions = new ArrayList<>();
        actions.add("Look at Chemistry Exam");
        actions.add("leave");
        return actions;
    }


    @Override
    public String performAction(Player player, String action, TextBox outputArea) {

        String lowerAction = action.toLowerCase().trim();
        StringBuilder result = new StringBuilder();

        switch (lowerAction) {
            case "1":
            case "look":
            case "look at chemistry exam":
                SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                result.append("CHEMISTRY EXAM – GRADE 12\n");
                result.append("\n");
                result.append("Name: _______________________  Date: //20__\n");
                result.append("Duration: 45 minutes  Answer all questions. No external materials allowed.\n");
                result.append("\n");
                result.append("Section A – Inorganic Chemistry Basics (20 pts)\n");
                result.append("\n");
                result.append("    Which of the following compounds is the most protolytically active?\n");
                result.append("     a) H₂O  b) CH₃COOH  c) H₂SO₄  d) NH₃\n");
                result.append("\n");
                result.append("    What is the pH of a 0.1 molar hydrochloric acid solution?\n");
                result.append("     a) 7  b) 1  c) 13  d) Cannot be determined\n");
                result.append("\n");
                result.append("    Which formula represents a strong oxidizing compound with neuroactive effects?\n");
                result.append("     a) C₆H₁₂O₆  b) H₂O₂  c) C₁₇H₂₁NO₄  d) NaCl\n");
                result.append("     (Hint: Consider behavior under emotional stimulation.)\n");
                result.append("\n");
                result.append("Section B – Behavioral Application (30 pts)\n");
                result.append("\n");
                result.append("    If a compound alters perception and behavior, it should in a controlled test environment be...\n");
                result.append("     a) Isolated and contained\n");
                result.append("     b) Administered under observation\n");
                result.append("     c) Documented, then administered\n");
                result.append("     d) Immediately neutralized\n");
                result.append("\n");
                result.append("    A subject shows signs of acute paranoia after exposure to Substance X.\nWhat is the next step according to protocol?\n");
                result.append("     a) Terminate contact\n");
                result.append("     b) Continue observation\n");
                result.append("     c) Report to supervisor\n");
                result.append("     d) Increase dosage for stimulation phase\n");
                result.append("     (Refer to Document M302-A, Archive Section 4)\n");
                result.append("\n");
                result.append("Section C – Personality Scaling (50 pts)\n");
                result.append("Answer using the scale: 1 = Strongly disagree … 5 = Strongly agree\n");
                result.append("\n");
                result.append("    I fully trust the school administration.\n");
                result.append("\n");
                result.append("    I would report someone who breaks the rules.\n");
                result.append("\n");
                result.append("    People behave rationally under pressure.\n");
                result.append("\n");
                result.append("    Nothing is more important than maintaining control.\n");
                result.append("\n");
                result.append("    Emotions are chemically measurable.\n");
                result.append("\n");
                result.append("    I sometimes feel like I’m being watched.\n");
                result.append("\n");
                result.append("Note to Instructors (Do not remove this page):\n");
                result.append("This version includes the revised M-Scale Index.\nOnly to be administered in Room 302.\nEnsure proper ventilation – prior incidents must not be repeated.\n");
                result.append("\n");
                result.append("    Scratched notes barely visible at the bottom:\n");
                result.append("    “…Baseline behavior triggers activated after 14 minutes.\nNo resistance. Synchronization confirmed.”\n");
                result.append("\n");
                result.append("\n");
                result.append("This is not the chemistry I know. Room 302. Isn't that the Electricity Room?\n");

                if (!player.hasFlag("saw_periodic_table") && !player.hasFlag("saw_formula_papers") && !player.hasFlag("acid_taken") && player.hasFlag("full_map_taken")) {
                    result.append("Hint: You should check out the chemistry room.");
                }
                if (!player.hasFlag("saw_periodic_table") || !player.hasFlag("saw_formula_papers") || !player.hasFlag("acid_taken")) {
                    result.append("Maybe I missed something in the Chemistry Room?");
                }
                break;
            case "leave":
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
        exits.put("teacher room", new Exit("teacher room", null));
        if (player.hasFlag("full_map_taken")) {
            exits.put("chemistry room", new Exit("chemistry room", null));
        }
        return exits;
    }
}