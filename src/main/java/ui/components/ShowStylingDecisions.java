package ui.components;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import ui.game.StylingDecision;

import java.util.Set;

public class ShowStylingDecisions {
    private final MultiWindowTextGUI gui;
    private final StylingDecision decision;
    private final ButtonStyling buttonStyling;

    public ShowStylingDecisions(MultiWindowTextGUI gui, StylingDecision decision, ButtonStyling buttonStyling) {
        this.gui = gui;
        this.decision = decision;
        this.buttonStyling = buttonStyling;
    }

    public void showStyleActions() {
        BasicWindow styleWindow = new BasicWindow("Styles");
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));

        for (StylingDecision.ButtonDescription buttonDesc : decision.getStyles()) {
            Panel row = new Panel(new LinearLayout(Direction.HORIZONTAL));
            row.addComponent(buttonDesc.getButton());
            row.addComponent(new Label(" " + buttonDesc.getDescription()));
            panel.addComponent(row);
        }

        panel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
        Button closeButton = new Button("Close", styleWindow::close);
        closeButton.setRenderer(buttonStyling);
        panel.addComponent(closeButton);


        styleWindow.setComponent(panel);
        styleWindow.setHints(Set.of(
                Window.Hint.CENTERED,
                Window.Hint.NO_POST_RENDERING
        ));

        gui.addWindowAndWait(styleWindow);
    }

}
