import controller.GameController;
import game.Commands;

public class Main {
	public static void main(String[] args) {
		Commands command = null;
		GameController controller = new GameController(null);
		controller.run();
	}
}
