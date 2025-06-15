package uno.multiplayergame;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;

import java.util.List;

public class OutputRenderer {
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showHand(CollectionOfUnoCards hand, int playerIndex) {
        System.out.println("Player " + (playerIndex + 1) + ", here is your hand:");
        for (int i = 0; i < hand.getNumCards(); i++) {
            System.out.println((i + 1) + ": " + hand.getCard(i));
        }
    }

    public void showTopCard(UnoCard topCard) {
        System.out.println("The card at the top of the discard pile is " + topCard);
    }

    public void showPlayerTurn(int playerIndex) {
        System.out.println("Player " + (playerIndex + 1) + ", it is your turn.");
    }

    public void showGameStart(int numPlayers) {
        System.out.println("Starting the game with " + numPlayers + " players!");
    }

    public void showResults(GameStateManager gameState, PlayerManager playerManager) {
        System.out.println("\nGame Over!");
        System.out.println("Player rankings:");

        List<Integer> finishingOrder = gameState.getFinishingOrder();
        for (int i = 0; i < finishingOrder.size(); i++) {
            int playerIndex = finishingOrder.get(i);
            System.out.println((i + 1) + ": Player " + (playerIndex + 1) + " (finished)");
        }

        // Last player
        for (int i = 0; i < gameState.getNumPlayers(); i++) {
            if (!finishingOrder.contains(i)) {
                int cards = playerManager.getPlayerHand(i).getNumCards();
                System.out.println((finishingOrder.size() + 1) + ": Player " + (i + 1) +
                        " (still has " + cards + " cards)");
                break;
            }
        }
    }
}