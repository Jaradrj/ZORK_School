package ui.game;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;

import java.util.Set;

public class ShowHelpActions {
    private final MultiWindowTextGUI gui;
    private final HelpActions actions;

    public ShowHelpActions(MultiWindowTextGUI gui, HelpActions actions) {
        this.gui = gui;
        this.actions = actions;
    }

    public void showHelpActions() {
        BasicWindow helpWindow = new BasicWindow("Help");
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        Table<String> actionTable = new Table<>("Command: ", "Description ");

        for (HelpActions.HelpAction action : actions.getActions()) {
            actionTable.getTableModel().addRow(action.getKey(), action.getDescription());
        }

        panel.addComponent(actionTable);
        panel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

        Button closeButton = new Button("Close", helpWindow::close);
        panel.addComponent(closeButton);

        helpWindow.setComponent(panel);
        helpWindow.setHints(Set.of(Window.Hint.CENTERED));

        gui.addWindowAndWait(helpWindow);
    }
}
