package com.jacksovern.jpkg;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

class Server {
    private static ArrayList<Package> packageList = new ArrayList<>();
    private static DataOutputStream dataOut;
    private static DataInputStream dataIn;

    public static void main (String[] args) throws Exception {
        System.out.println("Server is running...");
        ServerSocket serverSocket = new ServerSocket(1234);

        while (true) {
            Socket socket = serverSocket.accept();
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());

            String request = dataIn.readUTF();
            System.out.println("Request: " + request);

            if (request.equals("getPackageList")) {
                sendPackageList();

            } else {
                Package pack = findPackage(request);

                if (pack != null) {
                    sendFile(pack.getName() + ".zip");

                } else {
                    dataOut.writeUTF("Package not found.");
                }
            }

            socket.close();
            packageList.clear();
        }
    }

    private static Package findPackage(String request) {

        for (Package pack : packageList) {
            if (pack.getName().equals(request)) {
                System.out.println("Package found: " + pack.toJSON());
                return pack;
            }
        }

        return null;
    }

    private static void sendPackageList() {
        try {
            packageList.add(new Package("test", "1.0", "Test package", 1));
            packageList.add(new Package("test2", "1.0", "Test package 2", 1));

            dataOut.writeInt(packageList.size()); // list length (number of packages)

            for (Package pack : packageList) {
                dataOut.writeUTF(pack.toJSON());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        dataOut.writeLong(file.length());  
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOut.write(buffer,0,bytes);
            dataOut.flush();
        }
        fileInputStream.close();
    }
}
