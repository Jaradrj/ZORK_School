package controller;

import game.*;

import java.util.Scanner;

public class GameController {
    private Player player;
    private Room startRoom;
    private Commands command;

    public GameController(Commands command) {
        this.command = command;
        this.player = new Player();
        this.startRoom = RoomFactory.createRoom("main entrance hall");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        printStart();
        player.setCurrentRoom(startRoom);

        while (true) {
            Room currentRoom = player.getCurrentRoom();
            currentRoom.enter(player);

            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.startsWith("-") || input.equalsIgnoreCase("h") || input.equalsIgnoreCase("i") || input.equalsIgnoreCase("r")) {
                command.checkInputCommands(input, player);
            } else {
                String result = currentRoom.performAction(player, input);
                System.out.println(result);
            }

        }
    }

    public void printStart() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("░▒▓██████████████▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓███████▓▒░ ░▒▓███████▓▒░░▒▓██████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░      ░▒▓████████▓▒░ \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░░▒▓██████▓▒░░▒▓█▓▒░      ░▒▓████████▓▒░▒▓█▓▒░      ░▒▓██████▓▒░   \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░      ░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░      ░▒▓█▓▒░        \n" +
                            "░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓█▓▒░▒▓█▓▒░░▒▓█▓▒░▒▓███████▓▒░░▒▓███████▓▒░ ░▒▓██████▓▒░░▒▓█▓▒░░▒▓█▓▒░▒▓████████▓▒░▒▓████████▓▒░ \n" +
                            "                                                                                                                       \n" +
                            "                                                                                                                       \n" +
                            "\n" +
                "Welcome to MindScale\n" +
                "\n" +
                "Several students have disappeared without a trace in recent months.  \n" +
                "No farewell. No clues. Only silence.\n" +
                "\n" +
                "Rumors whisper of a secret program that promises more than just education.  \n" +
                "A connection between power, control and something dark.\n" +
                "\n" +
                "You're one of the few left, but that's about to change.  \n" +
                "Now it's up to you to find out what really happened.\n" +
                "\n" +
                "Find the missing, before it's too late.\n" +
                "\n" +
                "Press ENTER to start...\n\n\n");

        scanner.nextLine();
        player.oldName = player.name;
        System.out.println("Before you start, what's your name?");
        player.name = scanner.nextLine();
        System.out.printf("Welcome, %s!\n\n", player.name);
    }
}