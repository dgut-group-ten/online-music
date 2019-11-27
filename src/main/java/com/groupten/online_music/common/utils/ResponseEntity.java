package com.groupten.online_music.common.utils;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {
    private int status;
    private boolean success = true;
    private String message;
    private String token;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public ResponseEntity success(boolean success) {
        this.setSuccess(success);
        return this;
    }

    public ResponseEntity message(String data) {
        this.setMessage(data);
        return this;
    }

    public ResponseEntity data(T data) {
        this.setData(data);
        return this;
    }

    public ResponseEntity status(HttpStatus status) {
        this.setStatus(status.value());
        return this;
    }

    public ResponseEntity token(String token) {
        this.setToken(token);
        return this;
    }
}
