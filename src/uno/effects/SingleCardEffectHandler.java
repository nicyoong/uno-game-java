package uno.effects;

import uno.game.SinglePlayerUno;
import uno.cards.UnoCard;

public class SingleCardEffectHandler {
    
    public void handleCardEffect(UnoCard playedCard, int playerIndex, String gameMode, SinglePlayerUno game) {
        int cardNumber = playedCard.getNumber();
    
        if (cardNumber == 10) {
            handleSkipCard(playerIndex, game);
        } else if (cardNumber == 11) {
            handleReverseCard(playerIndex, game);
        } else if (cardNumber == 12) {
            handleDrawTwoCard(playerIndex, game);
        } else if (cardNumber == 13) {
            handleWildCard(playedCard, playerIndex, game);
        } else if (cardNumber == 14) {
            handleWildDrawFourCard(playedCard, playerIndex, game);
        } else if (cardNumber == 15) {
            handleSegFaultCard(playedCard, playerIndex, gameMode, game);
        }
    }
    
    private void handleSkipCard(int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Skip. Next player's turn is skipped.");
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleReverseCard(int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Reverse. Turn order is reversed.");
        game.setClockwise(!game.isClockwise());
    }
    
    private void handleDrawTwoCard(int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Draw Two. Next player must draw 2 uno.cards.");
        game.executeDraw(game.getNextActivePlayer(game.getCurrentPlayerIndex()), 2);
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleWildCard(UnoCard playedCard, int playerIndex, SinglePlayerUno game) {
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
    }
    
    private void handleWildDrawFourCard(UnoCard playedCard, int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Wild Draw Four. Next player must draw 4 uno.cards.");
        handleWildCard(playedCard, playerIndex, game);
        game.executeDraw(game.getNextActivePlayer(game.getCurrentPlayerIndex()), 4);
        game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
    }
    
    private void handleSegFaultCard(UnoCard playedCard, int playerIndex, String gameMode, SinglePlayerUno game) {
        if ("42".equals(gameMode)) {
            System.out.println("SegFault card played! Choosing a new color...");
            handleWildCard(playedCard, playerIndex, game);
            System.out.println("Player " + (game.getNextActivePlayer(game.getCurrentPlayerIndex()) + 1) + "'s turn is skipped.");
            game.setCurrentPlayerIndex(game.getNextActivePlayer(game.getCurrentPlayerIndex()));
            System.out.println("Player " + (game.getNextActivePlayer(game.getCurrentPlayerIndex()) + 1) + " must draw 2 uno.cards.");
            game.executeDraw(game.getNextActivePlayer(game.getCurrentPlayerIndex()), 2);
        } else {
            handleWildCard(playedCard, playerIndex, game);
        }
    }

    public void handleStartingCardEffect(UnoCard startingCard, String gameMode, SinglePlayerUno game) {
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
            handleStartingCardEffect(startingCard, gameMode, game); // Recursively call to handle the new card
            return; // Exit after handling the new starting card

        } else if (startingCard.getNumber() == 15) { 

            if ("42".equals(gameMode))
            {
                System.out.println("Starting card is a SegFault. Player 1 can choose the starting color, but must skip.");
                if (game.humanPlayerIndex == 0) {
                    int chosenColor = game.promptColorSelection(0); // Prompt human player for a color
                    startingCard.setColor(chosenColor); // Set chosen color
                } else {
                    int chosenColor = game.chooseRandomColor(); // AI chooses color
                    startingCard.setColor(chosenColor);
                    System.out.println("AI Player 1 chose color: " + game.getColorName(chosenColor));
                }
                game.setCurrentPlayerIndex(game.getNextPlayer(game.getCurrentPlayerIndex())); // Skip Player 1
                System.out.println("Player 2 draws 2 uno.cards.");
                game.executeDraw(game.currentPlayerIndex, 2);
            } else if ("Minecraft".equals(gameMode))
            {
                System.out.println("Starting card is a Creeper. Player 1 can choose the starting color.");
            
                // Check if Player 1 is human or AI
                if (game.humanPlayerIndex == 0) {
                    int chosenColor = game.promptColorSelection(0); // Prompt human player for a color
                    startingCard.setColor(chosenColor); // Set chosen color
                } else {
                    int chosenColor = game.chooseRandomColor(); // AI chooses color
                    startingCard.setColor(chosenColor);
                    System.out.println("AI Player 1 chose color: " + game.getColorName(chosenColor));
                }
            } else {
                System.out.println("Starting card is a Special. Player 1 can choose the starting color.");
            
                // Check if Player 1 is human or AI
                if (game.humanPlayerIndex == 0) {
                    int chosenColor = game.promptColorSelection(0); // Prompt human player for a color
                    startingCard.setColor(chosenColor); // Set chosen color
                } else {
                    int chosenColor = game.chooseRandomColor(); // AI chooses color
                    startingCard.setColor(chosenColor);
                    System.out.println("AI Player 1 chose color: " + game.getColorName(chosenColor));
                }
            }

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
                System.out.println("Starting card is a Draw Two. Player 1 must draw two uno.cards.");
                game.executeDraw(game.currentPlayerIndex, 2);
                game.setCurrentPlayerIndex(game.getNextPlayer(game.getCurrentPlayerIndex())); // Move to the next player
                break;
            default:
                break; // No action needed for regular uno.cards
        }
    }
    

    // Additional methods can be added here for different game modes if needed
}
