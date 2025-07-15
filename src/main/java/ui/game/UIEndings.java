package ui.game;


import com.googlecode.lanterna.gui2.*;
import console.game.Player;
import ui.controller.UIGameController;

import java.util.Scanner;

public class UIEndings {

    private final UIGameController controller;
    private MultiWindowTextGUI gui;
    public UIEndings(UIGameController controller, MultiWindowTextGUI gui) {
        this.controller = controller;
        this.gui = gui;
    }

    public static void happyEnding(Player player, TextBox outputArea) {
        StringBuilder text = new StringBuilder();
        text.append("Congratulations. You made it out. Alive.\n")
                .append("You called the police. You exposed the hidden doors. You told them everything.\n")
                .append("And for a while, it felt like justice would follow.\n")
                .append("But truth is rarely as simple as it seems.\n")
                .append("And survival doesn’t always mean freedom.\n")
                .append("\n")
                .append("Your school was known for excellence.\n")
                .append("A hub for “gifted youth,” praised for its scientific achievements and quietly pressured to produce results.\n")
                .append("Behind the high grades and smiling faculty hid something else:\n")
                .append("MindScale — a covert neuropsychological experiment disguised as a student performance program.\n")
                .append("\n")
                .append("It started small.\n")
                .append("Extra lessons. Secret “research initiatives.” Voluntary participation.\n")
                .append("Until it wasn’t voluntary anymore.\n")
                .append("\n")
                .append("Klara Price was the poster child.\n")
                .append("Top of her class. Always polite. Always ready to please.\n")
                .append("She wasn’t chosen randomly.\n")
                .append("She was conditioned. By praise, by pressure, and by Mrs. Hamps, the school psychologist.\n")
                .append("Hamps told her she was special. That her talent could change the world.\n")
                .append("All Klara had to do was help test a new compound:\n")
                .append("Scopolamine, a neurotoxin that suppresses resistance, enhances suggestibility, and erodes memory.\n")
                .append("\n")
                .append("Klara didn’t ask why.\n")
                .append("By the time she realized what was happening, it was too late.\n")
                .append("Her silence helped perfect a drug that killed her classmates\n")
                .append("and in the end, where she could hardly stay silent anymore, even herself.\n")
                .append("Her loyalty was her downfall.\n")
                .append("\n")
                .append("Leandro wasn’t like Klara.\n")
                .append("He questioned authority. Refused to play along. Noticed the missing students.\n")
                .append("They said he dropped out.\n")
                .append("But the truth? He became a liability. And liabilities were added to the test pool.\n")
                .append("Leandro resisted the drug. Fought the effects.\n")
                .append("But resistance wasn’t enough. His name was erased. His body hidden.\n")
                .append("He died trying to find the truth.\n");

        if (player.hasFlag("second_try")) {
            text.append(String.format(
                    "\n%s, a tragedy. I don't know what exactly led them here in the first place, " +
                            "but something tells me... they've done this before. The shadows feel familiar, " +
                            "the silence more oppressive. This time, maybe they'll get it right. " +
                            "Maybe they'll uncover what the others could not.\n", player.oldName
            ));
        }


        text.append("Toby McConnor worked at the school for 30 years.\n")
                .append("He wasn’t a genius. He wasn’t on any list. He was just… observant.\n")
                .append("When he heard strange noises in the electrical room, he followed them.\n")
                .append("He crawled through a maintenance shaft, left unlocked by accident.\n")
                .append("They never meant for anyone to find the room.\n")
                .append("Especially not Toby.\n")
                .append("His name never made the papers. He was just “an unfortunate workplace accident.”\n")
                .append("\n")
                .append("Mrs. Hamps was supposed to protect students.\n")
                .append("Instead, she led the project.\n")
                .append("MindScale wasn’t her idea, but she executed it perfectly.\n")
                .append("Students deemed “emotionally unstable” or “underperforming” were marked for trials.\n")
                .append("They weren’t expected to survive.\n")
                .append("They were expected to comply.\n")
                .append("The experiment spiraled.\n")
                .append("Overdoses. Hallucinations. Deaths.\n")
                .append("But even then, she continued. \"For the greater good.\"\n")
                .append("They say she broke down when arrested.\n")
                .append("But some claim she simply smiled.\n")
                .append("\n")
                .append("After your escape, the police launched an investigation.\n")
                .append("Hamps and three others were arrested. The school board denied knowledge.\n")
                .append("New programs were introduced.\n")
                .append("\"Wellness initiatives.\"\n")
                .append("\"Trauma-informed learning environments.\"\n")
                .append("But behind new posters and smiling counselors, the structure remained unchanged.\n")
                .append("\n")
                .append("Weeks later, a letter arrives at your home. No return address.\n")
                .append("\n")
                .append("Inside:\n")
                .append("    “Participant #27: Conclusion successful. Resistance indicates independent cognition. Memory retention: 97%. Recommendation: Proceed to Phase II.”\n")
                .append("\n")
                .append("You realize the truth.\n")
                .append("You weren’t the one who escaped the system.\n")
                .append("You were the final subject.\n")
                .append("\n")
                .append("Everything, the missing students, the clues, even your escape, it was all orchestrated.\n")
                .append("A test of your limits. Of your morality. Of your mind.\n")
                .append("\n")
                .append("The project never failed.\n")
                .append("It evolved.\n")
                .append("You believed you were fighting the system.\n")
                .append("But you were inside it the whole time.\n")
                .append("And if even you couldn’t tell what was real…\n")
                .append("how many others still think they’re awake?\n")
                .append("\n")
                .append("THE END.\n")
                .append("(or maybe just the next phase)\n")
                .append("\n")
                .append("          ___________\n")
                .append("         '._==_==_=_.'\n")
                .append("         .-\\:      /-.\n")
                .append("        | (|:.     |) |\n")
                .append("         '-|:.     |-'\n")
                .append("           \\::.    /\n")
                .append("            '::. .'\n")
                .append("              ) (\n")
                .append("            _.' '._\n")
                .append("           `\"\"\"\"\"\"\"`\n");

        outputArea.setText(outputArea.getText() + "\n\n" + text);
        System.exit(0);
    }

    public void badEnding(Player player, TextBox outputArea) {
        StringBuilder text = new StringBuilder();

        text.append("There you are. You pull on the door, harder and harder, hoping it will move.\n")
                .append("Not a jolt.\n")
                .append("\n")
                .append("Hope always dies last, because you will die before it does.\n")
                .append("\n")
                .append("The air turns cold around you, not from the room, but from something inside.\n")
                .append("Your fingers tremble, still grasping the handle like it owes you something. Like someone will answer.\n")
                .append("But nothing answers here.\n")
                .append("\n")
                .append("You scream, but your voice is swallowed by the concrete.\n")
                .append("You pound on the metal until your fists are raw, then bloody.\n")
                .append("The handle is slick with your own skin.\n")
                .append("\n")
                .append("No echo.\n")
                .append("No rescue.\n")
                .append("Just the sound of your breath hitching between sobs, and the quiet drip of blood on tile.\n")
                .append("\n");

        if (!player.hasFlag("body_inspected")) {
            text.append("You sink to the floor, shaking, and finally notice what you’d been avoiding in the dark.\n")
                    .append("            Shapes.\n")
                    .append("            Bodies.\n")
                    .append("            Others, crumpled in corners. Some collapsed in prayer.\n")
                    .append("            Some still clutching notes. One face half-rotted, eyes wide open, as if still waiting for help.\n");
        }

        text.append("The students, they didn’t pass the test.\n")
                .append("Neither will you.\n")
                .append("\n")
                .append("On the wall behind you, flickering in the dying emergency light, the words “MindScale Phase IV – Human Factor Adjustment” appear on an old clipboard.\n")
                .append("Your name is handwritten on the list. Next to a date. Today.\n")
                .append("\n")
                .append("The test wasn’t to see if you could escape.\n")
                .append("It was to see how long you would try before giving up.\n")
                .append("How far you'd bleed before breaking.\n")
                .append("\n")
                .append("You close your eyes, but the darkness is no different.\n")
                .append("\n")
                .append("\n")
                .append("  ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ \n")
                .append(" ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗\n")
                .append(" ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝\n")
                .append(" ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗\n")
                .append(" ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║\n")
                .append("  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝\n")
                .append("\n")
                .append("\n")
                .append("Do you want to give up, or try again?\n")
                .append("\n")
                .append("You already know your answer.\n")
                .append("They already recorded it.\n");

        outputArea.setText(outputArea.getText() + "\n\n" + text);
        controller.showEndingPrompt();
    }

    public void teacherEnding(TextBox outputArea) {
        StringBuilder text = new StringBuilder();
        text.append("She turns slowly, her eyes glinting in the dim light.\n")
                .append("'You found me... I didn’t think anyone would get this far.'\n")
                .append("A pause. The room feels colder now.\n")
                .append("'This place — it was never meant for you. Or anyone. We tried to bury it, erase it.'\n")
                .append("She looks past you, as if seeing something distant. Or remembering.\n")
                .append("'I stayed to make sure no one would open the door again. But now it's too late.'\n")
                .append("Her voice drops to a whisper. 'They’re already after us.'\n")
                .append("The ground trembles. Lights flicker.\n")
                .append("'I’m sorry.'\n")
                .append("Everything fades.\n\n\n")
                .append("  ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ \n")
                .append(" ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗\n")
                .append(" ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝\n")
                .append(" ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗\n")
                .append(" ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║\n")
                .append("  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝\n\n\n")
                .append("Do you want to give up, or try again?\n\n")
                .append("You already know your answer.\n")
                .append("They already recorded it.\n");

        outputArea.setText(outputArea.getText() + "\n\n" + text);
        controller.showEndingPrompt();
    }

}
