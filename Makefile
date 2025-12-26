day ?= 1
year ?= 2025

$(day):
	javac -g -d $(year)/$(day)/target/classes -Xlint:unchecked $(year)/$(day)/Main.java
	java -ea -classpath $(year)/$(day)/target/classes Main

# usage `make cls=[class imlemented into interested file]`
