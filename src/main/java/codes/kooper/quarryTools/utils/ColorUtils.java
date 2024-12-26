package codes.kooper.quarryTools.utils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorUtils {
    private static final Map<String, Color> NAMED_COLORS = new HashMap<>();

    static {
        NAMED_COLORS.put("white", new Color(255, 255, 255));
        NAMED_COLORS.put("black", new Color(0, 0, 0));
        NAMED_COLORS.put("red", new Color(255, 0, 0));
        NAMED_COLORS.put("green", new Color(0, 255, 0));
        NAMED_COLORS.put("dark_blue", new Color(0, 0, 255));
        NAMED_COLORS.put("yellow", new Color(255, 255, 0));
        NAMED_COLORS.put("cyan", new Color(0, 255, 255));
        NAMED_COLORS.put("magenta", new Color(255, 0, 255));
        NAMED_COLORS.put("gray", new Color(128, 128, 128));
        NAMED_COLORS.put("dark_gray", new Color(64, 64, 64));
        NAMED_COLORS.put("light_gray", new Color(192, 192, 192));
        NAMED_COLORS.put("orange", new Color(255, 165, 0));
        NAMED_COLORS.put("pink", new Color(255, 192, 203));
        NAMED_COLORS.put("purple", new Color(128, 0, 128));
    }

    /**
     * Extracts the RGB color from a color string.
     *
     * @param color The color string (e.g., "#FF5733", "red", "rgb(255, 0, 128)").
     * @return An array [R, G, B] where each value is 0-255.
     */
    public static int[] getRGB(String color) {
        if (color == null || color.isEmpty()) {
            return new int[] { 255, 255, 255 }; // Default to white
        }

        // Handle HEX colors (e.g., #FF5733)
        if (color.startsWith("#") && color.length() == 7) {
            try {
                int red = Integer.parseInt(color.substring(1, 3), 16);
                int green = Integer.parseInt(color.substring(3, 5), 16);
                int blue = Integer.parseInt(color.substring(5, 7), 16);
                return new int[] { red, green, blue };
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Handle RGB() format (e.g., rgb(255, 0, 128))
        if (color.toLowerCase().startsWith("rgb(") && color.endsWith(")")) {
            try {
                String innerValues = color.substring(4, color.length() - 1);
                String[] rgbValues = innerValues.split(",");
                if (rgbValues.length == 3) {
                    int red = Integer.parseInt(rgbValues[0].trim());
                    int green = Integer.parseInt(rgbValues[1].trim());
                    int blue = Integer.parseInt(rgbValues[2].trim());
                    return new int[] { red, green, blue };
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Handle named colors (e.g., "red", "blue", etc.)
        if (NAMED_COLORS.containsKey(color.toLowerCase())) {
            Color namedColor = NAMED_COLORS.get(color.toLowerCase());
            return new int[] { namedColor.getRed(), namedColor.getGreen(), namedColor.getBlue() };
        }

        // If all else fails, return white (fallback)
        return new int[] { 255, 255, 255 }; // Default to white
    }

    /**
     * Converts an RGB array to a string for display purposes.
     *
     * @param rgb An array [R, G, B] where each value is 0-255.
     * @return A string representing the RGB values (e.g., "RGB(255, 0, 128)").
     */
    public static String rgbToString(int[] rgb) {
        if (rgb == null || rgb.length != 3) return "RGB(255, 255, 255)";
        return String.format("RGB(%d, %d, %d)", rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Converts an RGB array to a hex color string.
     *
     * @param rgb An array [R, G, B] where each value is 0-255.
     * @return A hex string (e.g., "#FF5733").
     */
    public static String rgbToHex(int[] rgb) {
        if (rgb == null || rgb.length != 3) return "#FFFFFF"; // Default to white
        return String.format("#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
    }
}

