package ui.components;

import com.googlecode.lanterna.gui2.TextBox;
import ui.controller.UIGameController;

public class Logos {

    private static TextPrinter printer;

    public Logos(TextPrinter printer) {
        Logos.printer = printer;
    }

    public static String banner = """
            
              ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗ 
             ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗
             ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝
             ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║██║   ██║██╔══╝  ██╔══██╗
             ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝╚██████╔╝███████╗██║  ██║
              ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝  ╚═════╝ ╚══════╝╚═╝  ╚═╝
            
            """;

    public static String trophy = """
                          ___________
                         '._==_==_=_.' 
                         .-\\:      /-. 
                        | (|:.     |) | 
                         '-|:.     |-' 
                           \\::.    / 
                            '::. .' 
                              ) ( 
                            _.' '._ 
                           `^^^^^^^^` 
            """;


    public static String itLogo = """
                    [ACCESSING TERMINAL...]
                    
                       ███████╗██╗███╗   ██╗██████╗ ███████╗███╗   ███╗
                       ██╔════╝██║████╗  ██║██╔══██╗██╔════╝████╗ ████║
                       █████╗  ██║██╔██╗ ██║██║  ██║█████╗  ██╔████╔██║
                       ██╔══╝  ██║██║╚██╗██║██║  ██║██╔══╝  ██║╚██╔╝██║
                       ██║     ██║██║ ╚████║██████╔╝███████╗██║ ╚═╝ ██║
                       ╚═╝     ╚═╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝     ╚═╝
                    """;


    public static void printBanner(String logo, TextBox outputArea) {
        UIGameController.getGuiInstance().getGUIThread().invokeLater(() -> printer.textPrinter(logo, outputArea));
    }

}
