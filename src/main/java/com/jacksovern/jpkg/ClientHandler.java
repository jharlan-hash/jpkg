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
