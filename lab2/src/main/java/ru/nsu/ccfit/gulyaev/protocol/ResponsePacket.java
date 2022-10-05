package ru.nsu.ccfit.gulyaev.protocol;

import java.io.Serializable;

public class ResponsePacket implements Serializable {

    private final CodeHeader code;
    private int buffSize = 0;



    public ResponsePacket(CodeHeader code, int buffSize){
        this.code = code;
        this.buffSize = buffSize;
    }

    public ResponsePacket(CodeHeader code){
        this.code = code;
    }

    public CodeHeader getResponseCode(){
        return this.code;
    }

    public int getServerBuffSize(){
        return this.buffSize;
    }
}
