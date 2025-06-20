package uno.cards;

import java.util.Random;

public class CollectionOfUnoCards {

    final private static int MAXCARDS = 112;

    private final UnoCard[] cards;
    private int numCards;

    public CollectionOfUnoCards() {
        cards = new UnoCard[MAXCARDS];
        numCards = 0;
    }

    public boolean addCard(UnoCard c) {

        if (numCards == MAXCARDS)
            return false;

        cards[numCards] = c;
        numCards++;

        return true;
    }

    public UnoCard removeFromTop() {

        if (numCards == 0)
            return null;

        UnoCard retval = cards[numCards - 1];
        numCards--;

        return retval;
    }

    public UnoCard remove(int index) {

        // The invalid case here.
        if (index < 0 || index >= numCards)
            return null;

        // This is the card to remove.
        UnoCard retval = cards[index];

        // It is easiest to copy the last card into this slot.
        cards[index] = cards[numCards - 1];

        // Our new deck size.
        numCards--;

        // Return this card.
        return retval;
    }

    public void makeDeck(String gameMode) {
        // This clears our deck!
        numCards = 0;

        // Making 2 copies of each number card (1-9) and action uno.cards (Skip, Reverse, Draw Two)
        for (int i = 0; i < 2; i++) { // Loop for number uno.cards 1-9
            for (int j = 0; j < 4; j++) { // Loop for each color
                for (int k = 1; k <= 9; k++) {
                    cards[numCards] = new UnoCard(j, k, gameMode);
                    numCards++;
                }
            }
        }

        // Add one 0 card for each color
        for (int j = 0; j < 4; j++) { // Loop for each color
            cards[numCards] = new UnoCard(j, 0, gameMode); // 0 card
            numCards++;
        }

        // Add 2 copies of action uno.cards for each color
        for (int i = 0; i < 2; i++) { // Loop for each action
            for (int j = 0; j < 4; j++) { // Loop for each color
                cards[numCards] = new UnoCard(j, 10, gameMode); // Skip
                numCards++;
                cards[numCards] = new UnoCard(j, 11, gameMode); // Reverse
                numCards++;
                cards[numCards] = new UnoCard(j, 12, gameMode); // Draw Two
                numCards++;
            }
        }

        // Add 4 Wild and 4 Wild Draw Four uno.cards
        for (int i = 0; i < 4; i++) { // Loop for Wilds
            cards[numCards] = new UnoCard(-1, 13, gameMode); // Wild
            numCards++;
            cards[numCards] = new UnoCard(-2, 14, gameMode); // Wild Draw Four
            numCards++;
        }

        if ("42".equals(gameMode) || "Minecraft".equals(gameMode)) {
            for (int i = 0; i < 4; i++) { // Add 4 Special uno.cards
                cards[numCards] = new UnoCard(-1, 15, gameMode); // Special card
                numCards++;
            }
        }

        // The deck is complete.
    }


    public void shuffle() {
        Random r = new Random();

        for (int i = numCards - 1; i > 0; i--) {
            // Generate a random index from 0 to i
            int j = r.nextInt(i + 1);

            // Swap uno.cards[i] with the element at random index
            UnoCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    public String toString() {

        String answer = "";

        for (int i = 0; i < numCards; i++)
            answer = answer + i + ". " + cards[i] + "\n";

        return answer;
    }

    // Returns the number of uno.cards.
    public int getNumCards() {
        return numCards;
    }

    // Returns the card at the top of this collection.
    public UnoCard getTopCard() {
        if (numCards == 0)
            return null;
        return cards[numCards - 1];
    }

    // Returns true if there's any card in this collection that can be played
    // on top of c.
    public boolean canPlay(UnoCard c) {
        boolean hasPlayableCard = false; // To track if there are any playable uno.cards
        boolean hasWildDrawFour = false; // To track if there is a Wild Draw Four

        for (int i = 0; i < numCards; i++) {
            if (cards[i].canPlay(c)) {
                // If there is a playable card other than Wild Draw Four, set flag and break
                if (cards[i].getNumber() != 14) { // Assuming 14 is Wild Draw Four
                    hasPlayableCard = true;
                    break; // No need to check further
                }
            }
            // Check if we have a Wild Draw Four in hand
            if (cards[i].getNumber() == 14) { // Wild Draw Four
                hasWildDrawFour = true;
            }
        }

        // Determine return value based on the flags
        // No playable uno.cards at all
        if (hasPlayableCard) {
            return true; // There are playable uno.cards other than Wild Draw Four
        } else return hasWildDrawFour; // There are no other playable uno.cards but Wild Draw Four is available
    }

    public boolean canPlayExcludingWildDrawFour(UnoCard topCard) {
        // Look for any non–Wild-Draw-Four card in hand that matches the current color
        for (int i = 0; i < numCards; i++) {
            UnoCard card = cards[i];
            if (card.getNumber() == 14) {
                continue;
            }
            if (card.getColor() == topCard.getColor()) {
                return true;
            }
        }
        return false;
    }

    // Returns the in position index.
    public UnoCard getCard(int index) {
        if (index >= numCards || index < 0)
            return null;
        return cards[index];
    }
}