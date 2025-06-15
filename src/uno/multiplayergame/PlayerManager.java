package uno.multiplayergame;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private List<CollectionOfUnoCards> hands;
    private GameStateManager gameState;

    public PlayerManager(int numPlayers, GameStateManager gameState) {
        this.gameState = gameState;
        this.hands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            hands.add(new CollectionOfUnoCards());
        }
    }

    public void dealInitialCards(CardDeckManager deckManager, int numCardsPerHand) {
        for (int i = 0; i < numCardsPerHand; i++) {
            for (int j = 0; j < gameState.getNumPlayers(); j++) {
                hands.get(j).addCard(deckManager.drawCard());
            }
        }
    }

    public void checkForWinner(int playerIndex) {
        if (hands.get(playerIndex).getNumCards() == 0) {
            gameState.getFinishingOrder().add(playerIndex);
        }
    }

    public CollectionOfUnoCards getPlayerHand(int playerIndex) {
        return hands.get(playerIndex);
    }

    public void executeDraw(int playerIndex, int numCards, CardDeckManager deckManager) {
        for (int i = 0; i < numCards; i++) {
            if (deckManager.getDeck().getNumCards() == 0) {
                deckManager.shuffleDiscardPileIntoDeck();
            }

            if (deckManager.getDeck().getNumCards() > 0) {
                UnoCard drawnCard = deckManager.drawCard();
                hands.get(playerIndex).addCard(drawnCard);
            }
        }
    }
}