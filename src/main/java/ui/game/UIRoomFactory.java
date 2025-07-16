package ui.game;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import ui.components.Logos;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import ui.rooms.*;

public class UIRoomFactory {

    private static UIGameController controller;
    private static UICommands commands;
    private static MultiWindowTextGUI gui;
    private static TextPrinter printer;
    private static Logos logos;

    public static void setController(UIGameController controller) {
        UIRoomFactory.controller = controller;
    }
    public static void setCommands(UICommands commands) {
        UIRoomFactory.commands = commands;
    }
    public static void setPrinter(TextPrinter printer) {
        UIRoomFactory.printer = printer;
    }
    public static void setLogos(Logos logos) {
        UIRoomFactory.logos = logos;
    }

    public static UIRoom createRoom(String name) {
        return switch (name) {
            case "main entrance hall" -> new UIMainEntranceRoom(commands, printer
            );
            case "music room" -> new UIMusicRoom(commands);
            case "it room" -> new UIITRoom(commands);
            case "cafeteria" -> new UICafeteria(commands);
            case "chemistry room" -> new UIChemistryRoom(commands, printer);
            case "printer room" -> new UIPrinterRoom(commands, printer);
            case "sportshall" -> new UISportshall(commands);
            case "secretary" -> new UISecretary(commands);
            case "garage" -> new UIGarage();
            case "teacher room" -> new UITeacherRoom(controller, commands, gui, printer, logos);
            case "electricity room" -> new UIElectricityRoom(controller, commands, gui, printer, logos);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
