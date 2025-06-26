package controller;

import game.*;

public class GameController {
    private Player player;

    public GameController() {
        this.player = new Player();
        Room startRoom = RoomFactory.createRoom("Main Entrance");
        player.setCurrentRoom(startRoom);
        startRoom.enter(player);
    }

    public void run() {
        System.out.println("Test");
    }
}
