package org.challenge;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Connection accepted from " + clientSocket.toString());
            processMessage();

        } catch (IOException e) {
            return;
        }
    }

    private void processMessage() throws IOException {
        processOperation();
        processFile();
    }

    private void processOperation() throws IOException {
        int operationType = dataInputStream.readInt();
        if (operationType == OperationType.DOWNLOAD_OPERATION.getId() || operationType == OperationType.UPLOAD_OPERATION.getId()) {
            System.out.println("Server received valid operation request");
            sendServerResponse(ServerResponse.SERVER_READY.getId());
        } else {
            System.out.println("Invalid operation");
            sendServerResponse(ServerResponse.INVALID_OPERATION.getId());
            clientSocket.close();
        }
    }

    private void processFile() throws IOException {
        int fileNameSize = dataInputStream.readInt();
        byte[] fileNameBytes = new byte[fileNameSize];
        dataInputStream.read(fileNameBytes);
        String fileName = new String(fileNameBytes);
        long fileSize = dataInputStream.readLong();

        readFile(fileName, fileSize);
    }

    private void readFile(String fileName, long fileSize) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[4 * 1024];

        int bytes;
        while (fileSize > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            fileSize -= bytes;
        }
        fileOutputStream.close();
    }

    private void sendServerResponse(int response) throws IOException {
        dataOutputStream.writeInt(response);
    }

}
