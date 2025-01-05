package com.jacksovern.jpkg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ListManager
 */
public class ListManager {
    private Socket socket;
    private String PACKAGE_PATH = "~/.local/share/jpkglist";
    private ArrayList<Package> packageList;

    public ListManager(Socket socket) {
        this.socket = socket;
    }

    public void getPackageList() throws IOException {
        if (new File(PACKAGE_PATH).exists()) {
            readPackageList();
        } else {
            System.out.println("Package list not found. Fetching from server...");
            getPackageListFromServer();
        }
    }

    public void checkForPackage(String packageName) {
        // Check if package is available
    }

    private void readPackageList() {
        // Read package list from file
    }

    private ArrayList<Package> getPackageListFromServer() throws IOException{
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeUTF("getPackageList");

        int listlen = dataIn.readInt();

        for (int i = 0; i < listlen; i++) { // Read package list
            String json = dataIn.readUTF();

            Package pack = Request.fromJSON(json); // convert data to package object
            System.out.println(pack.toJSON());
            packageList.add(pack); // add package to list
        }

        return packageList;
    }
}
