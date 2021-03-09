package org.challenge;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Server server = new Server(setup());
        server.service();
    }

    private static String setup() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter absolute path of server directory (no trailing slash):");
        String path = scan.nextLine();
        System.out.println("The path you entered is: " + path);
        return path;
    }

}
