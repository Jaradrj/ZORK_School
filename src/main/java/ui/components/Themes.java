package ui.components;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;

public class Themes {

    public static final SimpleTheme defaultTheme = SimpleTheme.makeTheme(
            true,
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK_BRIGHT,
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK,
            TextColor.ANSI.BLACK,
            TextColor.ANSI.WHITE,
            TextColor.ANSI.BLACK_BRIGHT
    );


    public static final SimpleTheme redTheme = SimpleTheme.makeTheme(
            true,
            new TextColor.RGB(200, 60, 60),
            new TextColor.RGB(10, 10, 10),
            new TextColor.RGB(255, 100, 100),
            new TextColor.RGB(30, 0, 0),
            new TextColor.RGB(60, 0, 0),
            new TextColor.RGB(120, 0, 0),
            new TextColor.RGB(20, 0, 0)
    );

    public static final SimpleTheme yellowTheme = SimpleTheme.makeTheme(
            true,
            new TextColor.RGB(255, 182, 0),
            new TextColor.RGB(25, 25, 25),
            new TextColor.RGB(255, 220, 0),
            new TextColor.RGB(20, 20, 20),
            new TextColor.RGB(25, 30, 45),
            new TextColor.RGB(255, 200, 50),
            new TextColor.RGB(25, 25, 30)
    );

    public static final SimpleTheme blueTheme = SimpleTheme.makeTheme(
            true,
            new TextColor.RGB(100, 150, 255),
            new TextColor.RGB(10, 10, 30),
            new TextColor.RGB(180, 210, 255),
            new TextColor.RGB(20, 20, 50),
            new TextColor.RGB(70, 90, 140),
            new TextColor.RGB(220, 230, 255),
            new TextColor.RGB(25, 25, 60)
    );

    public static final SimpleTheme greenTheme = SimpleTheme.makeTheme(
            true,
            new TextColor.RGB(100, 220, 130),
            new TextColor.RGB(10, 30, 10),
            new TextColor.RGB(180, 255, 180),
            new TextColor.RGB(20, 50, 20),
            new TextColor.RGB(70, 140, 70),
            new TextColor.RGB(220, 255, 220),
            new TextColor.RGB(25, 60, 25)
    );


    public static class TextColors {

        public static final TextColor redText = new TextColor.RGB(200, 60, 60);
        public static final TextColor greenText = new TextColor.RGB(100, 220, 130);
        public static final TextColor blueText = new TextColor.RGB(100, 150, 255);
        public static final TextColor yellowText = new TextColor.RGB(255, 182, 0);
        public static final TextColor defaultText = TextColor.ANSI.WHITE;
        public static final TextColor blackText = new TextColor.RGB(0, 0, 0);

        public static final TextColor.ANSI defaultBg = TextColor.ANSI.BLACK_BRIGHT;
        public static final TextColor redBg = new TextColor.RGB(10, 10, 10);
        public static final TextColor blueBg = new TextColor.RGB(10, 10, 30);
        public static final TextColor greenBg = new TextColor.RGB(10, 30, 10);
        public static final TextColor yellowBg = new TextColor.RGB(25, 25, 25);
        public static final TextColor lightBg = new TextColor.RGB(255, 255, 255);

    }


}

