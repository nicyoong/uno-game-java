package uno.effects;

import uno.cards.UnoCard;
import uno.game.SinglePlayerUno;
import uno.singleplayergame.*;

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
        game.getGameState().setCurrentPlayerIndex(
                game.getGameState().getNextActivePlayer(game.getGameState().getCurrentPlayerIndex())
        );
    }

    private void handleReverseCard(int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Reverse. Turn order is reversed.");
        game.getGameState().setClockwise(!game.getGameState().isClockwise());
    }

    private void handleDrawTwoCard(int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Draw Two. Next player must draw 2 cards.");
        int nextPlayer = game.getGameState().getNextActivePlayer(game.getGameState().getCurrentPlayerIndex());
        game.drawCardsWithCreeperCheck(nextPlayer, 2);
        game.getGameState().setCurrentPlayerIndex(
                game.getGameState().getNextActivePlayer(game.getGameState().getCurrentPlayerIndex())
        );
    }

    private void handleWildCard(UnoCard playedCard, int playerIndex, SinglePlayerUno game) {
        OutputRenderer renderer = game.getOutputRenderer();

        if (playerIndex == game.getGameState().getHumanPlayerIndex()) {
            int color = game.getHumanController().promptColorSelection();
            playedCard.setColor(color);
            renderer.showMessage("You chose color: " + UnoCard.getColorName(color));
        } else {
            int color = game.chooseRandomColor();
            playedCard.setColor(color);
            renderer.showMessage("AI Player " + (playerIndex + 1) +
                    " chose color: " + UnoCard.getColorName(color));
        }
    }

    private void handleWildDrawFourCard(UnoCard playedCard, int playerIndex, SinglePlayerUno game) {
        System.out.println("Player " + (playerIndex + 1) + " played Wild Draw Four. Next player must draw 4 cards.");
        handleWildCard(playedCard, playerIndex, game);
        int nextPlayer = game.getGameState().getNextActivePlayer(game.getGameState().getCurrentPlayerIndex());
        game.drawCardsWithCreeperCheck(nextPlayer, 4);
        game.getGameState().setCurrentPlayerIndex(
                game.getGameState().getNextActivePlayer(game.getGameState().getCurrentPlayerIndex())
        );
    }

    private void handleSegFaultCard(UnoCard playedCard, int playerIndex, String gameMode, SinglePlayerUno game) {
        GameStateManager gameState = game.getGameState();
        OutputRenderer renderer = game.getOutputRenderer();

        if ("42".equals(gameMode)) {
            renderer.showMessage("SegFault card played! Choosing a new color...");
            handleWildCard(playedCard, playerIndex, game);

            int nextPlayer = gameState.getNextActivePlayer(gameState.getCurrentPlayerIndex());
            renderer.showMessage("Player " + (nextPlayer + 1) + "'s turn is skipped.");
            gameState.setCurrentPlayerIndex(nextPlayer);

            int drawPlayer = gameState.getNextActivePlayer(gameState.getCurrentPlayerIndex());
            renderer.showMessage("Player " + (drawPlayer + 1) + " must draw 2 cards.");
            game.drawCardsWithCreeperCheck(drawPlayer, 2);
        } else {
            handleWildCard(playedCard, playerIndex, game);
        }
    }

    public void handleStartingCardEffect(UnoCard startingCard, String gameMode, SinglePlayerUno game) {
        GameStateManager gameState = game.getGameState();
        OutputRenderer renderer = game.getOutputRenderer();
        CardDeckManager deckManager = game.getDeckManager();
        HumanPlayerController humanController = game.getHumanController();

        if (startingCard.getNumber() == 13) { // Wild
            renderer.showMessage("Starting card is a Wild. Player 1 can choose the starting color.");

            if (gameState.getHumanPlayerIndex() == 0) {
                int chosenColor = humanController.promptColorSelection();
                startingCard.setColor(chosenColor);
                renderer.showMessage("You chose color: " + UnoCard.getColorName(chosenColor));
            } else {
                int chosenColor = game.chooseRandomColor();
                startingCard.setColor(chosenColor);
                renderer.showMessage("AI Player 1 chose color: " + UnoCard.getColorName(chosenColor));
            }

        } else if (startingCard.getNumber() == 14) { // Wild Draw Four
            renderer.showMessage("Starting card is a Wild Draw Four. Returning it to the deck and drawing a new starting card.");
            deckManager.getDiscardPile().removeFromTop(); // Remove from discard
            deckManager.returnCardToDeckAndShuffle(startingCard); // Return to deck and shuffle
            UnoCard newStartingCard = deckManager.drawCard();
            deckManager.addToDiscardPile(newStartingCard); // Add new card to discard
            renderer.showMessage("New starting card: " + newStartingCard);

            // Handle the new starting card after drawing
            handleStartingCardEffect(newStartingCard, gameMode, game);
            return;

        } else if (startingCard.getNumber() == 15) {
            if ("42".equals(gameMode)) {
                renderer.showMessage("Starting card is a SegFault. Player 1 can choose the starting color, but must skip.");
                if (gameState.getHumanPlayerIndex() == 0) {
                    int chosenColor = humanController.promptColorSelection();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("You chose color: " + UnoCard.getColorName(chosenColor));
                } else {
                    int chosenColor = game.chooseRandomColor();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("AI Player 1 chose color: " + UnoCard.getColorName(chosenColor));
                }

                // Skip Player 1 (index 0)
                gameState.setCurrentPlayerIndex(gameState.getNextPlayer(0));
                renderer.showMessage("Player 2 draws 2 cards.");
                game.drawCardsWithCreeperCheck(1, 2); // Player 2 is index 1

            } else if ("Minecraft".equals(gameMode)) {
                renderer.showMessage("Starting card is a Creeper. Player 1 can choose the starting color.");

                if (gameState.getHumanPlayerIndex() == 0) {
                    int chosenColor = humanController.promptColorSelection();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("You chose color: " + UnoCard.getColorName(chosenColor));
                } else {
                    int chosenColor = game.chooseRandomColor();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("AI Player 1 chose color: " + UnoCard.getColorName(chosenColor));
                }
            } else {
                renderer.showMessage("Starting card is a Special. Player 1 can choose the starting color.");

                if (gameState.getHumanPlayerIndex() == 0) {
                    int chosenColor = humanController.promptColorSelection();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("You chose color: " + UnoCard.getColorName(chosenColor));
                } else {
                    int chosenColor = game.chooseRandomColor();
                    startingCard.setColor(chosenColor);
                    renderer.showMessage("AI Player 1 chose color: " + UnoCard.getColorName(chosenColor));
                }
            }
        }

        // Handle standard starting card effects
        switch (startingCard.getNumber()) {
            case 10: // Skip
                renderer.showMessage("Starting card is a Skip. Player 1's turn is skipped.");
                gameState.setCurrentPlayerIndex(gameState.getNextPlayer(0));
                break;
            case 11: // Reverse
                renderer.showMessage("Starting card is a Reverse. Turn order is reversed.");
                gameState.setClockwise(!gameState.isClockwise());
                break;
            case 12: // Draw Two
                renderer.showMessage("Starting card is a Draw Two. Player 1 must draw two cards.");
                game.drawCardsWithCreeperCheck(0, 2);
                gameState.setCurrentPlayerIndex(gameState.getNextPlayer(0));
                break;
            default:
                break; // No action needed for regular cards
        }
    }
    

    // Additional methods can be added here for different game modes if needed
}
