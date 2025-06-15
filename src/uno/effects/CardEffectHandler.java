package uno.effects;

import uno.game.Uno;
import uno.cards.UnoCard;

public class CardEffectHandler {
    
    public void handleCardEffect(UnoCard chosenCard, int playerIndex, String gameMode, Uno game) {
        int cardNumber = chosenCard.getNumber();
        
        if (cardNumber == 10) { // Skip
            handleSkip(game);
        } else if (cardNumber == 11) { // Reverse
            handleReverse(game);
        } else if (cardNumber == 12) { // Draw Two
            handleDrawTwo(chosenCard, playerIndex, game);
        } else if (cardNumber == 13) { // Wild
            handleWild(chosenCard, playerIndex, game);
        } else if (cardNumber == 14) { // Wild Draw Four
            handleWildDrawFour(chosenCard, playerIndex, game);
        } else if (cardNumber == 15) { // Special Card
            handleSpecialCard(chosenCard, playerIndex, gameMode, game);
        }
    }
    
    private void handleSkip(Uno game) {
        System.out.println("Next player is skipped!");
        game.checkForWinner(game.getCurrentPlayerIndex());
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleReverse(Uno game) {
        System.out.println("Turn order reversed!");
        game.setClockwise(!game.isClockwise()); // Reverse the direction of play
    }
    
    private void handleDrawTwo(UnoCard chosenCard, int playerIndex, Uno game) {
        System.out.println("Next player draws two uno.cards!");
        game.checkForWinner(game.getCurrentPlayerIndex());
        int nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
        game.executeDraw(nextPlayer, 2);
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleWild(UnoCard chosenCard, int playerIndex, Uno game) {
        System.out.println("Wild card played! Choosing a new color...");
        chosenCard.setColor(game.promptColorSelection(playerIndex));
    }
    
    private void handleWildDrawFour(UnoCard chosenCard, int playerIndex, Uno game) {
        System.out.println("Wild Draw Four card played! Choosing a new color...");
        chosenCard.setColor(game.promptColorSelection(playerIndex));
        System.out.println("Next player draws four uno.cards!");
        game.checkForWinner(game.getCurrentPlayerIndex());
        int nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
        game.executeDraw(nextPlayer, 4);
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleSpecialCard(UnoCard chosenCard, int playerIndex, String gameMode, Uno game) {
        if ("42".equals(gameMode)) {
            System.out.println("SegFault card played! Choosing a new color...");
            chosenCard.setColor(game.promptColorSelection(playerIndex));
            int nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
            System.out.println("Player " + (nextPlayer + 1) + " is skipped!");
            game.checkForWinner(nextPlayer);
            game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
            int followingPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
            System.out.println("Player " + (followingPlayer + 1) + " draws 2 uno.cards due to the SegFault card!");
            game.executeDraw(followingPlayer, 2);
        } else {
            System.out.println("Wild card played! Choosing a new color...");
            chosenCard.setColor(game.promptColorSelection(playerIndex));
        }
    }
    

    public void handleStartingCardEffect(UnoCard startingCard, int playerIndex, String gameMode, Uno game) {
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
                handleStartingCardEffect(startingCard, playerIndex, gameMode, game); // Recursively handle the new starting card
                break;

            case 15: // SegFault Card
                if ("42".equals(gameMode)) {
                    // SegFault logic for game mode "42"
                    System.out.println("Starting card is a SegFault. Player " + (playerIndex + 1) + " can choose the starting color, but will not be allowed to play.");
                    int chosenColor2 = game.promptColorSelection(playerIndex); // Prompt player for a color
                    startingCard.setColor(chosenColor2); // Set chosen color
                    System.out.println("Player " + (playerIndex + 1) + " is skipped!");
                    game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
                    int nextPlayer = game.getNextActivePlayer(game.getCurrentPlayerIndex());
                    System.out.println("Player " + (nextPlayer + 1) + " draws 2 uno.cards due to the SegFault card!");
                    game.executeDraw(nextPlayer, 2);
                } else if ("Minecraft".equals(gameMode)) {
                    System.out.println("Starting card is a Creeper card, we treat this as Wild.");
                    System.out.println("Player " + (playerIndex + 1) + " can choose the starting color.");
                    chosenColor = game.promptColorSelection(playerIndex); // Prompt player for a color
                    startingCard.setColor(chosenColor); // Set chosen color
                } else {
                    // Logic for other game modes if needed
                    System.out.println("Starting card is a Special card, we treat this as Wild.");
                    System.out.println("Player " + (playerIndex + 1) + " can choose the starting color.");
                    chosenColor = game.promptColorSelection(playerIndex); // Prompt player for a color
                    startingCard.setColor(chosenColor); // Set chosen color
                }
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
                System.out.println("Starting card is a Draw Two. Player " + (playerIndex + 1) + " must draw two uno.cards.");
                game.executeDraw(playerIndex, 2);
                game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex())); // Move to the next player
                break;

            default:
                break; // No action needed for regular uno.cards
        }
    }

    // Additional methods can be added here for different game modes if needed
}
