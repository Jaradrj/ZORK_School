package ui.game;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import ui.components.Logos;
import ui.components.TextPrinter;
import ui.controller.UIGameController;
import ui.rooms.*;

public class UIRoomFactory {

    private static UIGameController controller;
    private static MultiWindowTextGUI gui;
    private static TextPrinter printer;
    private static Logos logos;

    public static void setController(UIGameController controller) {
        UIRoomFactory.controller = controller;
    }
    public static void setPrinter(TextPrinter printer) {
        UIRoomFactory.printer = printer;
    }
    public static void setLogos(Logos logos) {
        UIRoomFactory.logos = logos;
    }

    public static UIRoom createRoom(String name) {
        return switch (name) {
            case "main entrance hall" -> new UIMainEntranceRoom(printer, controller);
            case "music room" -> new UIMusicRoom();
            case "it room" -> new UIITRoom();
            case "cafeteria" -> new UICafeteria();
            case "chemistry room" -> new UIChemistryRoom(printer, controller);
            case "printer room" -> new UIPrinterRoom(printer);
            case "sportshall" -> new UISportshall();
            case "secretary" -> new UISecretary();
            case "garage" -> new UIGarage();
            case "teacher room" -> new UITeacherRoom(controller);
            case "electricity room" -> new UIElectricityRoom(controller, printer);
            default -> throw new IllegalArgumentException("Unknown room " + name);
        };
    }
}
