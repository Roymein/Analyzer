package com.example.d2j.writer;

public class DexWriteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DexWriteException() {
        super();

    }

    public DexWriteException(String message) {
        super(message);

    }

    public DexWriteException(String message, Throwable cause) {
        super(message, cause);

    }

    public DexWriteException(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    public DexWriteException(Throwable cause) {
        super(cause);

    }

}
