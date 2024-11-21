import java.util.Scanner;

public class ExtremeUnoLauncher {
    public ExtremeUnoLauncher() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players between 2 and 500: ");

        int numberOfPlayers;
        // Input validation for number of players
        for (numberOfPlayers = scanner.nextInt(); numberOfPlayers < 2 || numberOfPlayers > 500; numberOfPlayers = scanner.nextInt()) {
            System.out.print("Invalid number of players. Please enter a number between 2 and 500: ");
        }

        System.out.println("Select game mode:");
        System.out.println("1: Single Player (with AI)");
        System.out.println("2: Multiplayer (human players)");

        while (true) {
            System.out.print("Enter your choice (1 or 2): ");
            if (scanner.hasNextInt()) {
                int gameModeChoice = scanner.nextInt();
                if (gameModeChoice == 1) {
                    System.out.println("Select game mode:");
                    System.out.println("1: Normal");
                    System.out.println("2: 42");
                    System.out.println("3: Minecraft");

                    int modeChoice = 0;
                    while (true) {
                        System.out.print("Enter your game mode choice (1, 2, or 3): ");
                        if (scanner.hasNextInt()) {
                            modeChoice = scanner.nextInt();
                            if (modeChoice == 1) {
                                String gameMode = "Normal";
                                System.out.println("Starting Extreme Single Player Uno (Normal) with " + numberOfPlayers + " players...");
                                ExtremeUno singlePlayerGame = new ExtremeUno(numberOfPlayers, gameMode);
                                singlePlayerGame.playGame();
                                break; // Exit after starting the game
                            } else if (modeChoice == 2) {
                                System.out.println("42 is coming soon. Thank you for your patience.");
                                // Optional: You can return to the game mode selection or exit here.
                            } else if (modeChoice == 3) {
                                String gameMode = "Minecraft";
                                System.out.println("Starting Extreme Single Player Uno (Minecraft) with " + numberOfPlayers + " players...");
                                ExtremeUno singlePlayerGame = new ExtremeUno(numberOfPlayers, gameMode);
                                singlePlayerGame.playGame();
                                break; // Exit after starting the game
                            } else {
                                System.out.println("Invalid choice. Please select 1, 2, or 3.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                            scanner.next(); // Consume the invalid input
                        }
                    }
                    break; // Exit the game mode selection loop
                } else if (gameModeChoice == 2) {
                    System.out.println("Multiplayer mode is not available for Extreme Uno yet.");
                    System.out.println("Extreme multiplayer is coming soon. Thank you for your patience.");
                    break; // Exit gracefully after notifying the user
                } else {
                    System.out.println("Invalid choice. Please select 1 for Single Player or 2 for Multiplayer.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.next(); // Consume the invalid input
            }
        }

        scanner.close(); // Close the scanner before exiting
    }
}
