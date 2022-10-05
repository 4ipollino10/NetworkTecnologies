package ru.nsu.ccfit.gulyaev.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class FileContext {
    private Path filePath;

    private final static AtomicInteger fileCounter = new AtomicInteger(0);

    private final OutputStream fileWriter;

    private String fileName;

    private final long fileSize;

    private long readBytes = 0;


    public FileContext(long fileSize, String fileName) throws IOException {

        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createFile();
        this.fileWriter = Files.newOutputStream(this.filePath);
    }


    private void createFile() throws IOException {

        Path directoryPath = Paths.get("uploads");
        if(Files.notExists(directoryPath)){
            Files.createDirectory(directoryPath);
        }
        Path filePath = Paths.get(directoryPath + "\\" + this.fileName);
        if(Files.exists(filePath)){
            this.fileName = this.fileCounter.addAndGet(1) + this.fileName;
            System.out.println(this.fileName);
            filePath = Paths.get(directoryPath + "\\" + this.fileName);
        }
        Files.createFile(filePath);
        this.filePath = filePath;
    }

    public boolean isFileDownoadCorrectly(){
        return this.readBytes == this.fileSize;
    }

    public void writeData(byte[] buffer, int read) throws IOException {
        this.fileWriter.write(buffer, 0, read);
        this.readBytes += read;
    }

    public void close() throws IOException {
        this.fileWriter.close();
    }

    public long getReadBytes(){
        return this.readBytes;
    }

    public String getFileName(){
        return this.fileName;
    }
}
