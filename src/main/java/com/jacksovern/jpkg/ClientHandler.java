package com.jacksovern.jpkg;

import java.io.*;
import java.net.*;

class ClientHandler {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void start() {
        try {
            System.out.println("Client connected...");
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            String request = dataIn.readUTF();
            System.out.println("Request: " + request);

            if (request.equals("getPackageList")) {
                dataOut.writeInt(1); // list length (number of packages)
                dataOut.writeUTF(new Package("test", "1.0", "Test package", 1).toJSON()); // package data
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            socket.close();
            System.out.println("Client disconnected...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
