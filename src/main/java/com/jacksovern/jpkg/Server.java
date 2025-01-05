package com.jacksovern.jpkg;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

class Server {
    private static ArrayList<Package> packageList = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
                
                Thread clientHandler = new Thread(() -> handleClient(socket));
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Server stopped.");
        }
    }
    
    private static void handleClient(Socket socket) {
        try (
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataIn = new DataInputStream(socket.getInputStream())
        ) {
            while (!socket.isClosed()) {
                String request = dataIn.readUTF();
                System.out.println("Request from " + socket.getInetAddress() + ": " + request);
                
                if (request.equals("getPackageList")) {
                    sendPackageList(dataOut);
                } else {
                    Package pack = findPackage(request);
                    if (pack != null) {
                        sendFile(pack.getName() + ".zip", dataOut);
                    } else {
                        System.out.println("Package not found: " + request);
                        dataOut.writeUTF("ERROR: Package not found");
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected: " + socket.getInetAddress());
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private static Package findPackage(String request) {
        for (Package pack : packageList) {
            System.out.println("Checking package: " + pack.getName());
            if (pack.getName().equals(request)) {
                System.out.println("Package found: " + pack.toJSON());
                return pack;
            }
        }
        return null;
    }

    private static void sendPackageList(DataOutputStream dataOut) throws IOException {
        packageList.add(new Package("test", "1.0", "Test package", 1));
        packageList.add(new Package("test2", "1.0", "Test package 2", 1));
        
        dataOut.writeInt(packageList.size());

        for (Package pack : packageList) {
            dataOut.writeUTF(pack.toJSON());
        }

        dataOut.flush();
        System.out.println("Package list sent.");
    }

    private static void sendFile(String path, DataOutputStream dataOut) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            dataOut.writeLong(-1); // Indicate file not found
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            dataOut.writeLong(file.length());
            System.out.println("Sending file: " + path + " (size: " + file.length() + " bytes)");
            
            byte[] buffer = new byte[4*1024];
            int bytes;
            while ((bytes = fileInputStream.read(buffer)) != -1) {
                dataOut.write(buffer, 0, bytes);
                System.out.println("Sent " + bytes + " bytes");
                dataOut.flush();
            }
        }
    }
}
