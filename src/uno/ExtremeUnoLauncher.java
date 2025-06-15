package uno;

import uno.singleplayergame.ExtremeUno;
import java.util.Scanner;
import java.util.logging.*;

public class ExtremeUnoLauncher {
    private static final Logger LOGGER = Logger.getLogger(ExtremeUnoLauncher.class.getName());

    static {
        // Configure SimpleFormatter to only output the message
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "%5$s%n"
        );
        // Send ConsoleHandler output to stdout
        System.setProperty("java.util.logging.ConsoleHandler.target", "SYSTEM_OUT");

        // Configure root logger: remove default handlers and add our ConsoleHandler
        Logger root = Logger.getLogger(".");
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        root.addHandler(handler);
        root.setLevel(Level.ALL);
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int numberOfPlayers = promptNumberOfPlayers(scanner);
            int mode = promptMainMenuChoice(scanner);

            if (mode == 1) {
                handleSinglePlayer(numberOfPlayers, scanner);
            } else {
                handleMultiplayer();
            }
        }
    }

    private static int promptNumberOfPlayers(Scanner scanner) {
        LOGGER.info("Enter the number of players between 2 and 500:");
        int players;
        while (true) {
            if (scanner.hasNextInt()) {
                players = scanner.nextInt();
                if (players >= 2 && players <= 500) {
                    return players;
                }
            } else {
                scanner.next(); // consume invalid token
            }
            LOGGER.warning("Invalid number of players. Please enter a number between 2 and 500:");
        }
    }

    private static int promptMainMenuChoice(Scanner scanner) {
        LOGGER.info("Select game mode:");
        LOGGER.info("1: Single Player (with AI)");
        LOGGER.info("2: Multiplayer (human players)");

        while (true) {
            LOGGER.info("Enter your choice (1 or 2):");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice == 1 || choice == 2) {
                    return choice;
                }
            } else {
                scanner.next();
            }
            LOGGER.warning("Invalid input. Please select 1 for Single Player or 2 for Multiplayer.");
        }
    }

    private static void handleSinglePlayer(int numberOfPlayers, Scanner scanner) {
        int singleMode = promptSinglePlayerMode(scanner);
        String gameModeName = modeName(singleMode);

        if (singleMode == 2) {
            LOGGER.info("42 is coming soon. Thank you for your patience.");
            return;
        }

        logGameStart(numberOfPlayers, gameModeName);
        new ExtremeUno(numberOfPlayers, gameModeName).playGame();
    }

    private static int promptSinglePlayerMode(Scanner scanner) {
        LOGGER.info("Select single-player mode:");
        LOGGER.info("1: Normal");
        LOGGER.info("2: 42");
        LOGGER.info("3: Minecraft");

        while (true) {
            LOGGER.info("Enter your game mode choice (1, 2, or 3):");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
            } else {
                scanner.next();
            }
            LOGGER.warning("Invalid choice. Please select 1, 2, or 3.");
        }
    }

    private static String modeName(int modeChoice) {
        switch (modeChoice) {
            case 1:
                return "Normal";
            case 3:
                return "Minecraft";
            default:
                return "Unknown";
        }
    }

    private static void logGameStart(int players, String gameMode) {
        LOGGER.log(
                Level.INFO,
                "Starting Extreme Single Player Uno ({0}) with {1} players…",
                new Object[]{gameMode, players}
        );
    }

    private static void handleMultiplayer() {
        LOGGER.info("Multiplayer mode is not available for Extreme Uno yet.");
        LOGGER.info("Extreme multiplayer is coming soon. Thank you for your patience.");
    }
}
