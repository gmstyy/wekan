package com.wekan.model;

/**
 * Created by yuanyuan06 on 2015/12/11.
 */
public class MsgException extends RuntimeException {
    public MsgException(String detailMessage) {
        super(detailMessage);
    }

    public MsgException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MsgException(Throwable throwable) {
        super(throwable);
    }
}
