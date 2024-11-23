import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SinglePlayerUno {

    final private static int NUMCARDSHAND = 7;
    final private static int MIN_PLAYERS = 2;
    final private static int MAX_PLAYERS = 10;

    public CollectionOfUnoCards deck;
    private CollectionOfUnoCards discardPile;
    private List<CollectionOfUnoCards> hands;
    public int currentPlayerIndex;
    private int numPlayers;
    private List<Integer> finishingOrder;
    public boolean isClockwise = true;
    public int humanPlayerIndex;
    private SingleCardEffectHandler singleCardEffectHandler;

    public SinglePlayerUno(int numPlayers, String gameMode) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Number of players must be between " + MIN_PLAYERS + " and " + MAX_PLAYERS);
        }

        this.numPlayers = numPlayers;
        this.finishingOrder = new ArrayList<>();

        Random random = new Random();
        this.humanPlayerIndex = random.nextInt(numPlayers); // Random index for human player

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

        singleCardEffectHandler = new SingleCardEffectHandler();

        // Handle the starting card effects
        singleCardEffectHandler.handleStartingCardEffect(startingCard, gameMode, this);
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

    public int chooseRandomColor() {
        Random random = new Random();
        return random.nextInt(4); // Randomly select a color (0-3)
    }    

    private void checkForWinner(int playerIndex) {
        if (hands.get(playerIndex).getNumCards() == 0) {
            System.out.println("Player " + (playerIndex + 1) + " has finished!");
            finishingOrder.add(playerIndex);
            if (finishingOrder.size() == 1) {
                System.out.println("Player " + (playerIndex + 1) + " won!");
            }
        }
    }

    public void playGame(String gameMode) {
        Scanner stdin = new Scanner(System.in);
        // Prompt for player name
        System.out.print("Enter your name: ");
        String playerName = stdin.nextLine();
        if (playerName.isEmpty()) {
            playerName = "You"; // Default name if none is provided
        }
        System.out.println("Starting the game with " + numPlayers + " players!");

        // Display initial hands
        System.out.println("Your hand:");
        System.out.println(hands.get(humanPlayerIndex)); // Show only the human player's hand

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

        // Handle last player
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
            if (i == 0) { // Check if it's the first player (the winner)
                if (finishingOrder.get(i) == humanPlayerIndex) {
                    System.out.println((i + 1) + ": " + playerName + " (Winner)");
                } else {
                    System.out.println((i + 1) + ": Player " + (finishingOrder.get(i) + 1) + " (Winner)");
                }
            } else { // For the rest of the players
                if (finishingOrder.get(i) == humanPlayerIndex) {
                    System.out.println((i + 1) + ": " + playerName + " (finished)");
                } else {
                    System.out.println((i + 1) + ": Player " + (finishingOrder.get(i) + 1) + " (finished)");
                }
            }
        }

        // Print the last player
        if (lastPlayerIndex != -1) {
            if (lastPlayerIndex == humanPlayerIndex) {
                System.out.println((finishingOrder.size() + 1) + ": " + playerName + " (still has " + lastPlayerCardCount + " cards)");
            } else {
                System.out.println((finishingOrder.size() + 1) + ": Player " + (lastPlayerIndex + 1) + " (still has " + lastPlayerCardCount + " cards)");
            }
        }

        stdin.close(); // Close Scanner at the end of the game
    }

    public int promptColorSelection(int player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + (player + 1) + ", choose a color:");
        System.out.println("1: Yellow, 2: Red, 3: Green, 4: Blue");
        
        int colorChoice = -1;
        while (colorChoice < 1 || colorChoice > 4) {
            System.out.print("Enter the color number: ");
            colorChoice = scanner.nextInt();
            if (colorChoice < 1 || colorChoice > 4) {
                System.out.println("Invalid choice. Please choose between 1 and 4.");
            }
        }
        
        // Print the chosen color name for better clarity
        String colorName = "";
        switch (colorChoice) {
            case 1: colorName = "Yellow"; break;
            case 2: colorName = "Red"; break;
            case 3: colorName = "Green"; break;
            case 4: colorName = "Blue"; break;
        }
        System.out.println("You have chosen " + colorName + " for the Wild card.");
        
        return colorChoice - 1;
    }
    

    public int getNextPlayer(int currentPlayer) {
        if (isClockwise) {
            return (currentPlayer + 1) % numPlayers;
        } else {
            return (currentPlayer - 1 + numPlayers) % numPlayers;
        }
    }

    public int getNextActivePlayer(int currentPlayerIndex) {
        int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
        while (finishingOrder.contains(nextPlayerIndex)) {
            nextPlayerIndex = getNextPlayer(nextPlayerIndex);
        }
        return nextPlayerIndex;
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
            // Reshuffle if deck is empty before drawing each card
            if (deck.getNumCards() == 0) {
                System.out.println("Deck is empty.");
                shuffleDiscardPileIntoDeck();
            }
    
            // Draw a card if deck is not empty
            if (deck.getNumCards() > 0) {
                UnoCard drawnCard = deck.removeFromTop();
                playerHand.addCard(drawnCard);
    
                // Check if the drawn card is a Creeper card
                if (drawnCard.isCreeper()) {
                    System.out.println("Player " + (playerIndex + 1) + " has drawn a Creeper card and must draw 3 more cards.");
    
                    // Draw 3 additional cards as per Creeper rule
                    for (int j = 0; j < 3; j++) {
                        // Reshuffle if deck is empty before each additional draw
                        if (deck.getNumCards() == 0) {
                            System.out.println("Deck is empty while drawing additional cards for Creeper.");
                            shuffleDiscardPileIntoDeck();
                        }
    
                        if (deck.getNumCards() > 0) {
                            UnoCard additionalCard = deck.removeFromTop();
                            playerHand.addCard(additionalCard);
                        }
                    }
                }
            }
        }
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

    public void playTurn(int player, String gameMode) {
        if (finishingOrder.contains(player)) {
            return; // Skip the turn if player has already finished
        }
      
        CollectionOfUnoCards playerHand = hands.get(player);
        UnoCard topCard = discardPile.getTopCard();
        
        System.out.println("Player " + (player + 1) + ", it is your turn.");
        System.out.println("The card at the top of the discard pile is: " + topCard);
        
        if (player == humanPlayerIndex) { // Human player
            if (!canPlayAnyCard(playerHand, topCard)) {
                System.out.println("No playable cards. Drawing a card...");
                executeDraw(player, 1);
                if (!canPlayAnyCard(playerHand, topCard)) {
                    System.out.println("You still cannot play. Turn skipped.");
                    currentPlayerIndex = getNextActivePlayer(currentPlayerIndex);
                    return;
                }
            }
    
            // Display player's hand
            System.out.println("Your hand:");
            for (int i = 0; i < playerHand.getNumCards(); i++) {
                System.out.println((i + 1) + ": " + playerHand.getCard(i));
            }
    
            // Use the selectCardToPlay helper method
            int cardIndex = selectCardToPlay(playerHand, topCard);
    
            // Play the card
            UnoCard playedCard = playerHand.getCard(cardIndex);
            playerHand.remove(cardIndex); // Remove the played card from the hand
            discardPile.addCard(playedCard);
            System.out.println("You played: " + playedCard);
            // Check if the player has one card left after playing
            if (playerHand.getNumCards() == 1) {
                System.out.println("You declare UNO!");
            }
            singleCardEffectHandler.handleCardEffect(playedCard, player, gameMode, this);
        } else { // AI player
            aiTurn(player, playerHand, topCard, discardPile, gameMode);
        }
    }
    
    public void aiTurn(int player, CollectionOfUnoCards playerHand, 
                        UnoCard topCard, CollectionOfUnoCards discardPile, String gameMode) {
        // Attempt to play a card first
        boolean played = false;
        List<Integer> playableCardIndices = new ArrayList<>();

        // Check for playable cards in the AI's hand
        for (int i = 0; i < playerHand.getNumCards(); i++) {
            UnoCard aiPlayedCard = playerHand.getCard(i);
            if (canPlay(aiPlayedCard, topCard)) {
                playableCardIndices.add(i);
            }
        }

        // If there are playable cards, choose one randomly
        if (!playableCardIndices.isEmpty()) {
            Random random = new Random();
            int randomIndex = playableCardIndices.get(random.nextInt(playableCardIndices.size()));
            UnoCard aiPlayedCard = playerHand.getCard(randomIndex);
            
            playerHand.remove(randomIndex);
            discardPile.addCard(aiPlayedCard);
            System.out.println("AI Player " + (player + 1) + " played: " + aiPlayedCard);
            
            // Check if the player has one card left after playing
            if (playerHand.getNumCards() == 1) {
                System.out.println("AI Player " + (player + 1) + " declares UNO!");
            }
            
            singleCardEffectHandler.handleCardEffect(aiPlayedCard, player, gameMode, this);
            played = true;
        } else {
            System.out.println("AI Player " + (player + 1) + " cannot play any card. Drawing a card...");
            executeDraw(player, 1);
            
            // Check if it can play after drawing
            if (canPlayAnyCard(playerHand, topCard)) {
                for (int i = 0; i < playerHand.getNumCards(); i++) {
                    UnoCard aiPlayedCard = playerHand.getCard(i);
                    if (canPlay(aiPlayedCard, topCard)) {
                        playerHand.remove(i);
                        discardPile.addCard(aiPlayedCard);
                        System.out.println("AI Player " + (player + 1) + " played: " + aiPlayedCard);
                        singleCardEffectHandler.handleCardEffect(aiPlayedCard, player, gameMode, this);
                        break;
                    }
                }
            } else {
                System.out.println("AI Player " + (player + 1) + " still cannot play. Turn skipped.");
            }
        }
    }

    private boolean canPlay(UnoCard cardToPlay, UnoCard topCard) {
        boolean[] conditions = new boolean[]{
            cardToPlay.getColor() == topCard.getColor(),
            cardToPlay.getNumber() == topCard.getNumber(),
            cardToPlay.getNumber() == 13,
            cardToPlay.getNumber() == 14,
            cardToPlay.getNumber() == 15
        };
    
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
    
        return false;
    }

    private boolean canPlayAnyCard(CollectionOfUnoCards playerHand, UnoCard topCard) {
        for (int i = 0; i < playerHand.getNumCards(); i++) {
            if (canPlay(playerHand.getCard(i), topCard)) {
                return true;
            }
        }
        return false;
    }

    public String getColorName(int colorNumber) {
        switch (colorNumber) {
            case 0: return "Yellow";
            case 1: return "Red";
            case 2: return "Green";
            case 3: return "Blue";
            default: return "Unknown";
        }
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
