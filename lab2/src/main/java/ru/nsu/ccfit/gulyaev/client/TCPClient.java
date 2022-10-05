package ru.nsu.ccfit.gulyaev.client;

import ru.nsu.ccfit.gulyaev.protocol.RequestPacket;
import ru.nsu.ccfit.gulyaev.protocol.ResponsePacket;
import ru.nsu.ccfit.gulyaev.protocol.CodeHeader;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TCPClient {

    private final Path path;
    private final int port;
    private final String host;


    public TCPClient(Path path, int port, String host){
        this.path = path;
        this.port = port;
        this.host = host;
    }

    public void startClient() throws IOException {
        System.out.println("Client started his work");
        try (Socket socket = new Socket(this.host, this.port);
             DataOutputStream fileWriter = new DataOutputStream(socket.getOutputStream());
             ObjectOutputStream objectWriter = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream());
             InputStream fileReader = Files.newInputStream(this.path)) {

            String fileName = this.path.getFileName().toString();

            long fileSize = Files.size(this.path);

            RequestPacket clientRequest = new RequestPacket(fileSize, fileName);

            objectWriter.writeObject(clientRequest);
            objectWriter.flush();

            ResponsePacket serverResponse = (ResponsePacket) objectReader.readObject();
            if (serverResponse.getResponseCode() == CodeHeader.FAILURE_REQUEST_CODE) {
                System.out.println("Failed sending request to server, goodbye!");
                return;
            }

            int buffSize = serverResponse.getServerBuffSize();
            byte[] buff = new byte[buffSize];
            int symbolsCounter;
            while ((symbolsCounter = fileReader.read(buff, 0, buffSize)) > 0) {

                fileWriter.write(buff, 0, symbolsCounter);
                fileWriter.flush();
            }

            serverResponse = (ResponsePacket) objectReader.readObject();
            if (serverResponse.getResponseCode() == CodeHeader.FAILURE_FILE_TRANSFER_CODE) {
                System.out.println("Failed sending file to server, goodbye!");
                return;
            }
            System.out.println("super!");
        } catch (UnknownHostException e) {
            System.out.println("Whaaat? undefined host? dead??? inside?");
        } catch (ClassNotFoundException e) {
            System.out.println("waaat?");
        } catch (IOException e) {
            System.out.println("socked - dead " + e.getMessage());
            e.printStackTrace();
        }
    }
}
