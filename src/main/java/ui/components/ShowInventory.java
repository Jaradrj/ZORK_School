package ui.components;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import console.game.Inventory;

import java.util.Set;

public class ShowInventory {
    private final MultiWindowTextGUI gui;
    private Inventory inventory;
    private final ButtonStyling buttonStyling;

    public ShowInventory(MultiWindowTextGUI gui, Inventory inventory, ButtonStyling buttonStyling) {
        this.gui = gui;
        this.inventory = inventory;
        this.buttonStyling = buttonStyling;
    }

    public void showInventory() {
        BasicWindow inventoryWindow = new BasicWindow("Inventory");
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label("Your Inventory:"));

        ActionListBox itemListBox = new ActionListBox(new TerminalSize(30, 10));

        if (inventory.isEmpty()) {
            itemListBox.addItem("(empty)", () -> {});
        } else {
            for (String item : inventory.getItems()) {
                itemListBox.addItem(item, () -> {});
            }
        }

        panel.addComponent(itemListBox);
        panel.addComponent(new EmptySpace());

        Button closeButton = new Button("Close", inventoryWindow::close);
        closeButton.setRenderer(buttonStyling);
        panel.addComponent(closeButton);

        inventoryWindow.setComponent(panel);
        inventoryWindow.setHints(Set.of(
                Window.Hint.CENTERED,
                Window.Hint.NO_POST_RENDERING
        ));
        gui.addWindowAndWait(inventoryWindow);
    }

}