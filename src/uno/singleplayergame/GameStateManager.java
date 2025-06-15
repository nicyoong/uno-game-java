package uno.singleplayergame;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private int currentPlayerIndex;
    private boolean isClockwise;
    private List<Integer> finishingOrder;
    private int numPlayers;
    private int humanPlayerIndex;

    public GameStateManager(int numPlayers) {
        this.numPlayers = numPlayers;
        this.finishingOrder = new ArrayList<>();
        this.isClockwise = true;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public boolean isClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        isClockwise = clockwise;
    }

    public List<Integer> getFinishingOrder() {
        return finishingOrder;
    }

    public void addToFinishingOrder(int playerIndex) {
        finishingOrder.add(playerIndex);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getHumanPlayerIndex() {
        return humanPlayerIndex;
    }

    public void setHumanPlayerIndex(int humanPlayerIndex) {
        this.humanPlayerIndex = humanPlayerIndex;
    }

    public int getNextPlayer(int currentPlayer) {
        if (isClockwise) {
            return (currentPlayer + 1) % numPlayers;
        } else {
            return (currentPlayer - 1 + numPlayers) % numPlayers;
        }
    }

    public int getNextActivePlayer(int currentPlayerIndex) {
        int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
        while (finishingOrder.contains(nextPlayerIndex)) {
            nextPlayerIndex = getNextPlayer(nextPlayerIndex);
        }
        return nextPlayerIndex;
    }

    public boolean isPlayerActive(int playerIndex) {
        return !finishingOrder.contains(playerIndex);
    }
}