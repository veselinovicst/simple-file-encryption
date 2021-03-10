package org.challenge;

import java.io.*;

public class FileStreamingService {

    private static final int BUFFER_SIZE = 4096;

    public static void writeFile(DataOutputStream dataOutputStream, FileInputStream inputStream, long fileSize) throws Exception {
        // send file size
        dataOutputStream.writeLong(fileSize);
        // send file in chunks
        int bytes = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytes = inputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
        }
        inputStream.close();
    }

    public static void readFile(DataInputStream dataInputStream, FileOutputStream fileOutputStream) throws IOException {
        long fileSize = dataInputStream.readLong();
        byte[] buffer = new byte[BUFFER_SIZE];

        int bytes = 0;
        while (fileSize > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            fileSize -= bytes;
        }
        fileOutputStream.close();
    }

}
