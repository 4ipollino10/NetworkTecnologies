package ru.nsu.ccfit.gulyaev.protocol;

import java.io.Serializable;

public class RequestPacket implements Serializable {
    private final long fileSize;
    private final String fileName;

    public RequestPacket(long fileSize, String fileName){
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    public long getFileSize(){
        return this.fileSize;
    }

    public String getFileName(){
        return this.fileName;
    }
}
