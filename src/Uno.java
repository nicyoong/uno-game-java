import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Uno {

    final private static int NUMCARDSHAND = 7;
    final private static int MIN_PLAYERS = 2;
    final private static int MAX_PLAYERS = 10;

    public CollectionOfUnoCards deck;
    private CollectionOfUnoCards discardPile;
    private List<CollectionOfUnoCards> hands;
    public int currentPlayerIndex;
    private int numPlayers;
    private List<Integer> finishingOrder;
    private boolean isClockwise = true;
    private CardEffectHandler cardEffectHandler;

    public Uno(int numPlayers, String gameMode) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Number of players must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        this.numPlayers = numPlayers;
        this.finishingOrder = new ArrayList<>();

        // Initialize the deck and shuffle it.
        deck = new CollectionOfUnoCards();
        deck.makeDeck(gameMode);
        deck.shuffle();

        // Discard Pile
        discardPile = new CollectionOfUnoCards();

        // Draw a card to start the game
        UnoCard startingCard = deck.removeFromTop();

        // Create hands for each player.
        hands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            hands.add(new CollectionOfUnoCards());
        }

        // Deal cards to each player.
        for (int i = 0; i < NUMCARDSHAND; i++) {
            for (int j = 0; j < numPlayers; j++) {
                hands.get(j).addCard(deck.removeFromTop());
            }
        }
        
        discardPile.addCard(startingCard);

        currentPlayerIndex = 0; // Start with the first player

        // Initialize the CardEffectHandler
        cardEffectHandler = new CardEffectHandler();

        // 42 gamemode affects this
        cardEffectHandler.handleStartingCardEffect(startingCard, 0, gameMode, this);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        currentPlayerIndex = index;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean direction) {
        isClockwise = direction;
    }

    public void checkForWinner(int playerIndex) {
        // Check if the current player finished their cards
        if (hands.get(playerIndex).getNumCards() == 0) {
            System.out.println("Player " + (playerIndex + 1) + " has finished!");
            
            finishingOrder.add(playerIndex); // Add the player to the finishing order
            
            // Declare winner if this is the first player finishing
            if (finishingOrder.size() == 1) {
                System.out.println("Player " + (playerIndex + 1) + " won!");
            }
        }
    }

    public void playGame(String gameMode) {
        Scanner stdin = new Scanner(System.in);
        System.out.println("Starting the game with " + numPlayers + " players!");

        // Display initial hands
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + ", here is your hand:\n" + hands.get(i));
        }

        System.out.println("The card at the top of the discard pile is " + discardPile.getTopCard());

        while (finishingOrder.size() < numPlayers - 1) {
            // Only play the turn if the current player has not finished
            if (!finishingOrder.contains(currentPlayerIndex)) {
                playTurn(currentPlayerIndex, gameMode);
                checkForWinner(currentPlayerIndex);
            }
            
            // Move to the next player
            currentPlayerIndex = getNextActivePlayer(currentPlayerIndex);
        
            // Check if the deck is empty and shuffle the discard pile if necessary
            if (deck.getNumCards() == 0) {
                shuffleDiscardPileIntoDeck();
            }
        }

        // Determine the last player and count their remaining cards
        int lastPlayerIndex = -1;
        int lastPlayerCardCount = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (!finishingOrder.contains(i)) {
                lastPlayerIndex = i;
                lastPlayerCardCount = hands.get(i).getNumCards();
                break; // Found the last player who hasn't finished
            }
        }

        // Print results
        System.out.println("\nGame Over!");
        System.out.println("Player rankings:");
        for (int i = 0; i < finishingOrder.size(); i++) {
            System.out.println((i + 1) + ": Player " + (finishingOrder.get(i) + 1) + " (finished)");
        }

        // Print the last player
        if (lastPlayerIndex != -1) {
            System.out.println((finishingOrder.size() + 1) + ": Player " + (lastPlayerIndex + 1) + " (still has " + lastPlayerCardCount + " cards)");
        }

        // printResult();
        stdin.close(); // Close Scanner at the end of the game
    }

    public void printResult() {
        if (deck.getNumCards() == 0) {
            System.out.println("Sorry, the game has ended in a draw.");
        } else {
            for (int i = 0; i < numPlayers; i++) {
                if (hands.get(i).getNumCards() == 0) {
                    System.out.println("Player " + (i + 1) + " wins!");
                    return;
                }
            }
        }
    }

    public int promptColorSelection(int player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + (player + 1) + ", choose a color:");
        System.out.println("1: Yellow, 2: Red, 3: Green, 4: Blue");
        
        int colorChoice = 0; // Initialize to 0 (invalid)
        while (colorChoice < 1 || colorChoice > 4) {
            System.out.print("Enter the color number: ");
            colorChoice = scanner.nextInt();
            if (colorChoice < 1 || colorChoice > 4) {
                System.out.println("Invalid choice. Please choose between 1 and 4.");
            }
        }
        return colorChoice - 1;
    }
    

    public int getNextPlayer(int currentPlayer) {
        if (isClockwise) {
            return (currentPlayer + 1) % numPlayers;
        } else {
            return (currentPlayer - 1 + numPlayers) % numPlayers;
        }
    }

    // Gets the next player who hasn't finished yet
    public int getNextActivePlayer(int currentPlayerIndex) {
        int nextPlayerIndex = getNextPlayer(currentPlayerIndex);

        // Loop to find the next player who is not in finishingOrder
        while (finishingOrder.contains(nextPlayerIndex)) {
            nextPlayerIndex = getNextPlayer(nextPlayerIndex);
        }
        
        return nextPlayerIndex;
    }

    private int selectCardToPlay(CollectionOfUnoCards playerHand, UnoCard topCard) {
        Scanner scanner = new Scanner(System.in);
        int chosenCardIndex = -1;
    
        while (true) {
            System.out.print("Choose a card to play by entering its index: ");
            
            // Check for valid integer input
            if (scanner.hasNextInt()) {
                chosenCardIndex = scanner.nextInt();
    
                // Check if the chosen index is valid
                if (chosenCardIndex >= 1 && chosenCardIndex <= playerHand.getNumCards()) {
                    int actualCardIndex = chosenCardIndex - 1; // Convert to 0-based index
                    UnoCard chosenCard = playerHand.getCard(actualCardIndex);
                    
                    // Check if the chosen card is a Wild Draw Four
                    if (chosenCard.getNumber() == 14) { // Wild Draw Four
                        // Check if there are other playable cards
                        if (playerHand.canPlayExcludingWildDrawFour(topCard)) {
                            // If there are other playable cards, inform the player and prompt again
                            System.out.println("You have other playable cards. You cannot play a Wild Draw Four now.");
                            continue; // Go back to choosing a card again
                        }
                    }
    
                    // Check if the chosen card is playable
                    if (chosenCard.canPlay(topCard)) {
                        return actualCardIndex; // Return the 0-based index to be used for removing the card
                    } else {
                        System.out.println("Card not playable. Choose a different card.");
                    }
                } else {
                    System.out.println("Invalid index. Choose again.");
                }
            } else {
                // Handle the case where input is not an integer
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    public void executeDraw(int playerIndex, int numCards) {
        // Check if the deck has enough cards
        if (deck.getNumCards() < numCards) {
            System.out.println("Current number of cards in the deck: " + deck.getNumCards());
            System.out.println("Not enough cards in the deck.");
            shuffleDiscardPileIntoDeck();
        }
        
        // Now draw the cards
        CollectionOfUnoCards playerHand = hands.get(playerIndex);
        for (int i = 0; i < numCards; i++) {
            if (deck.getNumCards() > 0) {
                playerHand.addCard(deck.removeFromTop());
            }
        }
    }
    

    public void playTurn(int player, String gameMode) {
        if (finishingOrder.contains(player)) {
            return; // Skip the turn if player has already finished
        }

        CollectionOfUnoCards playerHand = hands.get(player);
        UnoCard topCard = discardPile.getTopCard();
        
        System.out.println("Player " + (player + 1) + ", it is your turn.");
        System.out.println("The card at the top of the discard pile is: " + topCard);
        
        // Check if the player has a playable card
        if (!playerHand.canPlay(topCard)) {
            System.out.println("No playable cards. Drawing a card...");
            playerHand.addCard(deck.removeFromTop());
            if (!playerHand.canPlay(topCard)) {
                System.out.println("Still no playable cards. Turn is skipped.");
                // Do not move to the next player here
                return; // Exit this turn without moving to next player
            }
        }
        
        // Display hand to player
        System.out.println("Your hand:");
        for (int i = 0; i < playerHand.getNumCards(); i++) {
            System.out.println((i + 1) + ": " + playerHand.getCard(i));
        }
        
        // Choose a card to play
        int chosenCardIndex = selectCardToPlay(playerHand, topCard);
        UnoCard chosenCard = playerHand.getCard(chosenCardIndex);
        playerHand.remove(chosenCardIndex);
        discardPile.addCard(chosenCard);
        
        System.out.println("Player " + (player + 1) + " played: " + chosenCard);

        // Check if the player has one card left after playing
        if (playerHand.getNumCards() == 1) {
            System.out.println("Player " + (player + 1) + " declares UNO!");
        }
        
        // Check for action cards and execute their effects
        // 42 Gamemode affects this
        cardEffectHandler.handleCardEffect(chosenCard, player, gameMode, this);
    }
     
    private void shuffleDiscardPileIntoDeck() {
        System.out.println("Shuffling the discard pile back into the deck.");
    
        if (discardPile.getNumCards() == 0) {
            System.out.println("Discard pile is empty. No cards to shuffle.");
            return; // Exit if there's nothing to shuffle
        }
    
        UnoCard topCard = discardPile.getTopCard(); // Get the top card to keep it
        CollectionOfUnoCards newDeck = new CollectionOfUnoCards(); // New deck for shuffled cards
    
        // Move cards from discard pile except the top one
        while (discardPile.getNumCards() > 1) { // Continue until only the top card remains
            newDeck.addCard(discardPile.remove(0)); // Remove from index 0 to get cards correctly
        }
    
        // Shuffle the new deck
        newDeck.shuffle();
    
        // Add shuffled cards back to the main deck
        while (newDeck.getNumCards() > 0) {
            deck.addCard(newDeck.removeFromTop()); // Correctly remove from newDeck and add to the main deck
        }
    
        // Reset the discard pile with the top card
        discardPile = new CollectionOfUnoCards();
        discardPile.addCard(topCard); // Keep the top card in the discard pile
    
        System.out.println("The discard pile has been shuffled back into the deck.");
    }
}
