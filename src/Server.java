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
        Log.info("Server started on port " + PORT_NUMBER);
        Log.info("Waiting for client to connect...");

        // Wait for a client to connect
        Socket clientSocket = serverSocket.accept();
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

        Package[] currentList = getPackageList(); // get the current list of packages from the server

        int requestLength = in.readInt();
        ClientRequest req = ClientRequest.fromBytes(in.readNBytes(requestLength));

        checkPackageListValidity(clientSocket, req, currentList);
        checkPackageValidity(clientSocket, req);

        Log.info("Closing connection");
        serverSocket.close();
        clientSocket.close();
    }

    private static void checkPackageListValidity(Socket clientSocket, ClientRequest req, Package[] currentList) throws IOException {
        if (req.getPackageList() == null){
            // if they don't have a package list, send the package list
            sendPackageList(clientSocket);
            Log.warn("User has no package list - sending package list");
        } else if (req.timeSinceUpdate() > UPDATE_DELAY) {
            // if it's been a while since an update, send the package list
            sendPackageList(clientSocket);
            Log.warn("User's package list is outdated - sending package list");
        } else if (!req.getPackageList().equals(currentList)) {
            //temporaty for testing
            sendPackageList(clientSocket);
            Log.warn("User's package list is mismatched - sending package list");
        }
    }

    private static void checkPackageValidity(Socket clientSocket, ClientRequest req) throws IOException {
        if (req.getPackageName() != null) {
            // if the client requested a package, send the package
            Log.info("Client requested package " + req.getPackageName());
            if (serverHasPackage(req.getPackageName())) {
                Log.info("Server has package " + req.getPackageName());
                sendPackage(clientSocket, sendServerPackage(req.getPackageName()));
            } else {
                sendError(clientSocket, "Package not found");
            }
        } else {
            sendError(clientSocket, "Invalid request");
        }
    }

    private static void sendError(Socket clientSocket, String errorMessage) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        Log.warn("Sending error message: " + errorMessage);
        out.writeUTF(Log.ANSI_RED + "ERROR " + Log.ANSI_RESET + errorMessage);
    }

    private static Package[] getPackageList() {
        Package[] packageList = {
            new Package("package1", "1.0", "This is package 1", 0),
            new Package("package2", "1.0", "This is package 2", 1),
            new Package("package3", "1.0", "This is package 3", 2),
            new Package("package4", "1.0", "This is package 4", 3),
            new Package("package5", "1.0", "This is package 5", 4),
            new Package("package6", "1.0", "This is package 6", 5),
            new Package("package7", "1.0", "This is package 7", 6),
            new Package("package8", "1.0", "This is package 8", 7),
            new Package("package9", "1.0", "This is package 9", 8),
            new Package("package10", "1.0", "This is package 10", 9)
        };
        return packageList;
    }

    private static Package sendServerPackage(String packageName) {
        // Get the package from the server
        for (Package p : getPackageList()) {
            if (p.getName().equals(packageName)) {
                Log.info("Package " + packageName + " found");
                return p;
            }
        }
        Log.warn("Package not found");
        return null;
    }

    private static void sendPackageList(Socket clientSocket) {
        // Send the package list to the client
    }

    private static void sendPackage(Socket clientSocket, Package packageToSend) throws IOException {
        // Send the package to the client
        Log.info("Sending package " + packageToSend.getName());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeUTF(Log.ANSI_GREEN + "INFO" + Log.ANSI_RESET + "Package " + packageToSend.getName() + " sent");
    }

    private static boolean serverHasPackage(String packageName) {
        // Check if the server has the package
        for (Package p : getPackageList()) {
            if (p.getName().equals(packageName)) {
                return true;
            }
        }

        return false;
    }
}
