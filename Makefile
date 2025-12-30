year ?= 2025
day ?= 1
part ?= 1

$(day):
	javac -g -d $(year)/$(day)/$(part)/target/classes -Xlint:unchecked $(year)/$(day)/$(part)/Main.java
	java -ea -classpath $(year)/$(day)/$(part)/target/classes Main

# usage `make cls=[class imlemented into interested file]`
