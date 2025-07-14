package ui.game;

import com.googlecode.lanterna.gui2.TextBox;
import console.game.Exit;
import console.game.Player;

import java.util.List;
import java.util.Map;

public interface UIRoom {
    String getName();
    String enter(Player player);
    String performAction(Player player, String action, TextBox textBox);
    Map<String, Exit> getAvailableExits(Player player);
    String handleRoomChange(Player player, String roomName);
    List <String> getAvailableActions(Player player);
}
