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

            System.out.println("============");
            System.out.println("Connection accepted from " + clientSocket.toString());
            processMessage();

        } catch (Exception e) {
            return;
        }
    }

    private void processMessage() throws Exception {
        int operationType = dataInputStream.readInt();
        if (operationType == OperationType.UPLOAD_OPERATION.getId()) {
            System.out.println("Server received valid operation request to upload a file.");
            sendServerResponse(ServerResponse.SERVER_READY.getId());
            readFile(processFileName());
        } else if (operationType == OperationType.DOWNLOAD_OPERATION.getId()) {
            System.out.println("Server received valid operation request to download a file");
            sendServerResponse(ServerResponse.SERVER_READY.getId());
            writeFile(processFileName());
        } else {
            System.out.println("Invalid operation.");
            sendServerResponse(ServerResponse.INVALID_OPERATION.getId());
            clientSocket.close();
        }
    }

    private void writeFile(String fileName) throws Exception {
        File file = new File(directoryPath + File.separator + fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            sendServerResponse(ServerResponse.FILE_FOUND_SUCCESSFULLY.getId());
            FileStreamingService.writeFile(dataOutputStream, fileInputStream, file.length());
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

    private void readFile(String fileName) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(directoryPath + File.separator + fileName);
        FileStreamingService.readFile(dataInputStream, fileOutputStream);
    }

    private void sendServerResponse(int response) throws IOException {
        dataOutputStream.writeInt(response);
    }

}
