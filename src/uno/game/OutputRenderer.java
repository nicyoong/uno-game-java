package uno.game;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;

import java.util.List;

public class OutputRenderer {
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showHand(CollectionOfUnoCards hand, String playerName) {
        System.out.println(playerName + "'s hand:");
        for (int i = 0; i < hand.getNumCards(); i++) {
            System.out.println((i + 1) + ": " + hand.getCard(i));
        }
    }

    public void showTopCard(UnoCard topCard) {
        System.out.println("The card at the top of the discard pile is " + topCard);
    }

    public void showPlayerTurn(int playerIndex, String playerName) {
        System.out.println(playerName + ", it is your turn.");
    }

    public void showGameStart(int numPlayers, String playerName) {
        System.out.println("Starting the game with " + numPlayers + " players!");
        System.out.println(playerName + ", you are Player " + (playerName.equals("You") ? "" : (playerName + " ")) + "in this game.");
    }

    public void showResults(GameStateManager gameState, PlayerManager playerManager, String humanPlayerName) {
        System.out.println("\nGame Over!");
        System.out.println("Player rankings:");

        List<Integer> finishingOrder = gameState.getFinishingOrder();
        for (int i = 0; i < finishingOrder.size(); i++) {
            int playerIndex = finishingOrder.get(i);
            String name = (playerIndex == gameState.getHumanPlayerIndex()) ?
                    humanPlayerName : "Player " + (playerIndex + 1);
            System.out.println((i + 1) + ": " + name + (i == 0 ? " (Winner)" : ""));
        }

        // Last player
        for (int i = 0; i < gameState.getNumPlayers(); i++) {
            if (!finishingOrder.contains(i)) {
                String name = (i == gameState.getHumanPlayerIndex()) ?
                        humanPlayerName : "Player " + (i + 1);
                int cards = playerManager.getPlayerHand(i).getNumCards();
                System.out.println((finishingOrder.size() + 1) + ": " + name +
                        " (still has " + cards + " cards)");
                break;
            }
        }
    }
}