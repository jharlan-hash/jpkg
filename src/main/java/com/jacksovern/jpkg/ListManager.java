package com.jacksovern.jpkg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ListManager
 */
public class ListManager {
    private Socket socket;
    private String PACKAGE_PATH = "~/.local/share/jpkglist";
    public ArrayList<Package> packageList = new ArrayList<>();

    public ListManager(Socket socket) {
        this.socket = socket;
    }

    public void getPackageList() throws IOException {
        if (new File(PACKAGE_PATH).exists()) {
            readPackageListFromFile();
        } else {
            System.out.println("Package list not found. Fetching from server...");
            getPackageListFromServer();
        }
    }

    private void readPackageListFromFile() throws IOException {
        try {
            File myObj = new File(PACKAGE_PATH);
            Scanner myReader = new Scanner(myObj);
            
            if (myObj.createNewFile()) {
                writePackageListToFile();
            }

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }}

    private void writePackageListToFile() {
        try {
            File myObj = new File(PACKAGE_PATH);
            if (myObj.createNewFile()) {
                for (Package pack : packageList) {
                    System.out.println(pack.toJSON());
                }
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<Package> getPackageListFromServer() throws IOException{
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeUTF("getPackageList");

        int listlen = dataIn.readInt();

        for (int i = 0; i < listlen; i++) { // Read package list
            String json = dataIn.readUTF();

            Package pack = Package.fromJSON(json); // convert data to package object
            packageList.add(pack); // add package to list
        }

        return packageList;
    }
}
