package uno.game;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;

public class CardDeckManager {
    private CollectionOfUnoCards deck;
    private CollectionOfUnoCards discardPile;

    public CardDeckManager(String gameMode) {
        deck = new CollectionOfUnoCards();
        discardPile = new CollectionOfUnoCards();
        initializeDeck(gameMode);
    }

    private void initializeDeck(String gameMode) {
        deck.makeDeck(gameMode);
        deck.shuffle();
    }

    public void shuffleDiscardPileIntoDeck() {
        if (discardPile.getNumCards() <= 1) return;

        UnoCard topCard = discardPile.getTopCard();
        discardPile.removeFromTop();

        while (discardPile.getNumCards() > 0) {
            deck.addCard(discardPile.removeFromTop());
        }

        deck.shuffle();
        discardPile.addCard(topCard);
    }

    public UnoCard drawCard() {
        if (deck.getNumCards() == 0) {
            shuffleDiscardPileIntoDeck();
        }
        return deck.removeFromTop();
    }

    public void addToDiscardPile(UnoCard card) {
        discardPile.addCard(card);
    }

    public UnoCard getTopDiscardCard() {
        return discardPile.getTopCard();
    }

    public CollectionOfUnoCards getDeck() {
        return deck;
    }

    public CollectionOfUnoCards getDiscardPile() {
        return discardPile;
    }
}