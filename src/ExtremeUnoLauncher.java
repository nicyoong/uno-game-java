import java.util.Scanner;

public class ExtremeUnoLauncher {
   public ExtremeUnoLauncher() {
   }

   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter the number of players between 2 and 60: ");

      int numberOfPlayers;
      // Input validation for number of players
      for (numberOfPlayers = scanner.nextInt(); numberOfPlayers < 2 || numberOfPlayers > 60; numberOfPlayers = scanner.nextInt()) {
         System.out.print("Invalid number of players. Please enter a number between 2 and 60: ");
      }

      System.out.println("Select game mode:");
      System.out.println("1: Single Player (with AI)");
      System.out.println("2: Multiplayer (human players)");

      while (true) {
         System.out.print("Enter your choice (1 or 2): ");
         if (scanner.hasNextInt()) {
            int gameMode = scanner.nextInt();
            if (gameMode == 1) {
               System.out.println("Starting Extreme Single Player Uno with " + numberOfPlayers + " players...");
               ExtremeUno singlePlayerGame = new ExtremeUno(numberOfPlayers);
               singlePlayerGame.playGame();
               break; // Exit after starting the game
            } else if (gameMode == 2) {
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
