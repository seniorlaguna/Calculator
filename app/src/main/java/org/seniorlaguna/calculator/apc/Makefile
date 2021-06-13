JFLEX  = jflex 
BYACCJ = jyacc -J -Jclass=APCParser -Jpackage=org.seniorlaguna.calculator.apc
JAVAC  = javac

# targets:

all: APCParser.class

run: APCParser.class
	java APCParser

build: clean APCParser.class

clean:
	rm -f *~ *.class APCLexer.java APCParser.java

APCParser.class: APCLexer.java APCParser.java
	$(JAVAC) APCParser.java

APCLexer.java: APCLexer.flex
	$(JFLEX) APCLexer.flex

APCParser.java: APCParser.y
	$(BYACCJ) APCParser.y