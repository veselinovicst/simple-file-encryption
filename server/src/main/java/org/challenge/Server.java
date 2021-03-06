package org.challenge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5005;
    public String directoryPath;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Server(String directoryPath) {
        this.directoryPath = directoryPath;
    }

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
            // start new thread for every client connection
            new ServerThread(clientSocket, directoryPath).start();
        }
    }

}
