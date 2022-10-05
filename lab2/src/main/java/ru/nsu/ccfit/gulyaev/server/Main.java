package ru.nsu.ccfit.gulyaev.server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final String EXIT = "e";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        TCPServer server = new TCPServer(4040);
        Thread serverThread = new Thread(server);
        serverThread.start();
        while (true) {
            String inputString = scanner.nextLine();
            if (inputString.equals(EXIT)) {
                server.stopServer();
                break;
            }
        }
    }
}
