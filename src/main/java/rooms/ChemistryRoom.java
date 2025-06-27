package rooms;

import game.Player;
import game.Room;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChemistryRoom implements Room {

    @Override
    public String getName() {
        return "Chemistry Room";
    }

    @Override
    public void enter(Player player) {
        System.out.println("You enter the Chemistry Room.");

        System.out.println("Science has never been your favorite subject. But it most definitely is Klara's. This whole room is just full of her awards. It seems like they cover up the yellow walls.\nThe brewing station in the back of the room still has some chemicals opened. What is Scopolamine? I heard of that before...\nWhile trying to remember, your flashlight's battery suddenly dies. Maybe you should try to find a way into the Electricity Room?");

        System.out.println("Actions:");
        System.out.println("- Look at Periodic Table");
        System.out.println("- Look at Formula Papers");
        System.out.println("- Look at Chemistry Exam");
        if (!player.hasFlag("acid_taken") &&
                player.hasFlag("saw_formula_papers") &&
                player.hasFlag("saw_periodic_table")) {
            System.out.println("- Brew Acid");
        }
        System.out.println("- Leave");
    }

    @Override
    public String performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "1":
            case "look":
            case "look at periodic table":
                    player.setFlag("saw_periodic_table");
                    return "+----+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                            "| H  | He                                                         |\n" +
                            "+----+----+                                                    +--+\n" +
                            "| Li | Be |                      | B  | C  | N  | O  | F  | Ne |  |\n" +
                            "+----+----+                      +----+----+----+----+----+----+--+\n" +
                            "| Na | Mg |                      | Al | Si | P  | S  | Cl | Ar |\n" +
                            "+----+----+                      +----+----+----+----+----+----+\n" +
                            "| K  | Ca | Sc | Ti | V  | Cr | Mn | Fe | Co | Ni | Cu | Zn |\n" +
                            "+----+----+----+----+----+----+----+----+----+----+----+----+\n" +
                            "| Br | I  | Hg | Pb | Sn | As | Sb | Cd | Ba | Sr | Ra | U  |\n" +
                            "+----+----+----+----+----+----+----+----+----+----+----+----+\n";
            case "2":
            case "look at formula papers":
                    player.setFlag("saw_formula_papers");
                    return "╔════════════════════════════════════╗\n" +
                            "║     ADVANCED CHEMISTRY FORMULAS   ║\n" +
                            "╠════════════════════════════════════╣\n" +
                            "║ Water               -> H2O         ║\n" +
                            "║ Sulfuric Acid       -> H2SO4       ║\n" +
                            "║ Hydrochloric Acid   -> HCl         ║\n" +
                            "║ Nitric Acid         -> HNO3        ║\n" +
                            "║ Scopolamine (alk.)  -> C17H21NO4   ║\n" +
                            "╚════════════════════════════════════╝\n" +
                            "*Note: Handle with caution.\n";
            case "3":
            case "look at chemistry exam":
                    return "CHEMISTRY EXAM – GRADE 12\n" +
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
                            "This is not the chemistry I know. Room 302. Isn't that the Electricity Room?";
            case "brew acid":
                if (player.hasFlag("acid_taken")) {
                    return "You’ve already brewed the acid. You can’t carry more.";
                }
                if (!player.hasFlag("saw_formula_papers") || !player.hasFlag("saw_periodic_table")) {
                    return "You don't understand the chemistry well enough yet. Maybe check the formula papers and periodic table first.";
                }
                Scanner scanner = new Scanner(System.in);
                System.out.println("Input chemical formula to brew sulfuric acid:");
                String formula = scanner.nextLine().trim().toLowerCase();
                if (formula.equals("h2so4")) {
                    player.setFlag("acid_taken");
                    return "You mix the chemicals carefully. The solution bubbles violently. You now carry Sulfuric Acid. Maybe it can help melt the lock on the Electricity Room door.";
                } else {
                    return "You mix the chemicals, but nothing happens. Wrong combination?";
                }
            case "leave":
                return "You decide to leave. Where do you want to go? (Use: go to X)";
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
    public Map<String, Exit> getAvailableExits(Player player) {
        Map<String, Exit> exits = new HashMap<>();
        exits.put("secretary", new Exit("Secretary", null));
        if (player.hasFlag("acid_taken")) {
            exits.put("electricity room", new Exit("Electricity Room", null));
        }
        return exits;
    }
}