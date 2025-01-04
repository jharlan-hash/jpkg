package com.jacksovern.jpkg;

import java.io.IOException;
import java.net.*;

class Client {
    public static void main (String[] args) throws Exception {
        Socket socket = getConnection("localhost", 1234);
        System.out.println("Connected to server...");

        Package pack = new Package("test", "1.0", "Test package", 1);
        System.out.println(pack.toJSON());

        ListManager listManager = new ListManager(socket);
        listManager.getPackageList();


    }

    public static Socket getConnection(String host, int port) throws IOException {
        return new Socket(host, port);
    }
}
