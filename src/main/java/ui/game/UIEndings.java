package ui.game;


import com.googlecode.lanterna.gui2.*;
import console.game.Player;
import ui.audio.SoundPlayer;
import ui.audio.TypingEffect;
import ui.components.Logos;
import ui.controller.UIGameController;

import java.util.Timer;
import java.util.TimerTask;

public class UIEndings {

    private UIGameController controller;

    public static boolean enteredEndings = false;

    public UIEndings(UIGameController controller) {
        this.controller = controller;
    }

    public static void happyEnding(Player player, TextBox outputArea) {

        enteredEndings = true;

        UIGameController controller = UIGameController.getCurrent();

        if (player.hasFlag("second_try")) {
            SoundPlayer.playSound("/sounds/HappyEndingSecondTry.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
        } else {
            SoundPlayer.playSound("/sounds/HappyEnding.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
        }
        String input = """
                Congratulations. You made it out. Alive.
                You called the police. You exposed the hidden doors. You told them everything.
                And for a while, it felt like justice would follow.
                But truth is rarely as simple as it seems.
                And survival doesn’t always mean freedom.
                
                Your school was known for excellence.
                A hub for “gifted youth,” praised for its scientific achievements and quietly pressured to produce results.
                Behind the high grades and smiling faculty hid something else:
                MindScale — a covert neuropsychological experiment disguised as a student performance program.
                
                It started small.
                Extra lessons. Secret “research initiatives.” Voluntary participation.
                Until it wasn’t voluntary anymore.
                
                Klara Price was the poster child.
                Top of her class. Always polite. Always ready to please.
                She wasn’t chosen randomly.
                She was conditioned. By praise, by pressure, and by Mrs. Hamps, the school psychologist.
                Hamps told her she was special. That her talent could change the world.
                All Klara had to do was help test a new compound:
                Scopolamine, a neurotoxin that suppresses resistance, enhances suggestibility, and erodes memory.
                
                Klara didn’t ask why.
                By the time she realized what was happening, it was too late.
                Her silence helped perfect a drug that killed her classmates
                and in the end, where she could hardly stay silent anymore, even herself.
                Her loyalty was her downfall.
                
                Leandro wasn’t like Klara.
                He questioned authority. Refused to play along. Noticed the missing students.
                They said he dropped out.
                But the truth? He became a liability. And liabilities were added to the test pool.
                Leandro resisted the drug. Fought the effects.
                But resistance wasn’t enough. His name was erased. His body hidden.
                He died trying to find the truth.
                """;

        if (player.hasFlag("second_try")) {
            input += "\n" + player.oldName + ", a tragedy. I don't know what exactly led them here in the first place, " +
                     "but something tells me... they've done this before. The shadows feel familiar, " +
                     "the silence more oppressive. This time, maybe they'll get it right. " +
                     "Maybe they'll uncover what the others could not.\n";
        }

        input += """
                
                Toby McConnor worked at the school for 30 years.
                He wasn’t a genius. He wasn’t on any list. He was just… observant.
                When he heard strange noises in the electrical room, he followed them.
                He crawled through a maintenance shaft, left unlocked by accident.
                They never meant for anyone to find the room.
                Especially not Toby.
                His name never made the papers. He was just “an unfortunate workplace accident.”
                
                Mrs. Hamps was supposed to protect students.
                Instead, she led the project.
                MindScale wasn’t her idea, but she executed it perfectly.
                Students deemed “emotionally unstable” or “underperforming” were marked for trials.
                They weren’t expected to survive.
                They were expected to comply.
                The experiment spiraled.
                Overdoses. Hallucinations. Deaths.
                But even then, she continued. "For the greater good."
                They say she broke down when arrested.
                But some claim she simply smiled.
                
                After your escape, the police launched an investigation.
                Hamps and three others were arrested. The school board denied knowledge.
                New programs were introduced.
                "Wellness initiatives."
                "Trauma-informed learning environments."
                But behind new posters and smiling counselors, the structure remained unchanged.
                
                Weeks later, a letter arrives at your home. No return address.
                
                Inside:
                    “Participant #27: Conclusion successful. Resistance indicates independent cognition. Memory retention: 97%. Recommendation: Proceed to Phase II.”
                
                You realize the truth.
                You weren’t the one who escaped the system.
                You were the final subject.
                
                Everything, the missing students, the clues, even your escape, it was all orchestrated.
                A test of your limits. Of your morality. Of your mind.
                
                The project never failed.
                It evolved.
                You believed you were fighting the system.
                But you were inside it the whole time.
                And if even you couldn’t tell what was real…
                how many others still think they’re awake?
                
                THE END.
                (or maybe just the next phase)
                """;

        String finalInput = input;
        new Thread(() -> {
            controller.disableActionPanel();
            try {
                Thread.sleep(17_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            TypingEffect.typeWithBanner(outputArea, finalInput, UIGameController.getGuiInstance(), null, false, false, () -> {

                waitingForEnter(() -> {
                    Logos.printBanner(Logos.trophy, outputArea);

                    TypingEffect.isWaiting.set(true);

                    waitingForEnter(() -> UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> controller.showEndingPrompt(true)));
                });
            });
        }).start();
    }

    public void badEnding(Player player, TextBox outputArea) {
        enteredEndings = true;
        if (!player.hasFlag("body_inspected")) {
            SoundPlayer.playSound("/sounds/BadEnding.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
        } else {
            SoundPlayer.playSound("/sounds/BadEndingBodyInspected.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);
        }
        String input =
                "There you are. You pull on the door, harder and harder, hoping it will move.\n" +
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
                "\n";

        if (!player.hasFlag("body_inspected")) {
            input += "You sink to the floor, shaking, and finally notice what you’d been avoiding in the dark.\n" +
                     "            Shapes.\n" +
                     "            Bodies.\n" +
                     "            Others, crumpled in corners. Some collapsed in prayer.\n" +
                     "            Some still clutching notes. One face half-rotted, eyes wide open, as if still waiting for help.\n";
        }

        input +=
                "The students, they didn’t pass the test.\n" +
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
                "\n\n Press ENTER to continue";

        String finalInput = input;
        new Thread(() -> {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            TypingEffect.typeWithBanner(outputArea, finalInput, UIGameController.getGuiInstance(), null, false, false, () -> {

                waitingForEnter(() -> {

                    Logos.printBanner(Logos.banner, outputArea);

                    TypingEffect.isWaiting.set(true);

                    waitingForEnter(() -> UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> controller.showEndingPrompt(false)));
                });
            });
        }).start();
    }

    public void teacherEnding(TextBox outputArea) {

        enteredEndings = true;

        SoundPlayer.playSound("/sounds/TeacherEnding.wav", 0, 0, outputArea, UIGameController.getGuiInstance(), false);

        String narrative = """
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
                
                
                Press ENTER to continue
                """;

        TypingEffect.typeWithBanner(outputArea, narrative, UIGameController.getGuiInstance(), "TeacherEnding.wav", false, false, () -> {

            waitingForEnter(() -> {
                Logos.printBanner(Logos.banner, outputArea);

                TypingEffect.isWaiting.set(true);

                waitingForEnter(() -> UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> controller.showEndingPrompt(false)));
            });
        });
    }

    public void lightSwitchEnding(TextBox outputArea) {

        enteredEndings = true;

        String story = """
                You walk over to the light switch and flip it.
                At first nothing happens. But then...
                Your hand freezes in place.
                Your body trembles, heart pounding faster.
                You smell the smoke and see the flickering lights.
                And then ... nothing.
                """;

        TypingEffect.typeWithBanner(outputArea, story, UIGameController.getGuiInstance(), null, true, false, () -> {

            waitingForEnter(() -> {
                Logos.printBanner(Logos.banner, outputArea);

                TypingEffect.isWaiting.set(true);

                waitingForEnter(() -> UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> controller.showEndingPrompt(false)));
            });
        });
    }

    public void brewingEnding(Player player, TextBox outputArea) {

        enteredEndings = true;

        SoundPlayer.playSound("/sounds/Brewing.wav", 10000, 0, outputArea, UIGameController.getGuiInstance(), false);

        String story = """
                You mix the chemicals together and wait for that faint, familiar sizzling sound.
                It's not your first time brewing something, but it’s the first time you have no idea what the outcome will be.
                Scopolamine. 
                "What is it? What am I trying to brew?"
                Those thoughts swirl through your mind, until they're interrupted.
                
                The sizzling begins. It worked.
                You step closer to the mixture and take a look at the pale yellow liquid.
                It smells oddly appealing.
                A little bitter, but also earthly. Like a flower, or nature itself.
                You lean in even closer.
                The smell draws you in.
                You don’t even notice how dizzy you’ve become.
                You stumble back, but it's too late.
                The air thickens, making it difficult to breathe.
                Your vision is blurred, your mouth dry.
                You start to panic, increasing your already racing heartbeat. 
                
                You hear steps, or do you? A shadow appears in front of you. Or was it never there?
                Suddenly a face appears in front of you. 
                Is that...Mrs. Hamps? 
                No, no that can't be. 
                It disappears again. 
                "What is happening", you mumble.
                You try to to lean against the wall, but your knees give in.
                You collapse, hitting the ground hard.
                You want to get up, but your body trembles.
                Barely being able to keep your eyes open you stare into the distance.
                While staring at the Scopolamine, you begin regret ever touching it. But it's too late, too late to regret anything, too late to change anything.
                
                """;

        if (player.hasFlag("second_try")) {
            story +=
                    """
                    A face appears again. Sad. Angry. But also... caring.
                    It's  """+ player.oldName + """   
                    
                    That can't be. 
                    "I must be seeing things", you think to yourself.
                    
                        "Why did you try and find us? 
                        You should've never come here.
                        I told you ... I told you not to look for me if I didn’t come back.
                        And now, now we both share the same fate."
                    
                    You want to answer, but you're too weak, too tired.
                   
                    """;
        }

        story += """
                Your eyes begin to close.
                Just before you lose consciousness, a pair of hands lift you up and carry you away.
                """;

        TypingEffect.typeWithBanner(outputArea, story, UIGameController.getGuiInstance(), null, true, false, () -> {

            waitingForEnter(() -> {
                Logos.printBanner(Logos.banner, outputArea);

                TypingEffect.isWaiting.set(true);

                waitingForEnter(() -> UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> controller.showEndingPrompt(false)));
            });
        });
    }


    public static void waitingForEnter(Runnable callback) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!TypingEffect.isWaiting.get()) {
                    timer.cancel();
                    UIGameController.getGuiInstance().getGUIThread().invokeLater(callback);
                }
            }
        }, 0, 100);
    }
}


