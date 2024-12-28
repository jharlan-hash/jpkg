import java.net.*;
import java.io.*;

// Server.java
/*
* The Server class is the main class for the server side of the package manager.
* The server will accept a connection from a client and then send the client a list of all the packages that are - 
* available for download if the client has an outdated list
* If the client has an updated list, the server will query itself to see if the server has the package specified by the client
* If the server has the package, the server will send the package to the client
* If the server does not have the package, the server will send the client a message saying that the package is not available
* The server will then close the connection with the client
*/
class Server {
    private static int UPDATE_DELAY = 1209600000; // 2 weeks in millis
    private static int PORT_NUMBER = 10000;

    public static void main(String[] args) {
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() throws Exception {
        connect();
    }

    private static void connect() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("Server started on port " + PORT_NUMBER);
        System.out.println("Waiting for client to connect...");

        // Wait for a client to connect
        Socket clientSocket = serverSocket.accept();

        Package[] currentList = getPackageList();
        //TEST MODE
        ClientRequest req = new ClientRequest(new Package("hi", "1.0", "fortnite", 0), new Package[0], 0);

        if (req.getPackageList() == null){
            // if they don't have a package list, send the package list
            sendPackageList(clientSocket);
            System.out.println("Sent package list because user has no package list");
        } else if (req.timeSinceUpdate() > UPDATE_DELAY) {
            // if it's been a while since an update, send the package list
            sendPackageList(clientSocket);
            System.out.println("sent package list because of time");
        } else if (!req.getPackageList().equals(currentList)) {
            //temporaty for testing
            sendPackageList(clientSocket);
            System.out.println("sent package list because of list mismatch");
        }

        if (req.getPackage() != null) {
            // if the client requested a package, send the package
            System.out.println("Client requested package");
            if (serverHasPackage(req.getPackage())) {
                System.out.println("Server has package");
                sendPackage(clientSocket, req.getPackage());
                System.out.println("Sent package");
            } else {
                System.out.println("Server does not have package");
                sendError(clientSocket, "Package not found");
            }
        } else {
            System.out.println("Invalid request");
            sendError(clientSocket, "Invalid request");
        }

        System.out.println("Closing connection");
        serverSocket.close();
        clientSocket.close();
    }

    private static void sendError(Socket clientSocket, String errorMessage) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeUTF(errorMessage);
    }

    private static Package[] getPackageList() {
        Package[] packageList = new Package[0];
        return packageList;
    }

    private static void sendPackageList(Socket clientSocket) {
        // Send the package list to the client
    }

    private static void sendPackage(Socket clientSocket, Package packageToSend) {
        // Send the package to the client
    }

    private static boolean serverHasPackage(Package queriedPackage) {
        // Check if the server has the package
        return true;
    }
}
