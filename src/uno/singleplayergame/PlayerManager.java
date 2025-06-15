package uno.singleplayergame;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private List<CollectionOfUnoCards> hands;
    private int numPlayers;

    public PlayerManager(int numPlayers) {
        this.numPlayers = numPlayers;
        this.hands = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            hands.add(new CollectionOfUnoCards());
        }
    }

    public void dealInitialCards(CardDeckManager deckManager, int numCardsPerHand) {
        for (int i = 0; i < numCardsPerHand; i++) {
            for (int j = 0; j < numPlayers; j++) {
                hands.get(j).addCard(deckManager.drawCard());
            }
        }
    }

    public CollectionOfUnoCards getPlayerHand(int playerIndex) {
        return hands.get(playerIndex);
    }

    public boolean canPlayAnyCard(int playerIndex, UnoCard topCard) {
        CollectionOfUnoCards hand = hands.get(playerIndex);
        for (int i = 0; i < hand.getNumCards(); i++) {
            if (canPlay(hand.getCard(i), topCard)) {
                return true;
            }
        }
        return false;
    }

    private boolean canPlay(UnoCard cardToPlay, UnoCard topCard) {
        return cardToPlay.getColor() == topCard.getColor() ||
                cardToPlay.getNumber() == topCard.getNumber() ||
                cardToPlay.getNumber() == 13 ||
                cardToPlay.getNumber() == 14 ||
                cardToPlay.getNumber() == 15;
    }

    public void removeCardFromHand(int playerIndex, int cardIndex) {
        hands.get(playerIndex).remove(cardIndex);
    }

    public void addCardToHand(int playerIndex, UnoCard card) {
        hands.get(playerIndex).addCard(card);
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}