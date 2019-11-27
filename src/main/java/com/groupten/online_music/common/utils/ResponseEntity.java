package com.groupten.online_music.common.utils;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {
    private String message;
    private String token;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity() {
    }

    public ResponseEntity message(String data) {
        this.setMessage(data);
        return this;
    }

    public ResponseEntity data(T data) {
        this.setData(data);
        return this;
    }

    public ResponseEntity token(String token) {
        this.setToken(token);
        return this;
    }
}
