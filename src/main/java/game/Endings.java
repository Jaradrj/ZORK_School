package game;

import controller.GameController;
import java.util.Scanner;

public class Endings {

    private final GameController controller;

    public Endings(GameController controller) {
        this.controller = controller;
    }

    public void happyEnding(Player player) {
        System.out.println("Congratulations. You made it out. Alive.\n" +
                "You called the police. You exposed the hidden doors. You told them everything.\n" +
                "And for a while, it felt like justice would follow.\n" +
                "But truth is rarely as simple as it seems.\n" +
                "And survival doesn’t always mean freedom.\n" +
                "\n" +
                "Your school was known for excellence.\n" +
                "A hub for “gifted youth,” praised for its scientific achievements and quietly pressured to produce results.\n" +
                "Behind the high grades and smiling faculty hid something else:\n" +
                "MindScale — a covert neuropsychological experiment disguised as a student performance program.\n" +
                "\n" +
                "It started small.\n" +
                "Extra lessons. Secret “research initiatives.” Voluntary participation.\n" +
                "Until it wasn’t voluntary anymore.\n" +
                "\n" +
                "Klara Price was the poster child.\n" +
                "Top of her class. Always polite. Always ready to please.\n" +
                "She wasn’t chosen randomly.\n" +
                "She was conditioned. By praise, by pressure, and by Mrs. Hamps, the school psychologist.\n" +
                "Hamps told her she was special. That her talent could change the world.\n" +
                "All Klara had to do was help test a new compound:\n" +
                "Scopolamine, a neurotoxin that suppresses resistance, enhances suggestibility, and erodes memory.\n" +
                "\n" +
                "Klara didn’t ask why.\n" +
                "By the time she realized what was happening, it was too late.\n" +
                "Her silence helped perfect a drug that killed her classmates\n" +
                "and in the end, where she could hardly stay silent anymore, even herself.\n" +
                "Her loyalty was her downfall.\n" +
                "\n" +
                "Leandro wasn’t like Klara.\n" +
                "He questioned authority. Refused to play along. Noticed the missing students.\n" +
                "They said he dropped out.\n" +
                "But the truth? He became a liability. And liabilities were added to the test pool.\n" +
                "Leandro resisted the drug. Fought the effects.\n" +
                "But resistance wasn’t enough. His name was erased. His body hidden.\n" +
                "He died trying to find the truth.\n");

                if (player.hasFlag("second_try")) {
                    System.out.printf(
                            "%s, a tragedy. I don't know what exactly led them here in the first place, " +
                                    "but something tells me... they've done this before. The shadows feel familiar, " +
                                    "the silence more oppressive. This time, maybe they'll get it right. " +
                                    "Maybe they'll uncover what the others could not.\n\n",
                            player.oldName
                    );
                }


                 System.out.println("Toby McConnor worked at the school for 30 years.\n" +
                        "He wasn’t a genius. He wasn’t on any list. He was just… observant.\n" +
                        "When he heard strange noises in the electrical room, he followed them.\n" +
                        "He crawled through a maintenance shaft, left unlocked by accident.\n" +
                        "They never meant for anyone to find the room.\n" +
                        "Especially not Toby.\n" +
                        "His name never made the papers. He was just “an unfortunate workplace accident.”\n" +
                        "\n" +
                        "Mrs. Hamps was supposed to protect students.\n" +
                        "Instead, she led the project.\n" +
                        "MindScale wasn’t her idea, but she executed it perfectly.\n" +
                        "Students deemed “emotionally unstable” or “underperforming” were marked for trials.\n" +
                        "They weren’t expected to survive.\n" +
                        "They were expected to comply.\n" +
                        "The experiment spiraled.\n" +
                        "Overdoses. Hallucinations. Deaths.\n" +
                        "But even then, she continued. \"For the greater good.\"\n" +
                        "They say she broke down when arrested.\n" +
                        "But some claim she simply smiled.\n" +
                        "\n" +
                        "After your escape, the police launched an investigation.\n" +
                        "Hamps and three others were arrested. The school board denied knowledge.\n" +
                        "New programs were introduced.\n" +
                        "\"Wellness initiatives.\"\n" +
                        "\"Trauma-informed learning environments.\"\n" +
                        "But behind new posters and smiling counselors, the structure remained unchanged.\n" +
                        "\n" +
                        "Weeks later, a letter arrives at your home. No return address.\n" +
                        "\n" +
                        "Inside:\n" +
                        "    “Participant #27: Conclusion successful. Resistance indicates independent cognition. Memory retention: 97%. Recommendation: Proceed to Phase II.”\n" +
                        "\n" +
                        "You realize the truth.\n" +
                        "You weren’t the one who escaped the system.\n" +
                        "You were the final subject.\n" +
                        "\n" +
                        "Everything, the missing students, the clues, even your escape, it was all orchestrated.\n" +
                        "A test of your limits. Of your morality. Of your mind.\n" +
                        "\n" +
                        "The project never failed.\n" +
                        "It evolved.\n" +
                        "You believed you were fighting the system.\n" +
                        "But you were inside it the whole time.\n" +
                        "And if even you couldn’t tell what was real…\n" +
                        "how many others still think they’re awake?\n" +
                        "\n" +
                        "THE END.\n" +
                        "(or maybe just the next phase)\n\n\n" +
                        "          ___________\n" +
                        "         '._==_==_=_.'\n" +
                        "         .-\\:      /-.\n" +
                        "        | (|:.     |) |\n" +
                        "         '-|:.     |-'\n" +
                        "           \\::.    /\n" +
                        "            '::. .'\n" +
                        "              ) (\n" +
                        "            _.' '._\n" +
                        "           `\"\"\"\"\"\"\"`");


    }

    public void badEnding(Player player) {
        System.out.println("There you are. You pull on the door, harder and harder, hoping it will move.\n" +
                "Not a jolt.\n" +
                "\n" +
                "Hope always dies last, because you will die before it does.\n" +
                "\n" +
                "The air turns cold around you, not from the room, but from something inside.\n" +
                "Your fingers tremble, still grasping the handle like it owes you something. Like someone will answer.\n" +
                "But nothing answers here.\n" +
                "\n" +
                "You scream, but your voice is swallowed by the concrete.\n" +
                "You pound on the metal until your fists are raw, then bloody.\n" +
                "The handle is slick with your own skin.\n" +
                "\n" +
                "No echo.\n" +
                "No rescue.\n" +
                "Just the sound of your breath hitching between sobs, and the quiet drip of blood on tile.\n" +
                "\n");

        if (!player.hasFlag("body_inspected")) {
            System.out.println("You sink to the floor, shaking, and finally notice what you’d been avoiding in the dark.\n" +
                    "            Shapes.\n" +
                    "            Bodies.\n" +
                    "            Others, crumpled in corners. Some collapsed in prayer.\n" +
                    "            Some still clutching notes. One face half-rotted, eyes wide open, as if still waiting for help.");
        }

        System.out.println("The students, they didn’t pass the test.\n" +
                "Neither will you.\n" +
                "\n" +
                "On the wall behind you, flickering in the dying emergency light, the words “MindScale Phase IV – Human Factor Adjustment” appear on an old clipboard.\n" +
                "Your name is handwritten on the list. Next to a date. Today.\n" +
                "\n" +
                "The test wasn’t to see if you could escape.\n" +
                "It was to see how long you would try before giving up.\n" +
                "How far you'd bleed before breaking.\n" +
                "\n" +
                "You close your eyes, but the darkness is no different.\n" +
                "\n" +
                "\n" +
                "  ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ \n" +
                " ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗\n" +
                " ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝\n" +
                " ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗\n" +
                " ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║\n" +
                "  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝\n" +
                "\n" +
                "\n" +
                "Do you want to give up, or try again?\n" +
                "\n" +
                "You already know your answer.\n" +
                "They already recorded it.\n");
        checkTryAgain(player);
    }

    public void teacherEnding(Player player) {
        System.out.println("""
            She turns slowly, her eyes glinting in the dim light.
            'You found me... I didn’t think anyone would get this far.'
            A pause. The room feels colder now.
            'This place — it was never meant for you. Or anyone. We tried to bury it, erase it.'
            She looks past you, as if seeing something distant. Or remembering.
            'I stayed to make sure no one would open the door again. But now it's too late.'
            Her voice drops to a whisper. 'They’re already after us.'
            The ground trembles. Lights flicker. 
            'I’m sorry.'
            Everything fades.
        """);
        System.out.println("\n" +
                "\n" +
                "  ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ \n" +
                " ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗\n" +
                " ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝\n" +
                " ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗\n" +
                " ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║\n" +
                "  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝\n" +
                "\n" +
                "\n" +
                "Do you want to give up, or try again?\n" +
                "\n" +
                "You already know your answer.\n" +
                "They already recorded it.\n");
        checkTryAgain(player);
    }

    public void checkTryAgain(Player player) {
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();

        switch(action.toLowerCase().trim()) {
            case "no":
            case "n":
                System.out.println("Connection terminated.\n" +
                        "\n" +
                        "Your decisions have been logged.\n" +
                        "Emotional tolerance: Exceeded.\n" +
                        "Cognitive resistance: Broken.\n" +
                        "\n" +
                        "No further input required.\n" +
                        "\n" +
                        "Thank you for your participation.\n" +
                        "You will not be contacted again.\n" +
                        "\n" +
                        "[ MindScale // Phase IV Complete ]");

                System.exit(0);
                break;
            case "yes":
            case "y":
            default:
                player.clearFlags();
                player.setFlag("second_try");
                controller.run();
                break;
        }
    }
}
