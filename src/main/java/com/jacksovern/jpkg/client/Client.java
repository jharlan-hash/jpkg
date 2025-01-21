package com.jacksovern.jpkg.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Client {
    private final Socket socket;
    private final DataInputStream dataIn;
    private final DataOutputStream dataOut;
    private final String[] packageList;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.dataIn = new DataInputStream(socket.getInputStream());
        this.dataOut = new DataOutputStream(socket.getOutputStream());
        this.packageList = getPackageList();
        // System.out.println("Connected to server at " + host + ":" + port);
    }

    public void start(String[] args) {
        try {
            String choice = args[0];

            switch (choice) {
                case "list":
                    System.out.println("\nAvailable packages:");
                    for (String string : packageList) {
                        System.out.println(string);
                    }
                    break;
                case "install":
                    installPackage(args);
                    break;
                case "help":
                    printUsage();
                    break;
                case "search":
                    if (searchPackage(args)) {
                        System.out.println("Package " + args[1] + " found");
                    } else {
                        System.out.println("Package not found");
                    }
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printUsage();
        } finally {
            close();
        }
    }

    private boolean searchPackage(String[] args) {
        try {
            for (String pack : packageList) {
                if (pack.contains(args[1])) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return false;
        }
    }

    private void installPackage(String[] args) {
        try {
            String packageName = args[1];
            downloadPackage(packageName);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please provide a package name");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void printUsage() {
        System.out.println("Example usage:");
        System.out.println("jpkg search PACKAGE");
        // System.out.println("jpkg info PACKAGE");
        System.out.println("jpkg install PACKAGE");
        System.out.println("jpkg update");

        System.out.println("\nFurther Help:");
        System.out.println("jpkg commands");
    }

    private String[] getPackageList() throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        dataOut.writeUTF("getPackageList");
        dataOut.flush();

        int numPackages = dataIn.readInt();
        for (int i = 0; i < numPackages; i++) {
            String packageInfo = dataIn.readUTF();
            list.add(packageInfo);
        }

        numPackages = 0;
        return list.toArray(new String[list.size()]);
    }

    private void downloadPackage(String packageName) throws IOException {
        dataOut.writeUTF(packageName);
        dataOut.flush();

        receiveFile(packageName);
    }

    private void receiveFile(String fileName) throws IOException {
        long size = dataIn.readLong();
        if (size == -1) {
            System.out.println("File not found on server");
            return;
        }

        System.out.println(
                "Receiving file: " + fileName + " (size: " + size + " bytes)");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[4 * 1024];
            int bytes;
            while (size > 0 &&
                    (bytes = dataIn.read(
                            buffer,
                            0,
                            (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
                System.out.println(
                        "Received " + bytes + " bytes, " + size + " remaining");
            }
        }
        System.out.println("File received: " + fileName);
    }

    private void close() {
        try {
            if (dataIn != null)
                dataIn.close();
            if (dataOut != null)
                dataOut.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 1234);
            client.start(args);
        } catch (IOException e) {
            System.err.println("Failed to start client: " + e.getMessage());
        }
    }
}
