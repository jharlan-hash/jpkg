package com.jacksovern.jpkg;
import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    private final Socket socket;
    private final DataInputStream dataIn;
    private final DataOutputStream dataOut;
    
    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.dataIn = new DataInputStream(socket.getInputStream());
        this.dataOut = new DataOutputStream(socket.getOutputStream());
        System.out.println("Connected to server at " + host + ":" + port);
    }
    
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. List packages");
                System.out.println("2. Download package");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        getPackageList();
                        break;
                    case "2":
                        System.out.print("Enter package name to download: ");
                        String packageName = scanner.nextLine();
                        downloadPackage(packageName);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid option");
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }
    
    private void getPackageList() throws IOException {
        dataOut.writeUTF("getPackageList");
        dataOut.flush();
        
        int numPackages = dataIn.readInt();
        System.out.println("\nAvailable packages:");
        for (int i = 0; i < numPackages; i++) {
            String packageInfo = dataIn.readUTF();
            System.out.println(packageInfo);
        }
    }
    
    private void downloadPackage(String packageName) throws IOException {
        dataOut.writeUTF(packageName);
        dataOut.flush();
        
        receiveFile(packageName + ".zip");
    }
    
    private void receiveFile(String fileName) throws IOException {
        long size = dataIn.readLong();
        if (size == -1) {
            System.out.println("File not found on server");
            return;
        }
        
        System.out.println("Receiving file: " + fileName + " (size: " + size + " bytes)");
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] buffer = new byte[4*1024];
            int bytes;
            while (size > 0 && (bytes = dataIn.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
                System.out.println("Received " + bytes + " bytes, " + size + " remaining");
            }
        }
        System.out.println("File received: " + fileName);
    }
    
    private void close() {
        try {
            if (dataIn != null) dataIn.close();
            if (dataOut != null) dataOut.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 1234);
            client.start();
        } catch (IOException e) {
            System.err.println("Failed to start client: " + e.getMessage());
        }
    }
}
