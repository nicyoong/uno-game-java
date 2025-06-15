package uno.effects;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;
import uno.game.Uno;
import uno.multiplayergame.GameStateManager;
import uno.multiplayergame.OutputRenderer;

public class CardEffectHandler {
    private final OutputRenderer outputRenderer = new OutputRenderer();

    public void handleCardEffect(UnoCard chosenCard, int playerIndex,
                                 String gameMode, Uno game) {
        int cardNumber = chosenCard.getNumber();
        GameStateManager state = game.getGameState();

        if (cardNumber == 10) { // Skip
            handleSkip(game);
        } else if (cardNumber == 11) { // Reverse
            handleReverse(game);
            state.setCurrentPlayerIndex(state.getNextActivePlayer(state.getCurrentPlayerIndex()));
        } else if (cardNumber == 12) { // Draw Two
            handleDrawTwo(game);
        } else if (cardNumber == 13) { // Wild
            handleWild(chosenCard, playerIndex, game);
            state.setCurrentPlayerIndex(state.getNextActivePlayer(state.getCurrentPlayerIndex()));
        } else if (cardNumber == 14) { // Wild Draw Four
            handleWildDrawFour(chosenCard, playerIndex, game);
        } else if (cardNumber == 15) { // Special Card
            handleSpecialCard(chosenCard, playerIndex, gameMode, game);
        } else {
            state.setCurrentPlayerIndex(state.getNextActivePlayer(state.getCurrentPlayerIndex()));
        }
    }

    private void handleSkip(Uno game) {
        outputRenderer.showMessage("Next player is skipped!");
        GameStateManager state = game.getGameState();
        int nextPlayer = state.getNextActivePlayer(state.getCurrentPlayerIndex());
        state.setCurrentPlayerIndex(state.getNextActivePlayer(nextPlayer));
    }

    private void handleReverse(Uno game) {
        outputRenderer.showMessage("Turn order reversed!");
        GameStateManager state = game.getGameState();
        state.setClockwise(!state.isClockwise());
    }

    private void handleDrawTwo(Uno game) {
        outputRenderer.showMessage("Next player draws two cards and is skipped!");
        GameStateManager state = game.getGameState();
        int nextPlayer = state.getNextActivePlayer(state.getCurrentPlayerIndex());
        game.executeDraw(nextPlayer, 2);
        state.setCurrentPlayerIndex(state.getNextActivePlayer(nextPlayer));
    }

    private void handleWild(UnoCard chosenCard, int playerIndex, Uno game) {
        outputRenderer.showMessage("Wild card played! Choosing a new color...");
        int color = game.getHumanController().promptColorSelection(playerIndex);
        chosenCard.setColor(color);
        outputRenderer.showMessage("Color set to: " + UnoCard.getColorName(color));
    }

    private void handleWildDrawFour(UnoCard chosenCard, int playerIndex, Uno game) {
        GameStateManager state = game.getGameState();
        int previousColor = game.getDeckManager().getTopDiscardCard().getColor();
        outputRenderer.showMessage("Wild Draw Four card played! Choosing a new color...");
        handleWild(chosenCard, playerIndex, game);
        int nextPlayer = state.getNextActivePlayer(state.getCurrentPlayerIndex());
        outputRenderer.showMessage("Player " + (nextPlayer + 1) + ", you can challenge!");
        handleWildDrawFourChallenge(playerIndex, nextPlayer, previousColor, game);
    }

    private void handleWildDrawFourChallenge(int playerIndex, int nextPlayer,
                                             int previousColor, Uno game) {
        GameStateManager state = game.getGameState();

        outputRenderer.showMessage("Player " + (nextPlayer + 1) + ", you can challenge!");
        boolean challenge = game.getHumanController().promptChallenge(nextPlayer);

        if (challenge) {
            CollectionOfUnoCards challengerHand = game.getPlayerManager().getPlayerHand(playerIndex);
            boolean hasValidCard = false;

            // Check if player had playable non-Wild cards
            for (int i = 0; i < challengerHand.getNumCards(); i++) {
                UnoCard card = challengerHand.getCard(i);
                if (card.getColor() == previousColor && card.getNumber() < 13) {
                    hasValidCard = true;
                    break;
                }
            }

            if (hasValidCard) {
                // Challenge successful
                outputRenderer.showMessage("Challenge successful! Player " + (playerIndex + 1) +
                        " had a playable card and must draw 6 cards!");
                game.executeDraw(playerIndex, 6);
                // Challenger plays next
                state.setCurrentPlayerIndex(nextPlayer);
            } else {
                // Challenge failed
                outputRenderer.showMessage("Challenge failed! Player " + (nextPlayer + 1) +
                        " must draw 6 cards and is skipped!");
                game.executeDraw(nextPlayer, 6);
                // Skip challenger
                state.setCurrentPlayerIndex(state.getNextActivePlayer(nextPlayer));
            }
        } else {
            // No challenge
            outputRenderer.showMessage("No challenge. Player " + (nextPlayer + 1) +
                    " draws four cards and is skipped!");
            game.executeDraw(nextPlayer, 4);
            // Skip next player
            state.setCurrentPlayerIndex(state.getNextActivePlayer(nextPlayer));
        }
    }

    private void handleSpecialCard(UnoCard chosenCard, int playerIndex,
                                   String gameMode, Uno game) {
        GameStateManager state = game.getGameState();

        if ("42".equals(gameMode)) {
            outputRenderer.showMessage("SegFault card played! Choosing a new color...");
            handleWild(chosenCard, playerIndex, game);

            int nextPlayer = state.getNextActivePlayer(state.getCurrentPlayerIndex());
            outputRenderer.showMessage("Player " + (nextPlayer + 1) + " is skipped!");
            state.setCurrentPlayerIndex(state.getNextActivePlayer(state.getCurrentPlayerIndex()));

            int followingPlayer = state.getNextActivePlayer(state.getCurrentPlayerIndex());
            outputRenderer.showMessage("Player " + (followingPlayer + 1) +
                    " draws 2 cards due to the SegFault card!");
            game.executeDraw(followingPlayer, 2);
        } else {
            handleWild(chosenCard, playerIndex, game);
        }
    }

    public void handleStartingCardEffect(UnoCard startingCard, int playerIndex,
                                         String gameMode, Uno game) {
        GameStateManager state = game.getGameState();

        switch (startingCard.getNumber()) {
            case 13: // Wild
                outputRenderer.showMessage("Starting card is a Wild. Player " +
                        (playerIndex + 1) + " can choose the starting color.");
                handleWild(startingCard, playerIndex, game);
                break;

            case 14: // Wild Draw Four
                outputRenderer.showMessage("Starting card is a Wild Draw Four. " +
                        "Returning it to the deck and drawing a new starting card.");
                game.getDeckManager().returnCardToDeckAndShuffle(startingCard);
                UnoCard newCard = game.getDeckManager().drawCard();
                outputRenderer.showMessage("New starting card: " + newCard);
                handleStartingCardEffect(newCard, playerIndex, gameMode, game);
                break;

            case 15: // SegFault Card
                if ("42".equals(gameMode)) {
                    outputRenderer.showMessage("Starting card is a SegFault. Player " +
                            (playerIndex + 1) + " can choose the starting color, " +
                            "but will not be allowed to play.");
                    handleWild(startingCard, playerIndex, game);
                    outputRenderer.showMessage("Player " + (playerIndex + 1) + " is skipped!");
                    state.setCurrentPlayerIndex(state.getNextPlayer(playerIndex));

                    int nextPlayer = state.getNextPlayer(playerIndex);
                    outputRenderer.showMessage("Player " + (nextPlayer + 1) +
                            " draws 2 cards due to the SegFault card!");
                    game.executeDraw(nextPlayer, 2);
                } else if ("Minecraft".equals(gameMode)) {
                    outputRenderer.showMessage("Starting card is a Creeper card, " +
                            "we treat this as Wild.");
                    outputRenderer.showMessage("Player " + (playerIndex + 1) +
                            " can choose the starting color.");
                    handleWild(startingCard, playerIndex, game);
                } else {
                    outputRenderer.showMessage("Starting card is a Special card, " +
                            "we treat this as Wild.");
                    outputRenderer.showMessage("Player " + (playerIndex + 1) +
                            " can choose the starting color.");
                    handleWild(startingCard, playerIndex, game);
                }
                break;

            case 10: // Skip
                outputRenderer.showMessage("Starting card is a Skip. Player " +
                        (playerIndex + 1) + "'s turn is skipped.");
                state.setCurrentPlayerIndex(state.getNextPlayer(playerIndex));
                break;

            case 11: // Reverse
                outputRenderer.showMessage("Starting card is a Reverse. Turn order is reversed.");
                state.setClockwise(!state.isClockwise());
                break;

            case 12: // Draw Two
                outputRenderer.showMessage("Starting card is a Draw Two. Player " +
                        (playerIndex + 1) + " must draw two cards.");
                game.executeDraw(playerIndex, 2);
                state.setCurrentPlayerIndex(state.getNextPlayer(playerIndex));
                break;

            default:
                break; // No action needed for regular cards
        }
    }
}