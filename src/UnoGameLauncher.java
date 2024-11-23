import java.util.Scanner;

public class UnoGameLauncher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of players between 2 and 10: ");
        int numPlayers = scanner.nextInt();

        while (numPlayers < 2 || numPlayers > 10) {
            System.out.print("Invalid number of players. Please enter a number between 2 and 10: ");
            numPlayers = scanner.nextInt();
        }

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

        String gameMode = "";
        if (choice == 1) {
            System.out.println("Select game mode for Single Player:");
            System.out.println("1: Normal Mode");
            System.out.println("2: 42 Mode");
            System.out.println("3: Minecraft Mode");
        } else {
            System.out.println("Select game mode for Multiplayer:");
            System.out.println("1: Normal Mode");
            System.out.println("2: 42 Mode");
            System.out.println("3: Minecraft Mode");
        }

        int modeChoice = 0;

        while (true) {
            System.out.print("Enter your choice (1, 2, or 3): ");
            if (scanner.hasNextInt()) {
                modeChoice = scanner.nextInt();
                if (modeChoice == 1 || modeChoice == 2 || modeChoice == 3) {
                    if (modeChoice == 1) {
                        gameMode = "Normal";
                    } else if (modeChoice == 2) {
                        gameMode = "42";
                    } else {
                        gameMode = "Minecraft";  // Handles modeChoice == 3
                    }
                    break;
                } else {
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                scanner.next();
            }
        }

        // Launch the selected game mode
        if (choice == 1) {
            // Add code to ask for difficulty setting
            int difficulty;
            
            difficulty = 1; // We set this to 1 first, later on there will be 2
            System.out.println("Starting Single Player Uno with " + 
                                numPlayers + " players in " + 
                                gameMode + " mode and difficulty " + 
                                difficulty + "...");
            SinglePlayerUno singlePlayerGame = new SinglePlayerUno(numPlayers, gameMode);
            singlePlayerGame.playGame(gameMode, difficulty);
        } else {
            System.out.println("Starting Multiplayer Uno with " + numPlayers + " players in " + gameMode + " mode...");
            Uno multiplayerGame = new Uno(numPlayers, gameMode);
            multiplayerGame.playGame(gameMode);
        }

        scanner.close();
    }
}
