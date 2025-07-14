package console.game;

import java.util.Map;

public interface Room {
    String getName();
    void enter(Player player);
    String performAction(Player player, String action);
    Map<String, Exit> getAvailableExits(Player player);
    String handleRoomChange(Player player, String roomName);
}
