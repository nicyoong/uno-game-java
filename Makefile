# Makefile for building Java project

# Compiler
JAVAC = javac

# Source and binary directories
SRC_DIR = src
BIN_DIR = bin

# List of Java source files
SOURCES = $(SRC_DIR)/CollectionOfUnoCards.java \
          $(SRC_DIR)/ExtremeUno.java \
          $(SRC_DIR)/ExtremeUnoCollection.java \
          $(SRC_DIR)/ExtremeUnoLauncher.java \
          $(SRC_DIR)/SinglePlayerUno.java \
          $(SRC_DIR)/Uno.java \
          $(SRC_DIR)/UnoCard.java \
          $(SRC_DIR)/UnoGameLauncher.java

# Compile all .java files to .class files in the bin directory
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target to build all .class files
all: $(CLASSES)

# Pattern rule to compile .java files to .class files in the bin directory
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java | $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $<

# Ensure bin directory exists
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Run the UnoGameLauncher class
run: all
	java -cp $(BIN_DIR) UnoGameLauncher

# Run the ExtremeUnoLauncher class
run_extreme: all
	java -cp $(BIN_DIR) ExtremeUnoLauncher

# Clean up all .class files in the bin directory
clean:
	rm -rf $(BIN_DIR)/*.class

# Full clean - removes bin directory completely
fclean: clean
	rm -rf $(BIN_DIR)

# Phony targets
.PHONY: all clean fclean run run_extreme
