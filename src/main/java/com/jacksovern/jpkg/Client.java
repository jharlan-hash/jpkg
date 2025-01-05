package com.jacksovern.jpkg;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    private static DataInputStream dataIn;
    private static DataOutputStream dataOut;
    

    public static void main (String[] args) throws Exception {
        Socket socket = getConnection("localhost", 1234);
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected to server...");

        ListManager listManager = new ListManager(socket);
        listManager.getPackageList();

        System.out.println("Enter package name to download: ");
        Scanner scanner = new Scanner(System.in);
        String packageName = scanner.nextLine();


    }

    public static Socket getConnection(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dataInputStream.readLong();     // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
    }
}
