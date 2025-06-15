package uno;

import uno.game.SinglePlayerUno;
import uno.game.Uno;
import java.util.Scanner;
import java.util.logging.*;

public class UnoGameLauncher {
    private static final Logger LOGGER = Logger.getLogger(UnoGameLauncher.class.getName());

    static {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "%5$s%n"  // %5$s is the message, %n is newline
        );
        System.setProperty("java.util.logging.ConsoleHandler.target", "SYSTEM_OUT");
        Logger root = Logger.getLogger("");
        for (Handler handler : root.getHandlers()) {
            root.removeHandler(handler);
        }
        ConsoleHandler stdoutHandler = new ConsoleHandler();
        stdoutHandler.setLevel(Level.ALL);
        stdoutHandler.setFormatter(new SimpleFormatter());
        root.addHandler(stdoutHandler);
        root.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LOGGER.info("Enter the number of players between 2 and 10:");
        int numPlayers = readInt(scanner, 2, 10, "Invalid number of players. Please enter a number between 2 and 10:");

        LOGGER.info("Select game mode:\n1: Single Player (with AI)\n2: Multiplayer (human players)");
        int choice = readInt(scanner, new int[]{1, 2}, "Invalid choice. Please select 1 for Single Player or 2 for Multiplayer.");

        String modeLabel = (choice == 1) ? "Single Player" : "Multiplayer";
        LOGGER.log(Level.INFO,
                "Select game mode for {0}:\n1: Normal Mode\n2: 42 Mode\n3: Minecraft Mode",
                new Object[]{modeLabel}
        );
        int modeChoice = readInt(scanner, new int[]{1, 2, 3}, "Invalid choice. Please select 1, 2, or 3.");
        String gameMode = switch (modeChoice) {
            case 2 -> "42";
            case 3 -> "Minecraft";
            default -> "Normal";
        };

        if (choice == 1) {
            LOGGER.info("Please choose a difficulty level:\n1 - Easy\n2 - Realistic");
            int difficulty = readInt(scanner, new int[]{1, 2}, "Invalid input. Please enter 1 for Easy or 2 for Realistic.");
            LOGGER.log(Level.INFO,
                    "Starting Single Player Uno with {0} players in {1} mode and difficulty {2}...",
                    new Object[]{numPlayers, gameMode, difficulty}
            );
            SinglePlayerUno gameController = new SinglePlayerUno(numPlayers, gameMode, difficulty);
            gameController.playGame();
        } else {
            LOGGER.log(Level.INFO,
                    "Starting Multiplayer Uno with {0} players in {1} mode...",
                    new Object[]{numPlayers, gameMode}
            );
            Uno Uno = new Uno(numPlayers, gameMode);
            Uno.playGame(gameMode);
        }

        scanner.close();
    }

    private static int readInt(Scanner scanner, int min, int max, String errorMsg) {
        while (true) {
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                if (val >= min && val <= max) {
                    return val;
                }
            } else {
                scanner.next();
            }
            LOGGER.warning(errorMsg);
        }
    }

    private static int readInt(Scanner scanner, int[] validOptions, String errorMsg) {
        while (true) {
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                for (int opt : validOptions) {
                    if (val == opt) {
                        return val;
                    }
                }
            } else {
                scanner.next();
            }
            LOGGER.warning(errorMsg);
        }
    }
}