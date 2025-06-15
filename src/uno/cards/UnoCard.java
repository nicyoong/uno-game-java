package uno.cards;

import java.util.Random;

public class UnoCard {

    final private static String[] colors = {"Yellow", "Red", "Green", "Blue"};
    final private static String[] actionNames = {"Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four"};
    final private static String[] specialNames = {"SegFault", "Creeper", "Special"};

    private int color; // 0-3 for colors, -1 for wild uno.cards initially with no color
    private final int number; // 0-9 for numbers; 10: Skip, 11: Reverse, 12: Draw Two, 13: Wild, 14: Wild Draw Four, 15: SegFault
    private String gameMode;

    // Constructor for creating a random card, including action uno.cards and wilds
    public UnoCard(Random r) {
        number = Math.abs(r.nextInt() % 16); // Generates a number between 0 and 15
        if (number <= 9) {
            color = Math.abs(r.nextInt() % 4); // Color set for number uno.cards
        } else if (number <= 12) {
            color = Math.abs(r.nextInt() % 4); // Color set for action uno.cards like Skip, Reverse, Draw Two
        } else {
            color = -1; // No color initially for Wild and Wild Draw Four and Special
        }
    }

    // Constructor for specific uno.cards, including Wild uno.cards which may have no color initially
    public UnoCard(int color, int number, String gameMode) {
        this.color = color;
        this.number = number;
        this.gameMode = gameMode;
    }

    public static String getColorName(int colorNumber) {
        switch (colorNumber) {
            case 0:
                return "Yellow";
            case 1:
                return "Red";
            case 2:
                return "Green";
            case 3:
                return "Blue";
            default:
                return "Unknown";
        }
    }

    // toString method to represent the card
    public String toString() {
        if (number == 15) {
            // Check game mode for handling number 15
            if ("42".equals(gameMode)) {
                return specialNames[0]; // SegFault
            } else if ("Minecraft".equals(gameMode)) {
                return specialNames[1]; // Creeper
            } else {
                return specialNames[2]; // Special
            }
        }
        if (number >= 10) { // Action uno.cards and Wilds
            return (number >= 13 ? actionNames[number - 10] : colors[color] + " " + actionNames[number - 10]);
        }
        return colors[color] + " " + number; // Regular colored number uno.cards
    }

    public boolean canPlay(UnoCard other) {
        boolean canPlayCard = (this.color == other.color || this.number == other.number || isWild());
        return canPlayCard;
    }

    private boolean isWild() {
        boolean isWildCard = (this.number >= 13 && this.number <= 15);
        return isWildCard;
    }

    public boolean isCreeper() {
        return number == 15 && "Minecraft".equals(gameMode);
    }

    // Getters for color and number
    public int getColor() {
        return color;
    }

    // Allow Wild, Wild Draw Four and SegFault uno.cards to set their color
    public void setColor(int newColor) {
        if (number >= 13 || number <= 15) {
            this.color = newColor;
        } else {
            throw new UnsupportedOperationException("Only Wild uno.cards can change color.");
        }
    }

    public int getNumber() {
        return number;
    }

    // compareTo method for comparing uno.cards (for sorting or rules)
    public int compareTo(UnoCard other) {
        if (this.color < other.color) return -1;
        else if (this.color > other.color) return 1;

        if (this.number < other.number) return -1;
        else if (this.number > other.number) return 1;

        return 0;
    }
}
