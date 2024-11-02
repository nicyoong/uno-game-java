import java.util.Scanner;

public class UnoGameLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the number of players for both game modes
        System.out.print("Enter the number of players between 2 and 8: ");
        int numPlayers = scanner.nextInt();

        // Validate the input for multiplayer game mode
        while (numPlayers < 2 || numPlayers > 8) {
            System.out.print("Invalid number of players. Please enter a number between 2 and 8: ");
            numPlayers = scanner.nextInt();
        }

        // Game mode selection
        System.out.println("Select game mode:");
        System.out.println("1: Single Player (with AI)");
        System.out.println("2: Multiplayer (human players)");

        int choice = 0;

        while (true) {
            System.out.print("Enter your choice (1 or 2): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    break; // Valid input, exit the loop
                } else {
                    System.out.println("Invalid choice. Please select 1 for Single Player or 2 for Multiplayer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.next(); // Clear the invalid input
            }
        }

        // Launch the selected game mode
        if (choice == 1) {
            // Start Single Player Uno
            System.out.println("Starting Single Player Uno with " + numPlayers + " players...");
            SinglePlayerUno singlePlayerGame = new SinglePlayerUno(numPlayers);
            singlePlayerGame.playGame(); // Assuming there's a playGame method in SinglePlayerUno
        } else {
            // Start Multiplayer Uno
            System.out.println("Starting Multiplayer Uno with " + numPlayers + " players...");
            Uno multiplayerGame = new Uno(numPlayers);
            multiplayerGame.playGame(); // Assuming there's a playGame method in Uno
        }

        scanner.close();
    }
}
