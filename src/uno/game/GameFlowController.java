package uno.game;

import uno.ai.AIPlayer;
import uno.cards.CollectionOfUnoCards;
import uno.effects.SingleCardEffectHandler;
import uno.cards.UnoCard;
import uno.model.RecentAction;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class GameFlowController {
    private static final int NUMCARDSHAND = 7;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 10;

    private GameStateManager gameState;
    private CardDeckManager deckManager;
    private PlayerManager playerManager;
    private HumanPlayerController humanController;
    private OutputRenderer outputRenderer;
    private SingleCardEffectHandler effectHandler;

    private String gameMode;
    private int difficulty;
    private String humanPlayerName;

    public GameFlowController(int numPlayers, String gameMode, int difficulty) {
        validatePlayerCount(numPlayers);

        this.gameMode = gameMode;
        this.difficulty = difficulty;

        // Initialize components
        gameState = new GameStateManager(numPlayers);
        deckManager = new CardDeckManager(gameMode);
        playerManager = new PlayerManager(numPlayers);
        humanController = new HumanPlayerController();
        outputRenderer = new OutputRenderer();
        effectHandler = new SingleCardEffectHandler();

        setupGame();
    }

    public HumanPlayerController getHumanController() {
        return humanController;
    }

    private void validatePlayerCount(int numPlayers) {
        if (numPlayers < MIN_PLAYERS || numPlayers > MAX_PLAYERS) {
            throw new IllegalArgumentException("Players must be between " +
                    MIN_PLAYERS + " and " + MAX_PLAYERS);
        }
    }

    private void setupGame() {
        // Random human player assignment
        Random random = new Random();
        gameState.setHumanPlayerIndex(random.nextInt(gameState.getNumPlayers()));

        // Deal initial cards
        playerManager.dealInitialCards(deckManager, NUMCARDSHAND);

        // Setup discard pile
        UnoCard startingCard = deckManager.drawCard();
        deckManager.addToDiscardPile(startingCard);
        effectHandler.handleStartingCardEffect(startingCard, gameMode, this);
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        promptPlayerName(scanner);

        outputRenderer.showGameStart(gameState.getNumPlayers(), humanPlayerName);
        outputRenderer.showHand(playerManager.getPlayerHand(gameState.getHumanPlayerIndex()), humanPlayerName);
        outputRenderer.showTopCard(deckManager.getTopDiscardCard());

        LinkedList<RecentAction> turnMemory = new LinkedList<>();
        final int memorySpan = 12;

        while (gameState.getFinishingOrder().size() < gameState.getNumPlayers() - 1) {
            int currentPlayer = gameState.getCurrentPlayerIndex();

            if (!gameState.getFinishingOrder().contains(currentPlayer)) {
                playTurn(currentPlayer, turnMemory, memorySpan);
            }

            gameState.setCurrentPlayerIndex(gameState.getNextActivePlayer(currentPlayer));

            if (deckManager.getDeck().getNumCards() == 0) {
                deckManager.shuffleDiscardPileIntoDeck();
            }
        }

        outputRenderer.showResults(gameState, playerManager, humanPlayerName);
        scanner.close();
    }

    private void promptPlayerName(Scanner scanner) {
        System.out.print("Enter your name: ");
        humanPlayerName = scanner.nextLine();
        humanPlayerName = humanPlayerName.isEmpty() ? "You" : humanPlayerName;
    }

    private void playTurn(int player, LinkedList<RecentAction> memory, int memorySpan) {
        CollectionOfUnoCards hand = playerManager.getPlayerHand(player);
        UnoCard topCard = deckManager.getTopDiscardCard();

        if (player == gameState.getHumanPlayerIndex()) {
            handleHumanTurn(player, hand, topCard, memory, memorySpan);
        } else {
            handleAITurn(player, hand, topCard, memory, memorySpan);
        }

        checkForWinner(player);
    }

    private void handleHumanTurn(int player, CollectionOfUnoCards hand,
                                 UnoCard topCard, LinkedList<RecentAction> memory,
                                 int memorySpan) {
        outputRenderer.showPlayerTurn(player, humanPlayerName);
        outputRenderer.showTopCard(topCard);

        if (!playerManager.canPlayAnyCard(player, topCard)) {
            handleNoPlayableCards(player, memory, memorySpan);
            return;
        }

        outputRenderer.showHand(hand, humanPlayerName);
        int cardIndex = humanController.selectCardToPlay(hand, topCard);
        UnoCard playedCard = hand.getCard(cardIndex);

        playCard(player, cardIndex, playedCard, memory, memorySpan);
        effectHandler.handleCardEffect(playedCard, player, gameMode, this);
    }

    private void handleAITurn(int player, CollectionOfUnoCards hand,
                              UnoCard topCard, LinkedList<RecentAction> memory,
                              int memorySpan) {
        System.out.println("\nPlayer " + (player + 1) + "'s turn");
        AIPlayer aiPlayer = new AIPlayer(this);

        if (difficulty == 1) {
            aiPlayer.easyAITurn(player, hand, topCard,
                    deckManager.getDiscardPile(), gameMode, memory, memorySpan);
        } else if (difficulty == 2) {
            aiPlayer.realisticAITurn(player, hand, topCard,
                    deckManager.getDiscardPile(), gameMode, memory, memorySpan);
        }
    }

    private void handleNoPlayableCards(int player, LinkedList<RecentAction> memory, int memorySpan) {
        outputRenderer.showMessage("No playable cards. Drawing a card...");
        drawCardsWithCreeperCheck(player, 1);
        memory.add(new RecentAction(player, "Drew", null));
        trimMemory(memory, memorySpan);

        if (!playerManager.canPlayAnyCard(player, deckManager.getTopDiscardCard())) {
            outputRenderer.showMessage("Still no playable cards. Turn skipped.");
            memory.add(new RecentAction(player, "Skipped", null));
            trimMemory(memory, memorySpan);
        }
    }

    private void playCard(int player, int cardIndex, UnoCard card,
                          LinkedList<RecentAction> memory, int memorySpan) {
        playerManager.removeCardFromHand(player, cardIndex);
        deckManager.addToDiscardPile(card);
        outputRenderer.showMessage((player == gameState.getHumanPlayerIndex() ?
                humanPlayerName : "Player " + (player + 1)) +
                " played: " + card);

        memory.add(new RecentAction(player, "Played", card));
        trimMemory(memory, memorySpan);

        if (playerManager.getPlayerHand(player).getNumCards() == 1) {
            outputRenderer.showMessage("UNO!");
            memory.add(new RecentAction(player, "Declared UNO", null));
            trimMemory(memory, memorySpan);
        }
    }

    private void trimMemory(LinkedList<RecentAction> memory, int maxSize) {
        while (memory.size() > maxSize) {
            memory.removeFirst();
        }
    }

    public void drawCardsWithCreeperCheck(int player, int numCards) {
        for (int i = 0; i < numCards; i++) {
            UnoCard drawnCard = deckManager.drawCard();
            if (drawnCard == null) return;

            playerManager.addCardToHand(player, drawnCard);

            if (drawnCard.isCreeper()) {
                outputRenderer.showMessage("Creeper card drawn! Drawing 3 more cards...");
                drawCardsWithCreeperCheck(player, 3);
            }
        }
    }

    private void checkForWinner(int player) {
        if (playerManager.getPlayerHand(player).getNumCards() == 0) {
            outputRenderer.showMessage("Player " + (player + 1) + " has finished!");
            gameState.addToFinishingOrder(player);
        }
    }

    // Getters for dependencies
    public GameStateManager getGameState() { return gameState; }
    public PlayerManager getPlayerManager() { return playerManager; }
    public CardDeckManager getDeckManager() { return deckManager; }
    public OutputRenderer getOutputRenderer() { return outputRenderer; }
    public int chooseRandomColor() { return new Random().nextInt(4); }
}