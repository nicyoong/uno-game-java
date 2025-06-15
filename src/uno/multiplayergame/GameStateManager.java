package uno.multiplayergame;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private int currentPlayerIndex;
    private boolean isClockwise;
    private List<Integer> finishingOrder;
    private int numPlayers;

    public GameStateManager(int numPlayers) {
        this.numPlayers = numPlayers;
        this.finishingOrder = new ArrayList<>();
        this.isClockwise = true;
    }

    // Getters and setters
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public void setCurrentPlayerIndex(int currentPlayerIndex) { this.currentPlayerIndex = currentPlayerIndex; }
    public boolean isClockwise() { return isClockwise; }
    public void setClockwise(boolean clockwise) { isClockwise = clockwise; }
    public List<Integer> getFinishingOrder() { return finishingOrder; }
    public int getNumPlayers() { return numPlayers; }

    public int getNextPlayer(int currentPlayer) {
        if (isClockwise) {
            return (currentPlayer + 1) % numPlayers;
        } else {
            return (currentPlayer - 1 + numPlayers) % numPlayers;
        }
    }

    public int getNextActivePlayer(int currentPlayer) {
        int nextPlayer = getNextPlayer(currentPlayer);
        while (finishingOrder.contains(nextPlayer)) {
            nextPlayer = getNextPlayer(nextPlayer);
        }
        return nextPlayer;
    }
}