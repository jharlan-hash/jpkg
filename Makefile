client:
	javac ./src/server/*.java
	java -cp ./src/server/ Client

server:
	javac ./src/server/*.java
	java -cp ./src/server/ Server
