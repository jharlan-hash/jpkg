client:
	javac -d ./bin ./src/*.java
	java -cp ./bin Client

server:
	javac -d ./bin ./src/*.java
	java -cp ./bin Server
