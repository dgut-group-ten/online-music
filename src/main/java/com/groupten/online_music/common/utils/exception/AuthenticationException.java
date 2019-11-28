package com.groupten.online_music.common.utils.exception;

public class AuthenticationException extends RuntimeException{
    public final int code;

    public AuthenticationException(int code) {
        this.code = code;
    }
    public AuthenticationException() {
        this(0, null, (Throwable) null);
    }

    public AuthenticationException(String message) {
        this(0, message, (Throwable) null);
    }

    public AuthenticationException(Throwable cause) {
        this(0, null, cause);
    }

    public AuthenticationException(String message, Throwable cause) {
        this(0, message, cause);
    }

    public AuthenticationException(int code, String message) {
        this(code, message, (Throwable) null);
    }

    public AuthenticationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
