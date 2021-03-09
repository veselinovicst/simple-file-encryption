package org.challenge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5005;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public Server() { }

    public void service() {
        System.out.println("Starting server ...");
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (Exception e){
            e.printStackTrace();
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // start new thread for every client
            new ServerThread(clientSocket).start();
        }
    }

}
