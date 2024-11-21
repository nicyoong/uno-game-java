import java.util.Random;

public class ExtremeUnoCollection {
	
	final private static int MAXCARDS  = 112 * 64;
	 
	private UnoCard[] cards;
	private int numCards;
	
	public ExtremeUnoCollection() {
		cards = new UnoCard[MAXCARDS];
		numCards = 0;
	}
	
	public boolean addCard(UnoCard c) {
		
		// Don't allow us to add this card if we're full.
		if (numCards == MAXCARDS)
			return false;
		
		// Add the card and adjust the number of cards.
		cards[numCards] = c;
		numCards++;
		
		return true;
	}
	
	public UnoCard removeFromTop() {
		
		// This is how we indicate that there's no card to return.
		if (numCards == 0)
			return null;
			
		// Save the card to return, reduce the number of cards
		UnoCard retval = cards[numCards-1];
		numCards--;
		
		// Return the card.
		return retval;
	}
	
	public UnoCard remove(int index) {
		
		// The invalid case here.
		if (index < 0 || index >= numCards)
			return null;
			
		// This is the card to remove.
		UnoCard retval = cards[index];
		
		// It is easiest to copy the last card into this slot.
		cards[index] = cards[numCards-1];
		
		// Our new deck size.
		numCards--;
		
		// Return this card.
		return retval;
	}
	
	public void makeExtremeDeck(int numberOfPlayers, String gameMode) {
        // Determine how many decks are needed based on the number of players
        int decksNeeded = (int) Math.ceil((double) numberOfPlayers / 8);
        cards = new UnoCard[112 * decksNeeded]; // Initialize the cards array

        // Reset numCards for the new deck creation
        numCards = 0;

        // Loop through the required number of decks
        for (int deckCount = 0; deckCount < decksNeeded; deckCount++) {
            // Making 2 copies of each number card (1-9) and action cards (Skip, Reverse, Draw Two)
            for (int i = 0; i < 2; i++) { // Loop for number cards 1-9
                for (int j = 0; j < 4; j++) { // Loop for each color
                    for (int k = 1; k <= 9; k++) {
                        cards[numCards] = new UnoCard(j, k, gameMode);
                        numCards++;
                    }
                }
            }

            for (int j = 0; j < 4; j++) { // Loop for each color
                cards[numCards] = new UnoCard(j, 0, gameMode); // 0 card
                numCards++;
            }

            // Add 2 copies of action cards for each color
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

            // Add 4 Wild and 4 Wild Draw Four cards
            for (int i = 0; i < 4; i++) { // Loop for Wilds
                cards[numCards] = new UnoCard(-1, 13, gameMode); // Wild
                numCards++;
                cards[numCards] = new UnoCard(-2, 14, gameMode); // Wild Draw Four
                numCards++;
            }

            if ("42".equals(gameMode) || "Minecraft".equals(gameMode)) {
                for (int i = 0; i < 4; i++) { // Add 4 Special cards
                    cards[numCards] = new UnoCard(-1, 15, gameMode); // Special card
                    numCards++;
                }
            }  
        }

        // The deck is complete.
    }
    
	
	public void shuffle() {
        Random r = new Random();
    
        for (int i = numCards - 1; i > 0; i--) {
            // Generate a random index from 0 to i
            int j = r.nextInt(i + 1);
    
            // Swap cards[i] with the element at random index
            UnoCard temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }    
	
	public String toString() {
        return "Extreme Uno Collection with " + getNumCards() + " cards:\n" + super.toString();
    }
	
	// Returns the number of cards.
	public int getNumCards() {
		return numCards;
	}
	
	// Returns the card at the top of this collection.
	public UnoCard getTopCard() {
		if (numCards == 0)
			return null;
		return cards[numCards-1];
	}
	
	// Returns true iff there's any card in this collection that can be played
	// on top of c.
	public boolean canPlay(UnoCard c) {
        boolean hasPlayableCard = false; // To track if there are any playable cards
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
        if (hasPlayableCard) {
            return true; // There are playable cards other than Wild Draw Four
        } else if (hasWildDrawFour) {
            return true; // There are no other playable cards but Wild Draw Four is available
        } else {
            return false; // No playable cards at all
        }
    }

    public boolean canPlayExcludingWildDrawFour(UnoCard c) {
        // Flag to track if there are any playable cards excluding Wild Draw Four
        boolean hasPlayableCard = false;
    
        for (int i = 0; i < numCards; i++) {
            // Check if the current card can be played on the given card
            if (cards[i].canPlay(c)) {
                // If the card is playable and it's not a Wild Draw Four, set flag and break
                if (cards[i].getNumber() != 14) { // Assuming 14 is Wild Draw Four
                    hasPlayableCard = true;
                    break; // No need to check further
                }
            }
        }
    
        // Return whether there are playable cards excluding Wild Draw Four
        return hasPlayableCard;
    }
    
	
	// Returns the in position index.
	public UnoCard getCard(int index) {
		if (index >= numCards || index < 0)
			return null;
		return cards[index];
	}
}