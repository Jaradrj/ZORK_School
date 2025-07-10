import controller.GameController;
import game.Commands;
import game.RoomFactory;
import game.Room;

import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<String, Room> roomMap = new HashMap<>();
		String[] roomNames = {
				"main entrance hall",
				"music room",
				"printer room",
				"secretary",
				"cafeteria",
				"garage",
				"it room",
				"sportshall",
				"chemistry room",
				"electricity room",
				"teacher room"
		};

		for (String name : roomNames) {
			roomMap.put(name, RoomFactory.createRoom(name));
		}

		Commands commands = new Commands(roomMap);
		GameController controller = new GameController(commands);
		RoomFactory.setController(controller);
		controller.run();
	}
}
