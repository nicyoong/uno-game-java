package uno.ai;

import uno.model.RecentAction;
import uno.effects.SingleCardEffectHandler;
import uno.game.GameFlowController;
import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class AIPlayer {

    private GameFlowController game;
    private SingleCardEffectHandler singleCardEffectHandler;

    public AIPlayer(GameFlowController game) {
        this.game = game;
        this.singleCardEffectHandler = new SingleCardEffectHandler();
    }

    public void easyAITurn(int player, CollectionOfUnoCards playerHand,
                           UnoCard topCard, CollectionOfUnoCards discardPile,
                           String gameMode, LinkedList<RecentAction> turnMemory, int memorySpan) {
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

            // Add this action to the turn memory
            turnMemory.add(new RecentAction(player, "Played", aiPlayedCard));
            if (turnMemory.size() > memorySpan) {
                turnMemory.removeFirst(); // Remove the oldest action if memory exceeds the limit
            }

            // Check if the player has one card left after playing
            if (playerHand.getNumCards() == 1) {
                System.out.println("AI Player " + (player + 1) + " declares UNO!");
                turnMemory.add(new RecentAction(player, "Declared UNO", null));
                if (turnMemory.size() > memorySpan) {
                    turnMemory.removeFirst();
                }
            }

            singleCardEffectHandler.handleCardEffect(aiPlayedCard, player, gameMode, game);
            played = true;
        } else {
            handleDrawIfCannotPlay(playerHand, topCard, player, turnMemory,
                    memorySpan, gameMode, game, singleCardEffectHandler, discardPile);
        }
    }

    public void realisticAITurn(int player, CollectionOfUnoCards playerHand,
                                UnoCard topCard, CollectionOfUnoCards discardPile,
                                String gameMode, LinkedList<RecentAction> turnMemory,
                                int memorySpan) {
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

            // Add this action to the turn memory
            turnMemory.add(new RecentAction(player, "Played", aiPlayedCard));
            if (turnMemory.size() > memorySpan) {
                turnMemory.removeFirst(); // Remove the oldest action if memory exceeds the limit
            }

            // Check if the player has one card left after playing
            if (playerHand.getNumCards() == 1) {
                System.out.println("AI Player " + (player + 1) + " declares UNO!");
                turnMemory.add(new RecentAction(player, "Declared UNO", null));
                if (turnMemory.size() > memorySpan) {
                    turnMemory.removeFirst();
                }
            }

            singleCardEffectHandler.handleCardEffect(aiPlayedCard, player, gameMode, game);
            played = true;
        } else {
            handleDrawIfCannotPlay(playerHand, topCard, player, turnMemory,
                    memorySpan, gameMode, game, singleCardEffectHandler, discardPile);
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

    private void handleDrawIfCannotPlay(CollectionOfUnoCards playerHand, UnoCard topCard, int player,
                                        LinkedList<RecentAction> turnMemory, int memorySpan,
                                        String gameMode, GameFlowController game,
                                        SingleCardEffectHandler singleCardEffectHandler,
                                        CollectionOfUnoCards discardPile) {
        System.out.println("AI Player " + (player + 1) + " cannot play any card. Drawing a card...");
        game.drawCardsWithCreeperCheck(player, 1);

        // Add this action to the turn memory
        turnMemory.add(new RecentAction(player, "Drew", null));
        if (turnMemory.size() > memorySpan) {
            turnMemory.removeFirst();
        }

        // Use PlayerManager's canPlayAnyCard through GameFlowController
        if (game.getPlayerManager().canPlayAnyCard(player, topCard)) {
            for (int i = 0; i < playerHand.getNumCards(); i++) {
                UnoCard aiPlayedCard = playerHand.getCard(i);
                if (canPlay(aiPlayedCard, topCard)) {
                    playerHand.remove(i);
                    discardPile.addCard(aiPlayedCard);
                    System.out.println("AI Player " + (player + 1) + " played: " + aiPlayedCard);
                    // Add this action to the turn memory
                    turnMemory.add(new RecentAction(player, "Played", aiPlayedCard));
                    if (turnMemory.size() > memorySpan) {
                        turnMemory.removeFirst();
                    }
                    singleCardEffectHandler.handleCardEffect(aiPlayedCard, player, gameMode, game);
                    break;
                }
            }
        } else {
            System.out.println("AI Player " + (player + 1) + " still cannot play. Turn skipped.");
            turnMemory.add(new RecentAction(player, "Skipped", null));
            if (turnMemory.size() > memorySpan) {
                turnMemory.removeFirst();
            }
        }
    }
}