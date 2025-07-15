
package ui.rooms;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.*;
import ui.audio.SoundPlayer;
import ui.components.TextPrinter;
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
    private TextPrinter printer;

    @Override
    public String getName() {
        return "printer room";
    }

    public UIPrinterRoom(UICommands commands, TextPrinter printer) {
        this.commands = commands;
        this.printer = printer;
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

        if (lowerAction.equals("look at chemistry exam")) {

            if(player.hasFlag("test_seen")){
                result.append("You already saw the test");
            } else {

                String input = """
                        CHEMISTRY EXAM – GRADE 12
                        
                        Name: _______________________  Date: //20__
                        Duration: 45 minutes  Answer all questions. No external materials allowed.
                        
                        Section A – Inorganic Chemistry Basics (20 pts)
                        
                            Which of the following compounds is the most protolytically active?
                             a) H₂O  b) CH₃COOH  c) H₂SO₄  d) NH₃
                        
                            What is the pH of a 0.1 molar hydrochloric acid solution?
                             a) 7  b) 1  c) 13  d) Cannot be determined
                        
                            Which formula represents a strong oxidizing compound with neuroactive effects?
                             a) C₆H₁₂O₆  b) H₂O₂  c) C₁₇H₂₁NO₄  d) NaCl
                             (Hint: Consider behavior under emotional stimulation.)
                        
                        Section B – Behavioral Application (30 pts)
                        
                            If a compound alters perception and behavior, it should in a controlled test environment be...
                             a) Isolated and contained
                             b) Administered under observation
                             c) Documented, then administered
                             d) Immediately neutralized
                        
                            A subject shows signs of acute paranoia after exposure to Substance X.
                        What is the next step according to protocol?
                             a) Terminate contact
                             b) Continue observation
                             c) Report to supervisor
                             d) Increase dosage for stimulation phase
                             (Refer to Document M302-A, Archive Section 4)
                        
                        Section C – Personality Scaling (50 pts)
                        Answer using the scale: 1 = Strongly disagree … 5 = Strongly agree
                        
                            I fully trust the school administration.
                        
                            I would report someone who breaks the rules.
                        
                            People behave rationally under pressure.
                        
                            Nothing is more important than maintaining control.
                        
                            Emotions are chemically measurable.
                        
                            I sometimes feel like I’m being watched.
                        
                        Note to Instructors (Do not remove this page):
                        This version includes the revised M-Scale Index.
                        Only to be administered in Room 302.
                        Ensure proper ventilation – prior incidents must not be repeated.
                        
                            Scratched notes barely visible at the bottom:
                            “…Baseline behavior triggers activated after 14 minutes.
                        No resistance. Synchronization confirmed.”
                        
                        
                        This is not the chemistry I know. Room 302. Isn't that the Electricity Room?
                        """;
                outputArea.invalidate();
                printer.textPrinter(input, outputArea);
                SoundPlayer.playSound("/sounds/ReadNote.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
                player.setFlag("test_seen");
            }

        } else if (lowerAction.equalsIgnoreCase("leave")) {
            return "";
        } else {
            result.append("Invalid action.");
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