package ru.nsu.ccfit.gulyaev.server;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServer implements Runnable{
    private final int port;

    private static final int BUFF_SIZE = 1024;

    private boolean isWorking = true;

    private ServerSocket serverSocket;

    TCPServer(int port){
        this.port = port;
    }

    private void openSocket(){
        try{
            this.serverSocket = new ServerSocket(this.port);
        }catch (IOException e){
            System.out.println("server is dead... inside?");
        }
    }

    public void stopServer() throws IOException {
        this.isWorking = false;
        this.serverSocket.close();
    }

    @Override
    public void run() {
        this.openSocket();

        Socket clientSocket;
        while(isWorking) {
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                System.out.println("client dead, sorry(");
                return;
            }

            Thread clientTask = null;
            try {
                clientTask = new Thread(new ClientTask(clientSocket, BUFF_SIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientTask.start();
        }

    }
}
