public class RecentAction {
    private final int playerId;       // ID of the player who made the move
    private final String actionType; // The type of action (e.g., "Played", "Drew", "Skipped")
    private final UnoCard card;      // The card involved in the action (nullable for actions like "Drew")

    public RecentAction(int playerId, String actionType, UnoCard card) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.card = card;
    }

    // Getters
    public int getPlayerId() {
        return playerId;
    }

    public String getActionType() {
        return actionType;
    }

    public UnoCard getCard() {
        return card;
    }

    // A specific method to convert RecentAction to string for turn memory display
    public String toTurnMemoryString() {
        String cardString = (card != null) ? card.toString() : "None";
        return "Player " + (playerId + 1) + " " + actionType + " " + cardString;
    }
}
