package rooms;

import game.*;
import java.util.HashMap;
import java.util.Map;

public class PrinterRoom implements Room {

    @Override
    public String getName() {
        return "Printer Room";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the Printer Room.");
        System.out.println("The printer seems to have been used very recently. The exams were already printed.");

        System.out.println("Actions:");

        System.out.println("- Look at Chemistry Exam");
        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "look":
            case "look at chemistry exam":
                System.out.println("CHEMISTRY EXAM – GRADE 12\n" +
                        "\n" +
                        "Name: _______________________  Date: //20__\n" +
                        "Duration: 45 minutes  Answer all questions. No external materials allowed.\n" +
                        "\n" +
                        "Section A – Inorganic Chemistry Basics (20 pts)\n" +
                        "\n" +
                        "    Which of the following compounds is the most protolytically active?\n" +
                        "     a) H₂O  b) CH₃COOH  c) H₂SO₄  d) NH₃\n" +
                        "\n" +
                        "    What is the pH of a 0.1 molar hydrochloric acid solution?\n" +
                        "     a) 7  b) 1  c) 13  d) Cannot be determined\n" +
                        "\n" +
                        "    Which formula represents a strong oxidizing compound with neuroactive effects?\n" +
                        "     a) C₆H₁₂O₆  b) H₂O₂  c) C₁₇H₂₁NO₄  d) NaCl\n" +
                        "     (Hint: Consider behavior under emotional stimulation.)\n" +
                        "\n" +
                        "Section B – Behavioral Application (30 pts)\n" +
                        "\n" +
                        "    If a compound alters perception and behavior, it should in a controlled test environment be...\n" +
                        "     a) Isolated and contained\n" +
                        "     b) Administered under observation\n" +
                        "     c) Documented, then administered\n" +
                        "     d) Immediately neutralized\n" +
                        "\n" +
                        "    A subject shows signs of acute paranoia after exposure to Substance X. What is the next step according to protocol?\n" +
                        "     a) Terminate contact\n" +
                        "     b) Continue observation\n" +
                        "     c) Report to supervisor\n" +
                        "     d) Increase dosage for stimulation phase\n" +
                        "     (Refer to Document M302-A, Archive Section 4)\n" +
                        "\n" +
                        "Section C – Personality Scaling (50 pts)\n" +
                        "Answer using the scale: 1 = Strongly disagree … 5 = Strongly agree\n" +
                        "\n" +
                        "    I fully trust the school administration.\n" +
                        "\n" +
                        "    I would report someone who breaks the rules.\n" +
                        "\n" +
                        "    People behave rationally under pressure.\n" +
                        "\n" +
                        "    Nothing is more important than maintaining control.\n" +
                        "\n" +
                        "    Emotions are chemically measurable.\n" +
                        "\n" +
                        "    I sometimes feel like I’m being watched.\n" +
                        "\n" +
                        "Note to Instructors (Do not remove this page):\n" +
                        "This version includes the revised M-Scale Index. Only to be administered in Room 302. Ensure proper ventilation – prior incidents must not be repeated.\n" +
                        "\n" +
                        "    Scratched notes barely visible at the bottom:\n" +
                        "    “…Baseline behavior triggers activated after 14 minutes. No resistance. Synchronization confirmed.”\n\n\n" +
                        "This is not the chemistry I know. Room 302. Isn't that the Electricity Room?");
                if (!player.hasFlag("saw_periodic_table") && !player.hasFlag("saw_formula_papers") && !player.hasFlag("acid_taken") && player.hasFlag("full_map_taken")) {
                    return "Hint: You should check out the chemistry room.";
                }
                if (!player.hasFlag("saw_periodic_table") || !player.hasFlag("saw_formula_papers") || !player.hasFlag("acid_taken")) {
                    return "Maybe I missed something in the Chemistry Room?";
                }
                break;
            case "leave":
                return "You decide to leave. Where do you want to go? (Use: go to X)";
            default:
                if (action.startsWith("go to ")) {
                    return handleRoomChange(player, action.substring(6).trim());
                }
                return "invalid action";
        }
        return "";
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
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("Teacher Room", new Exit("Teacher Room", null));
        if (player.hasFlag("has_full_map")) {
            exits.put("chemistry room", new Exit("Chemistry Room", null));
        }
        return exits;
    }
}