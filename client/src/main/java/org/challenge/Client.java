package org.challenge;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5005;

    private static final int UPLOAD_OPERATION = 1;
    private static final int DOWNLOAD_OPERATION = 2;

    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    private String workingDirectoryPath;
    private SecretKeySpec secretKeySpec;

    public Client(String workingDirectoryPath, SecretKeySpec secretKeySpec) {
        this.workingDirectoryPath = workingDirectoryPath;
        this.secretKeySpec = secretKeySpec;
    }

    public void uploadFileOperation(Scanner scan) throws Exception {
        scan.nextLine();
        System.out.println("Enter absolute file path:");
        String path = scan.nextLine();

        Path filePath = Paths.get(path);
        FileInputStream fileInputStream = new FileInputStream(path);

        File file = new File(filePath.getFileName().toString());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        FileEncryptionService.encrypt(secretKeySpec, fileInputStream, fileOutputStream);

        upload(file);

        fileOutputStream.close();
        file.delete();

        System.out.println("File successfully uploaded.");
    }

    public void downloadFileOperation(Scanner scan) throws Exception {
        scan.nextLine();
        System.out.println("Enter file name:");
        String fileName = scan.nextLine();

        File file = new File(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        download(fileOutputStream, fileName);
        fileOutputStream.close();

        FileEncryptionService.decrypt(secretKeySpec, new FileInputStream(file), new FileOutputStream(workingDirectoryPath + File.separator + fileName));
        file.delete();

        System.out.println("File successfully downloaded.");
    }

    private void upload(File file) throws Exception {
        openServerConnection();
        requestOperation(UPLOAD_OPERATION);

        if (isServerReady()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // file name size and then file name
            dataOutputStream.writeInt(file.getName().length());
            dataOutputStream.write(file.getName().getBytes());

            FileStreamingService.writeFile(dataOutputStream, fileInputStream, file.length());
        }

        closeServerConnection();
    }

    private void download(FileOutputStream fileOutputStream, String fileName) throws IOException {
        openServerConnection();

        requestOperation(DOWNLOAD_OPERATION);
        if (isServerReady()) {
            // send file name size and then file name
            dataOutputStream.writeInt(fileName.length());
            dataOutputStream.write(fileName.getBytes());
            if (isFileFound()) {
                FileStreamingService.readFile(dataInputStream, fileOutputStream);
            }
        }

        closeServerConnection();
    }

    private void openServerConnection() {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeServerConnection() throws IOException {
        dataInputStream.close();
        dataInputStream.close();
    }

    private void requestOperation(int operation) throws IOException {
        dataOutputStream.writeInt(operation);
    }

    private boolean isServerReady() throws IOException {
        int response = dataInputStream.readInt();
        // SERVER_READY = 1
        return response == 1;
    }

    private boolean isFileFound() throws IOException {
        int response = dataInputStream.readInt();
        // FILE_FOUND_SUCCESSFULLY = 3
        return response == 3;
    }

}
