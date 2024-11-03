public class CardEffectHandler {
    
    public void handleCardEffect(UnoCard chosenCard, int playerIndex, Uno game) {
        switch (chosenCard.getNumber()) {
            case 10: // Skip
                System.out.println("Next player is skipped!");
                game.checkForWinner(game.getCurrentPlayerIndex());
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;

            case 11: // Reverse
                System.out.println("Turn order reversed!");
                game.setClockwise(!game.isClockwise()); // Reverse the direction of play
                break;

            case 12: // Draw Two
                System.out.println("Next player draws two cards!");
                game.checkForWinner(game.getCurrentPlayerIndex());
                int nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
                game.executeDraw(nextPlayer, 2);
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;

            case 13: // Wild
                System.out.println("Wild card played! Choosing a new color...");
                chosenCard.setColor(game.promptColorSelection(playerIndex));
                break;

            case 14: // Wild Draw Four
                System.out.println("Wild Draw Four card played! Choosing a new color...");
                chosenCard.setColor(game.promptColorSelection(playerIndex));
                System.out.println("Next player draws four cards!");
                game.checkForWinner(game.getCurrentPlayerIndex());
                nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
                game.executeDraw(nextPlayer, 4);
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                break;

            default:
                // No action for regular cards
                break;
        }
    }

    public void handleStartingCardEffect(UnoCard startingCard, int playerIndex, Uno game) {
        switch (startingCard.getNumber()) {
            case 13: // Wild
                System.out.println("Starting card is a Wild. Player " + (playerIndex + 1) + " can choose the starting color.");
                int chosenColor = game.promptColorSelection(playerIndex); // Prompt player for a color
                startingCard.setColor(chosenColor); // Set chosen color
                break;

            case 14: // Wild Draw Four
                System.out.println("Starting card is a Wild Draw Four. Returning it to the deck and drawing a new starting card.");
                game.deck.addCard(startingCard); // Return Wild Draw Four to the deck
                game.deck.shuffle(); // Reshuffle the deck
                startingCard = game.deck.removeFromTop(); // Draw a new starting card
                handleStartingCardEffect(startingCard, playerIndex, game); // Recursively handle the new starting card
                break;

            case 10: // Skip
                System.out.println("Starting card is a Skip. Player " + (playerIndex + 1) + "'s turn is skipped.");
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex())); // Skip current player
                break;

            case 11: // Reverse
                System.out.println("Starting card is a Reverse. Turn order is reversed.");
                game.setClockwise(!game.isClockwise()); // Reverse the direction
                break;

            case 12: // Draw Two
                System.out.println("Starting card is a Draw Two. Player " + (playerIndex + 1) + " must draw two cards.");
                game.executeDraw(playerIndex, 2);
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex())); // Move to the next player
                break;

            default:
                break; // No action needed for regular cards
        }
    }

    // Additional methods can be added here for different game modes if needed
}
