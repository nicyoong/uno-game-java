# Source and binary directories
SRC_DIR = src
BIN_DIR = bin

# Expand the SOURCES variable
SOURCES := \
    $(SRC_DIR)/uno/cards/CollectionOfUnoCards.java \
    $(SRC_DIR)/uno/singleplayergame/ExtremeUno.java \
    $(SRC_DIR)/uno/singleplayergame/ExtremeUnoCollection.java \
    $(SRC_DIR)/uno/ExtremeUnoLauncher.java \
    $(SRC_DIR)/uno/singleplayergame/GameStateManager.java \
    $(SRC_DIR)/uno/singleplayergame/CardDeckManager.java \
    $(SRC_DIR)/uno/singleplayergame/PlayerManager.java \
    $(SRC_DIR)/uno/singleplayergame/HumanPlayerController.java \
    $(SRC_DIR)/uno/singleplayergame/OutputRenderer.java \
    $(SRC_DIR)/uno/game/SinglePlayerUno.java \
	$(SRC_DIR)/uno/game/MultiplayerGame.java \
    $(SRC_DIR)/uno/multiplayergame/PlayerManager.java \
    $(SRC_DIR)/uno/multiplayergame/OutputRenderer.java \
    $(SRC_DIR)/uno/multiplayergame/HumanPlayerController.java \
    $(SRC_DIR)/uno/multiplayergame/GameStateManager.java \
    $(SRC_DIR)/uno/multiplayergame/CardDeckManager.java \
    $(SRC_DIR)/uno/cards/UnoCard.java \
    $(SRC_DIR)/uno/effects/CardEffectHandler.java \
    $(SRC_DIR)/uno/effects/SingleCardEffectHandler.java \
    $(SRC_DIR)/uno/model/RecentAction.java \
    $(SRC_DIR)/uno/ai/AIPlayer.java \
    $(SRC_DIR)/uno/UnoGameLauncher.java

# Compile all .java files to .class files in the bin directory
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Main target
all: $(BIN_DIR) $(CLASSES)

# Create the binary directory
$(BIN_DIR):
	@mkdir -p $(BIN_DIR)

# Rule to compile all Java files at once
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	@echo "Compiling all Java files to class files..."
	@echo "Running command: javac -d $(BIN_DIR) $(SOURCES)"
	@javac -d $(BIN_DIR) $(SOURCES)

# Run the Uno game
run: $(CLASSES)
	@echo "Running the Uno game..."
	@java -cp $(BIN_DIR) uno.UnoGameLauncher

# Run the Extreme Uno game
run_extreme: $(CLASSES)
	@echo "Running the Extreme Uno game..."
	@java -cp $(BIN_DIR) uno.ExtremeUnoLauncher

# Clean target
clean:
	@echo "Cleaning up compiled classes..."
	@rm -rf $(BIN_DIR)/*.class

# Full clean target
fclean: clean
	@echo "Removing binary directory..."
	@rm -rf $(BIN_DIR)

# Recompile
re: fclean all

# Phony targets
.PHONY: all clean fclean re run run_extreme
