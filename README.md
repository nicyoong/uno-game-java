# UNO Game Project

## Overview

This UNO project is a Java implementation of the classic card game that includes different modes to accommodate various types of gameplay. The game supports:

- **Single Player Mode**: Play against computer opponents, with the ability to configure up to 10 opponents.
- **Extreme Mode**: A variant of UNO that allows for a larger player base, accommodating up to 500 players.
- **Multiplayer Mode**: Engage in a classic UNO experience with up to 10 human players.
- **42 Mode**: A unique variant of UNO that introduces the SegFault card, adding new strategic elements to the game.

## Features

- **Single Player Mode**: 
  - Play against AI opponents.
  - Customize the number of opponents (up to 10).
  - Game logic handles turns, drawing cards, and determining winners.

- **Extreme Mode**:
  - Designed for larger groups of players.
  - Up to 500 players can join in the fun.
  - Enhanced gameplay mechanics to manage the increased number of players.

- **Multiplayer Mode**:
  - Compete with friends and family.
  - Up to 10 players can join in local multiplayer.
  - Supports standard UNO rules and features.
  
- **42 Mode**:
  - A special variant of UNO featuring the SegFault card.
  - The SegFault card requires the next player to skip a turn, and following player must draw 2 cards before playing.
  - Adds an extra layer of strategy and unpredictability to the game.
  - Available in both single player and multiplayer formats.

## Getting Started

### Prerequisites

To run this project, you will need:

- Java Development Kit (JDK) installed on your machine.
- A terminal or command prompt for executing commands.

### Building the Project

1. **Clone the repository**:

Git clone the repository's URL

Then cd into the directory

2. **Navigate to the project directory**:
Ensure you are in the directory containing the `Makefile`.

3. **Build the project**:
Run the following command to compile the Java files:

make

### Running the Game

- **To run the standard UNO game**:

make run

- **To run the extreme version of the game**:

make run_extreme

Note: extreme version is single player only

### Cleaning Up

- **To clean up compiled class files**:

make clean

- **To fully clean up the project (remove binary directory)**:

make fclean

## Contributing

Contributions are welcome! If you have suggestions or improvements, feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.