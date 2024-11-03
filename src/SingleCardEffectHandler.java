public class SingleCardEffectHandler {
    
    public void handleCardEffect(UnoCard playedCard, int playerIndex, SinglePlayerUno game) {
        switch (playedCard.getNumber()) {
            case 10: // Skip
                System.out.println("Player " + (playerIndex + 1) + " played Skip. Next player's turn is skipped.");
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;
            case 11: // Reverse
                System.out.println("Player " + (playerIndex + 1) + " played Reverse. Turn order is reversed.");
                game.setClockwise(!game.isClockwise());
                break;
            case 12: // Draw Two
                System.out.println("Player " + (playerIndex + 1) + " played Draw Two. Next player must draw 2 cards.");
                game.executeDraw(game.getNextActivePlayer(game.getCurrentPlayerIndex()), 2);
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;
            case 13: // Wild
                if (playerIndex == game.humanPlayerIndex) {
                    // Human player chooses color
                    int color = game.promptColorSelection(playerIndex);
                    playedCard.setColor(color);
                } else {
                    // AI chooses color
                    int color = game.chooseRandomColor();
                    playedCard.setColor(color);
                    System.out.println("AI Player " + (playerIndex + 1) + " chose color: " + game.getColorName(color));
                }
                break;
            case 14: // Wild Draw Four
                System.out.println("Player " + (playerIndex + 1) + " played Wild Draw Four. Next player must draw 4 cards.");
                game.executeDraw(game.getNextActivePlayer(game.getCurrentPlayerIndex()), 4);
                if (playerIndex == game.humanPlayerIndex) {
                    // Human player chooses color
                    int color = game.promptColorSelection(playerIndex);
                    playedCard.setColor(color);
                } else {
                    // AI chooses color
                    int color = game.chooseRandomColor();
                    playedCard.setColor(color);
                    System.out.println("AI Player " + (playerIndex + 1) + " chose color: " + game.getColorName(color));
                }
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;
        }
    }

    public void handleStartingCardEffect(UnoCard startingCard, SinglePlayerUno game) {
        if (startingCard.getNumber() == 13) { // Wild
            System.out.println("Starting card is a Wild. Player 1 can choose the starting color.");
            
            // Check if Player 1 is human or AI
            if (game.humanPlayerIndex == 0) {
                int chosenColor = game.promptColorSelection(0); // Prompt human player for a color
                startingCard.setColor(chosenColor); // Set chosen color
            } else {
                int chosenColor = game.chooseRandomColor(); // AI chooses color
                startingCard.setColor(chosenColor);
                System.out.println("AI Player 1 chose color: " + game.getColorName(chosenColor));
            }
            
        } else if (startingCard.getNumber() == 14) { // Wild Draw Four
            System.out.println("Starting card is a Wild Draw Four. Returning it to the deck and drawing a new starting card.");
            game.deck.addCard(startingCard); // Return Wild Draw Four to the deck
            game.deck.shuffle(); // Reshuffle the deck
            startingCard = game.deck.removeFromTop(); // Draw a new starting card
            // Handle the new starting card after drawing
            handleStartingCardEffect(startingCard, game); // Recursively call to handle the new card
            return; // Exit after handling the new starting card
        }
    
        switch (startingCard.getNumber()) {
            case 10: // Skip
                System.out.println("Starting card is a Skip. Player 1's turn is skipped.");
                game.setCurrentPlayerIndex(game.getNextPlayer(game.getCurrentPlayerIndex())); // Skip Player 1
                break;
            case 11: // Reverse
                System.out.println("Starting card is a Reverse. Turn order is reversed.");
                game.setClockwise(!game.isClockwise()); // Reverse the direction
                break;
            case 12: // Draw Two
                System.out.println("Starting card is a Draw Two. Player 1 must draw two cards.");
                game.executeDraw(game.currentPlayerIndex, 2);
                game.setCurrentPlayerIndex(game.getNextPlayer(game.getCurrentPlayerIndex())); // Move to the next player
                break;
            default:
                break; // No action needed for regular cards
        }
    }
    

    // Additional methods can be added here for different game modes if needed
}
