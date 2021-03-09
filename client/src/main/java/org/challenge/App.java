package org.challenge;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Scanner;

public class App {

    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        SecretKeySpec secretKeySpec = KeyFactory.generateAESKey();

        System.out.println("Welcome to the file transfer client.");
        System.out.println("Please enter absolute path of the client working directory (no trailing slash):");
        String path = scan.nextLine();
        Client client = new Client(path, secretKeySpec);
        while (true) {
            System.out.println("Choose an operation: \n 1 - upload \n 2 - download \n 3 - exit");
            int operation = scan.nextInt();

            switch (operation) {
                case 1:
                    client.uploadFileOperation(scan);
                    break;
                case 2:
                    client.downloadFileOperation(scan);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid operation.");
            }
        }
    }

}
