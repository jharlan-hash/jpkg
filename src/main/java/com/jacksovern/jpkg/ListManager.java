package com.jacksovern.jpkg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * ListManager
 */
public class ListManager {
    private Socket socket;
    private String PACKAGE_PATH = "~/.local/share/jpkglist";

    public ListManager(Socket socket) {
        this.socket = socket;
    }

    public void getPackageList() throws IOException {
        if (new File(PACKAGE_PATH).exists()) {
            readPackageList();
        } else {
            getPackageListFromServer();
        }
    }

    public void checkForPackage(String packageName) {
        // Check if package is available
    }

    private void readPackageList() {
        // Read package list from file
    }

    private void getPackageListFromServer() throws IOException{
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

        dataOut.writeUTF("getPackageList");

        int datalen = dataIn.readInt();
        byte[] data = new byte[datalen];

        dataIn.read(data);

        Package pack = new Package();

    }
}

