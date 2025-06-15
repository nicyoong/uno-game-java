package uno.game;

import uno.cards.CollectionOfUnoCards;
import uno.cards.UnoCard;
import java.util.Scanner;

public class HumanPlayerController {
    private Scanner scanner;

    public HumanPlayerController() {
        scanner = new Scanner(System.in);
    }

    public int promptColorSelection() {
        System.out.println("Choose a color:");
        System.out.println("1: Yellow, 2: Red, 3: Green, 4: Blue");
        int colorChoice = -1;
        while (colorChoice < 1 || colorChoice > 4) {
            System.out.print("Enter the color number: ");
            colorChoice = scanner.nextInt();
            if (colorChoice < 1 || colorChoice > 4) {
                System.out.println("Invalid choice. Please choose between 1 and 4.");
            }
        }
        return colorChoice - 1;
    }

    public int selectCardToPlay(CollectionOfUnoCards hand, UnoCard topCard) {
        int chosenCardIndex = -1;
        while (true) {
            System.out.print("Choose a card to play by entering its index: ");
            if (scanner.hasNextInt()) {
                chosenCardIndex = scanner.nextInt();
                if (chosenCardIndex >= 1 && chosenCardIndex <= hand.getNumCards()) {
                    int actualIndex = chosenCardIndex - 1;
                    UnoCard chosenCard = hand.getCard(actualIndex);

                    if (chosenCard.getNumber() == 14 && hand.canPlayExcludingWildDrawFour(topCard)) {
                        System.out.println("You have other playable cards. You cannot play a Wild Draw Four now.");
                        continue;
                    }

                    if (chosenCard.canPlay(topCard)) {
                        return actualIndex;
                    } else {
                        System.out.println("Card not playable. Choose a different card.");
                    }
                } else {
                    System.out.println("Invalid index. Choose again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }
}