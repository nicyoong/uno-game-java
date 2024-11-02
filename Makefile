# Source and binary directories
SRC_DIR = src
BIN_DIR = bin

# Expand the SOURCES variable
SOURCES := $(SRC_DIR)/CollectionOfUnoCards.java \
           $(SRC_DIR)/ExtremeUno.java \
           $(SRC_DIR)/ExtremeUnoCollection.java \
           $(SRC_DIR)/ExtremeUnoLauncher.java \
           $(SRC_DIR)/SinglePlayerUno.java \
           $(SRC_DIR)/Uno.java \
           $(SRC_DIR)/UnoCard.java \
           $(SRC_DIR)/UnoGameLauncher.java

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
	@java -cp $(BIN_DIR) UnoGameLauncher

# Run the Extreme Uno game
run_extreme: $(CLASSES)
	@echo "Running the Extreme Uno game..."
	@java -cp $(BIN_DIR) ExtremeUnoLauncher

# Clean target
clean:
	@echo "Cleaning up compiled classes..."
	@rm -rf $(BIN_DIR)/*.class

# Full clean target
fclean: clean
	@echo "Removing binary directory..."
	@rm -rf $(BIN_DIR)

# Phony targets
.PHONY: all clean fclean run run_extreme
