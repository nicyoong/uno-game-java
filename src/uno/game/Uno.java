package uno.game;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;
import uno.effects.CardEffectHandler;
import uno.multiplayergame.HumanPlayerController;
import uno.multiplayergame.*;

public class Uno {
    private static final int NUMCARDSHAND = 7;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 10;

    private GameStateManager gameState;
    private CardDeckManager deckManager;
    private PlayerManager playerManager;
    private HumanPlayerController humanController;
    private OutputRenderer outputRenderer;
    private CardEffectHandler effectHandler;

    public Uno(int numPlayers, String gameMode) {
        validatePlayerCount(numPlayers);

        gameState = new GameStateManager(numPlayers);
        deckManager = new CardDeckManager(gameMode);
        playerManager = new PlayerManager(numPlayers, gameState);
        humanController = new HumanPlayerController();
        outputRenderer = new OutputRenderer();
        effectHandler = new CardEffectHandler();

        setupGame(gameMode);
    }

    private void validatePlayerCount(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Players must be between " +
                    MIN_PLAYERS + " and " + MAX_PLAYERS);
        }
    }

    private void setupGame(String gameMode) {
        // Deal initial cards
        playerManager.dealInitialCards(deckManager, NUMCARDSHAND);

        // Setup discard pile
        UnoCard startingCard = deckManager.drawCard();
        deckManager.addToDiscardPile(startingCard);
        effectHandler.handleStartingCardEffect(startingCard, 0, gameMode, this);
    }

    public void playGame(String gameMode) {
        outputRenderer.showGameStart(gameState.getNumPlayers());

        // Show initial hands
        for (int i = 0; i < gameState.getNumPlayers(); i++) {
            outputRenderer.showHand(playerManager.getPlayerHand(i), i);
        }
        outputRenderer.showTopCard(deckManager.getTopDiscardCard());

        while (gameState.getFinishingOrder().size() < gameState.getNumPlayers() - 1) {
            int currentPlayer = gameState.getCurrentPlayerIndex();

            if (!gameState.getFinishingOrder().contains(currentPlayer)) {
                playTurn(currentPlayer, gameMode);
            }

            if (deckManager.getDeck().getNumCards() == 0) {
                deckManager.shuffleDiscardPileIntoDeck();
            }
        }

        outputRenderer.showResults(gameState, playerManager);
    }

    private void playTurn(int player, String gameMode) {
        CollectionOfUnoCards hand = playerManager.getPlayerHand(player);
        UnoCard topCard = deckManager.getTopDiscardCard();

        outputRenderer.showPlayerTurn(player);
        outputRenderer.showTopCard(topCard);

        if (!hand.canPlay(topCard)) {
            outputRenderer.showMessage("No playable cards. Drawing a card...");
            playerManager.executeDraw(player, 1, deckManager);
            if (!hand.canPlay(topCard)) {
                outputRenderer.showMessage("Still no playable cards. Turn skipped.");
                gameState.setCurrentPlayerIndex(
                        gameState.getNextActivePlayer(player)
                );
                return;
            }
        }

        outputRenderer.showHand(hand, player);
        int cardIndex = humanController.selectCardToPlay(hand, topCard);
        UnoCard playedCard = hand.getCard(cardIndex);

        playCard(player, cardIndex, playedCard, gameMode);
    }

    private void playCard(int player, int cardIndex, UnoCard card, String gameMode) {
        playerManager.getPlayerHand(player).remove(cardIndex);
        deckManager.addToDiscardPile(card);
        outputRenderer.showMessage("Player " + (player + 1) + " played: " + card);

        if (playerManager.getPlayerHand(player).getNumCards() == 1) {
            outputRenderer.showMessage("Player " + (player + 1) + " declares UNO!");
        }

        playerManager.checkForWinner(player);
        effectHandler.handleCardEffect(card, player, gameMode, this);
    }

    // Getters for effect handler
    public GameStateManager getGameState() { return gameState; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public CardDeckManager getDeckManager() { return deckManager; }
    public HumanPlayerController getHumanController() { return humanController; }

    public void executeDraw(int playerIndex, int numCards) {
        playerManager.executeDraw(playerIndex, numCards, deckManager);
    }
}