package ru.nsu.ccfit.gulyaev.protocol;

public enum CodeHeader {
    SUCCSSESFUL_REQUEST_CODE(10),
    SUCC_FILE_TRANSFER_CODE(11),

    FAILURE_REQUEST_CODE(20),
    FAILURE_FILE_TRANSFER_CODE(21);


    private final int code;

    CodeHeader(int code){
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
