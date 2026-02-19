year ?= 2025
day ?= 4
part ?= 2

$(day):
	javac -g -d $(year)/$(day)/$(part)/target/classes -Xlint:unchecked $(year)/$(day)/$(part)/main.java
	java -ea -classpath $(year)/$(day)/$(part)/target/classes Main

# usage `make cls=[class imlemented into interested file]`
