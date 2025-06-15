# Source and binary directories
SRC_DIR = src
BIN_DIR = bin

# Expand the SOURCES variable
SOURCES := \
    $(SRC_DIR)/uno/cards/CollectionOfUnoCards.java \
    $(SRC_DIR)/uno/game/ExtremeUno.java \
    $(SRC_DIR)/uno/game/ExtremeUnoCollection.java \
    $(SRC_DIR)/uno/ExtremeUnoLauncher.java \
    $(SRC_DIR)/uno/game/GameStateManager.java \
    $(SRC_DIR)/uno/game/CardDeckManager.java \
    $(SRC_DIR)/uno/game/PlayerManager.java \
    $(SRC_DIR)/uno/game/HumanPlayerController.java \
    $(SRC_DIR)/uno/game/OutputRenderer.java \
    $(SRC_DIR)/uno/game/GameFlowController.java \
    $(SRC_DIR)/uno/game/Uno.java \
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

# Run the uno.game.Uno game
run: $(CLASSES)
	@echo "Running the uno.game.Uno game..."
	@java -cp $(BIN_DIR) uno.UnoGameLauncher

# Run the Extreme uno.game.Uno game
run_extreme: $(CLASSES)
	@echo "Running the Extreme uno.game.Uno game..."
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
