// File: Client.java
// Author: Jack Sovern

// client file for the package manager project

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 10000));

        System.out.println("Connected to server");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        while(in.available() != 0) {
            in.readUTF();
        }

        socket.close();
    }
}
