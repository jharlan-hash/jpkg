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
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_LIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNBOLD = "\u001B[21m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_STOP_UNDERLINE = "\u001B[24m";
    public static final String ANSI_BLINK = "\u001B[5m";
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
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

        Package[] currentList = getPackageList(); // get the current list of packages from the server

        int requestLength = in.readInt();
        ClientRequest req = ClientRequest.fromBytes(in.readNBytes(requestLength));

        checkPackageListValidity(clientSocket, req, currentList);
        checkPackageValidity(clientSocket, req);

        System.out.println("Closing connection");
        serverSocket.close();
        clientSocket.close();
    }

    private static void checkPackageListValidity(Socket clientSocket, ClientRequest req, Package[] currentList) throws IOException {
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
    }

    private static void checkPackageValidity(Socket clientSocket, ClientRequest req) throws IOException {
        if (req.getPackage() != null) {
            // if the client requested a package, send the package
            System.out.println("Client requested package " + req.getPackage().getName());
            if (serverHasPackage(req.getPackage())) {
                System.out.println("Server has package " + req.getPackage().getName());
                sendPackage(clientSocket, req.getPackage());
            } else {
                sendError(clientSocket, "Package not found");
            }
        } else {
            sendError(clientSocket, "Invalid request");
        }
    }

    private static void sendError(Socket clientSocket, String errorMessage) throws IOException {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

        System.out.println("Sending error message: " + errorMessage);
        out.writeUTF(ANSI_RED + "ERROR " + ANSI_RESET + errorMessage);
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

    private static void sendPackageList(Socket clientSocket) {
        // Send the package list to the client
    }

    private static void sendPackage(Socket clientSocket, Package packageToSend) {
        // Send the package to the client
        System.out.println("Sent package");
    }

    private static boolean serverHasPackage(Package queriedPackage) {
        // Check if the server has the package
        for (Package p : getPackageList()) {
            if (p.getName().equals(queriedPackage.getName())) {
                return true;
            }
        }
        
        return false;
    }
}
