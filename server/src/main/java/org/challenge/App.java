package org.challenge;

import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        Server server = new Server(setup());
        server.service();
    }

    private static String setup() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter absolute path of server directory:");
        String path = scan.nextLine();
        System.out.println("The path you entered is: " + path);
        return path;
    }

}
