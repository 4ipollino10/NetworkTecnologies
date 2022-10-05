package ru.nsu.ccfit.gulyaev.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        String host = "";
        int port = 4040;

        String filePath = "D:\\work\\seti\\dope\\Task.dat";

        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            System.out.println("nas uje na nulevoy snesli -> no such file" + filePath);
            return;
        }
        TCPClient client = new TCPClient(path, port, host);

        client.startClient();

        System.out.println("Done");
    }
}
