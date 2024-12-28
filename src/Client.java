// File: Client.java
// Author: Jack Sovern

// client file for the package manager project

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

class Client {
    public static Package[] currentList = {
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

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 10000));
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        Log.info("Connected to server");

        System.out.println("Enter the name of the package you want to get from the server");
        String testPackageName = scanner.nextLine();
        ClientRequest req = new ClientRequest(testPackageName, currentList, System.currentTimeMillis() - 1000000);

        out.writeInt(req.toBytes().length);
        out.write(req.toBytes());

        DataInputStream in = new DataInputStream(socket.getInputStream());
        try {
            while(true) {
                System.out.println(in.readUTF());
            }
        } catch (Exception e) {
            Log.warn("Done reading");
        } finally {
            Log.info("Closing socket");
            scanner.close();
            socket.close();
        }
    }
}
