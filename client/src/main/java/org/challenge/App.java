package org.challenge;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class App {

    private static final int UPLOAD_OPERATION = 1;
    private static final int DOWNLOAD_OPERATION = 2;
    private static final int BUFFER_SIZE = 4096;

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to file transfer client.");
        while (true) {
            System.out.println("Choose an operation: \n 1 - upload \n 2 - download \n 3 - exit");
            int operation = scan.nextInt();

            switch (operation) {
                case 1:
                    uploadFileOperation();
                    break;
                case 2:
                    downloadFileOperation();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid operation.");
            }
        }
    }

    private static void uploadFileOperation() {
        scan.nextLine();
        System.out.println("Enter absolute file path:");
        String path = scan.nextLine();
        File file = new File(path);
        uploadFile(file);
        System.out.println("File successfully uploaded.");
    }

    private static void downloadFileOperation() {
        scan.nextLine();
        System.out.println("Enter file name:");
        String fileName = scan.nextLine();
        openConnectionAndDownload(fileName);
    }

    private static void openConnectionAndDownload(String fileName) {
        try(Socket socket = new Socket("localhost",5005)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Downloading file");

            requestOperation(DOWNLOAD_OPERATION);
            if (isServerReady()) {
                downloadFile(fileName);
            }

            dataInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void uploadFile(File file) {
        try(Socket socket = new Socket("localhost",5005)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            requestOperation(UPLOAD_OPERATION);

            if (isServerReady()) {
                sendFile(file);
            }

            dataInputStream.close();
            dataInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void requestOperation(int operation) throws IOException {
        dataOutputStream.writeInt(operation);
    }

    private static boolean isServerReady() throws IOException {
        int response = dataInputStream.readInt();
        return response == 1;
    }

    private static void sendFile(File file) throws Exception{
        int bytes = 0;
        int fileNameSize = file.getName().length();
        FileInputStream fileInputStream = new FileInputStream(file);

        // file name size
        dataOutputStream.writeInt(fileNameSize);
        // file name
        dataOutputStream.write(file.getName().getBytes());
        // send file size
        dataOutputStream.writeLong(file.length());
        // send file in chunks
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0, bytes);
        }
        fileInputStream.close();
    }

    private static void downloadFile(String fileName) throws IOException {
        // todo first read if file exists on the server
        // file name size
        dataOutputStream.writeInt(fileName.length());
        // file name
        dataOutputStream.write(fileName.getBytes());

        long fileSize = dataInputStream.readLong();
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/stefanveselinovic/Desktop/testfiles/" + fileName);
        byte[] buffer = new byte[4096];

        int bytes;
        while (fileSize > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            fileSize -= bytes;
        }
        fileOutputStream.close();
    }

}
