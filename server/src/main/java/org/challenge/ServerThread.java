package org.challenge;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket clientSocket;
    private String directoryPath;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public ServerThread(Socket clientSocket, String directoryPath) {
        this.clientSocket = clientSocket;
        this.directoryPath = directoryPath;
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
        int operationType = dataInputStream.readInt();
        if (operationType == OperationType.UPLOAD_OPERATION.getId()) {
            System.out.println("Server received valid operation request");
            sendServerResponse(ServerResponse.SERVER_READY.getId());
            writeFile(processFileName());
        } else if (operationType == OperationType.DOWNLOAD_OPERATION.getId()) {
            System.out.println("Server received valid operation request");
            sendServerResponse(ServerResponse.SERVER_READY.getId());
            sendFile(processFileName());
        } else {
            System.out.println("Invalid operation");
            sendServerResponse(ServerResponse.INVALID_OPERATION.getId());
            clientSocket.close();
        }
    }

    private void sendFile(String fileName) throws IOException {
        File file = new File(directoryPath + File.separator + fileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            dataOutputStream.writeLong(file.length());

            int bytes = 0;
            byte[] buffer = new byte[4096];
            while ((bytes = inputStream.read(buffer)) != -1){
                dataOutputStream.write(buffer,0, bytes);
            }
            inputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("File does not exist");
            sendServerResponse(ServerResponse.FILE_NOT_FOUND.getId());
            clientSocket.close();
        }
    }

    private String processFileName() throws IOException {
        int fileNameSize = dataInputStream.readInt();
        byte[] fileNameBytes = new byte[fileNameSize];
        dataInputStream.read(fileNameBytes);
        return new String(fileNameBytes);
    }

    private void writeFile(String fileName) throws IOException {
        long fileSize = dataInputStream.readLong();
        FileOutputStream fileOutputStream = new FileOutputStream(directoryPath + File.separator + fileName);
        byte[] buffer = new byte[4096];

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
