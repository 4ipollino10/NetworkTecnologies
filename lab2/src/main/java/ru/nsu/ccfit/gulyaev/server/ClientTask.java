package ru.nsu.ccfit.gulyaev.server;

import ru.nsu.ccfit.gulyaev.protocol.CodeHeader;
import ru.nsu.ccfit.gulyaev.protocol.RequestPacket;
import ru.nsu.ccfit.gulyaev.protocol.ResponsePacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTask implements Runnable {
    private final Socket clientSocket;

    private static final long SPEED_TEST_INTERVAL = 3000;

    private FileContext fileContext;

    private final ObjectInputStream objectReader;
    private final ObjectOutputStream objectWriter;
    private final DataInputStream fileReader;

    private final int BUFF_SIZE;

    private long currentSpeed = 0;

    private long currTime;

    private final long startTime;

    private long currReadBytes;



    public ClientTask(Socket clientSocket, int bufSize) throws IOException {
        this.clientSocket = clientSocket;
        this.BUFF_SIZE = bufSize;

        this.startTime = System.currentTimeMillis();

        this.currTime = this.startTime;


        this.objectReader = new ObjectInputStream(clientSocket.getInputStream());
        this.objectWriter = new ObjectOutputStream(clientSocket.getOutputStream());
        this.fileReader = new DataInputStream(clientSocket.getInputStream());
    }




    @Override
    public void run() {
        RequestPacket clientRequest;
        try {
            clientRequest = (RequestPacket) objectReader.readObject();

            this.prepareFileForWriting(clientRequest);

            ResponsePacket responsePacket = new ResponsePacket(CodeHeader.SUCCSSESFUL_REQUEST_CODE, BUFF_SIZE);

            this.objectWriter.writeObject(responsePacket);
            this.objectWriter.flush();

            try {
                byte[] buffer = new byte[BUFF_SIZE];
                while(this.fileContext.getReadBytes() < clientRequest.getFileSize()) {
                    this.printClientSpeedStatistic();
                    int read = fileReader.read(buffer, 0, BUFF_SIZE);
                    this.fileContext.writeData(buffer, read);
                }
            } catch (Exception e) {
                this.cleanUp();
                System.out.println("failed to read file");
            }

            this.printClientSpeedStatistic();

            if(this.fileContext.isFileDownoadCorrectly()){
                long speed = this.fileContext.getReadBytes() * 1000 / (System.currentTimeMillis() - this.startTime);
                this.objectWriter.writeObject(new ResponsePacket(CodeHeader.SUCC_FILE_TRANSFER_CODE));
                System.out.println("file: " + this.fileContext.getFileName() + " downloaded avg speed = " + speed + " byte/sec");
                this.cleanUp();
                return;
            }
            this.objectWriter.writeObject(new ResponsePacket(CodeHeader.FAILURE_FILE_TRANSFER_CODE));
            this.cleanUp();



        } catch (Exception e) {
            try {
                this.cleanUp();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("Something goes wrong");

        }
    }
    private void prepareFileForWriting (RequestPacket clientRequest) throws IOException {
        this.fileContext = new FileContext(clientRequest.getFileSize(), clientRequest.getFileName());
    }

    private void printClientSpeedStatistic(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.currTime > SPEED_TEST_INTERVAL){
            this.currentSpeed = (this.fileContext.getReadBytes() - this.currReadBytes) * 1000 / (currentTime - this.currTime);
            long avgSpeed = this.fileContext.getReadBytes() * 1000 / (currentTime - this.startTime);
            System.out.println("file: " + this.fileContext.getFileName() + " - is downloading. current speed = " + this.currentSpeed + " byte/sec" + ", avg speed = " + avgSpeed + " byte/sec");
            this.currTime = currentTime;
            this.currReadBytes = this.fileContext.getReadBytes();
        }
    }



    private void cleanUp () throws IOException {
        this.fileContext.close();
        this.objectWriter.close();
        this.clientSocket.close();
        this.fileReader.close();
        this.objectReader.close();
    }

}
