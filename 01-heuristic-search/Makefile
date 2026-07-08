JAVAC = javac
JAVA = java
JAR = jar

SRC_DIR = src
BIN_DIR = bin
LIB_DIR = libs
JAR_LIBS = $(LIB_DIR)/AIMA.jar
MAIN_CLASS = Main_tot
JAR_NAME = Practica1.jar
MANIFEST = manifest.txt


CLASSES_SRC = $(SRC_DIR)/Classes/*.java
PRAC_SRC = $(SRC_DIR)/pracBusquedaLocal/*.java
MAIN_SRC = $(SRC_DIR)/$(MAIN_CLASS).java


ALL_SRC = $(CLASSES_SRC) $(PRAC_SRC) $(MAIN_SRC)

CLASSPATH = $(BIN_DIR):$(JAR_LIBS):$(SRC_DIR)

#s'executen en ordre

all: compile_classes compile_prac compile_main
compile_classes:
	@mkdir -p $(BIN_DIR)
	@echo "=== Compiling Classes package ==="
	$(JAVAC) -cp "$(CLASSPATH)" -d $(BIN_DIR) $(CLASSES_SRC)

compile_prac: compile_classes
	@echo "=== Compiling pracBusquedaLocal package ==="
	$(JAVAC) -cp "$(CLASSPATH)" -d $(BIN_DIR) $(PRAC_SRC)

compile_main: compile_prac
	@echo "=== Compiling main class ==="
	$(JAVAC) -cp "$(CLASSPATH)" -d $(BIN_DIR) $(MAIN_SRC)

$(MANIFEST):
	@echo "Main-Class: $(MAIN_CLASS)" > $(MANIFEST)
	@echo "Class-Path: $(JAR_LIBS)" >> $(MANIFEST)

jar: all $(MANIFEST)
	@echo "=== Creating runnable JAR ==="
	$(JAR) cfm $(JAR_NAME) $(MANIFEST) -C $(BIN_DIR) .

# Run the program with optional arguments
run: jar
	$(JAVA) -jar $(JAR_NAME) $(ARGS)

# Clean compiled classes and JAR
clean:
	@echo "=== Cleaning bin and jar files ==="
	rm -rf $(BIN_DIR)/* $(JAR_NAME) $(MANIFEST)
