import java.util.Random;

public class UnoCard {

    final private static String[] colors = {"Yellow", "Red", "Green", "Blue"};
    final private static String[] actionNames = {"Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four"};
    
    private int color; // 0-3 for colors, -1 for wild cards initially with no color
    private int number; // 0-9 for numbers; 10: Skip, 11: Reverse, 12: Draw Two, 13: Wild, 14: Wild Draw Four

    // Constructor for creating a random card, including action cards and wilds
    public UnoCard(Random r) {
        number = Math.abs(r.nextInt() % 15); // Generates a number between 0 and 14
        if (number <= 9) { 
            color = Math.abs(r.nextInt() % 4); // Color set for number cards
        } else if (number <= 12) { 
            color = Math.abs(r.nextInt() % 4); // Color set for action cards like Skip, Reverse, Draw Two
        } else {
            color = -1; // No color initially for Wild and Wild Draw Four
        }
    }

    // Constructor for specific cards, including Wild cards which may have no color initially
    public UnoCard(int color, int number) {
        this.color = color;
        this.number = number;
    }

    // toString method to represent the card
    public String toString() {
        if (number >= 10) { // Action cards and Wilds
            return (number >= 13 ? actionNames[number - 10] : colors[color] + " " + actionNames[number - 10]);
        }
        return colors[color] + " " + number; // Regular colored number cards
    }

    // Allow Wild and Wild Draw Four cards to set their color
    public void setColor(int newColor) {
        if (number == 13 || number == 14) {  // Only allow setting color for Wild and Wild Draw Four
            this.color = newColor;
        } else {
            throw new UnsupportedOperationException("Only Wild cards can change color.");
        }
    }

    // Check if this card can be played on another card
    public boolean canPlay(UnoCard other) {
        return this.color == other.color || this.number == other.number || this.color == -1 || this.number == 14;
    }

    // Getters for color and number
    public int getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    // compareTo method for comparing cards (for sorting or rules)
    public int compareTo(UnoCard other) {
        if (this.color < other.color) return -1;
        else if (this.color > other.color) return 1;
        
        if (this.number < other.number) return -1;
        else if (this.number > other.number) return 1;

        return 0;
    }
}
