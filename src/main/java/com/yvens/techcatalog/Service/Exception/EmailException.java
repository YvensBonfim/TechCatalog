package com.yvens.techcatalog.Service.Exception;

@SuppressWarnings("serial")
public class EmailException extends RuntimeException {

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}