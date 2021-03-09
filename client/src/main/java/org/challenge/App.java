package org.challenge;

import java.io.*;
import java.net.Socket;

public class App {

    private static final int UPLOAD_OPERATION = 1;
    private static final int DOWNLOAD_OPERATION = 2;

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {

        File file = new File("/Users/stefanveselinovic/Desktop/testfile.pdf");
        uploadFile(file);
    }

    private static void uploadFile(File file) {
        try(Socket socket = new Socket("localhost",5005)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            requestOperation(UPLOAD_OPERATION);

            if (isServerReady()) {
                sendFile(file);
            }
            // sendFile("path/to/file2.pdf");

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

}
