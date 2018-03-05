package rasos;

public class ColorUtils {
    public static final String ANSI_YELLOW = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    static final String ANSI_RESET_COLOR = "\u001B[0m";

    public static String colorString(String str, String color) {
        return String.format("%s%s%s", color, str, ANSI_RESET_COLOR);
    }
}
