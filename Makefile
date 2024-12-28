client:
	javac ./src/client/*.java
	java -cp ./src/client/ Client

server:
	javac ./src/server/*.java
	java -cp ./src/server/ Server
