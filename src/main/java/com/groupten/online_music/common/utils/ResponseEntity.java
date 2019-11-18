package com.groupten.online_music.common.utils;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {
    private int status;
    private boolean success = true;
    private String message;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity() {
    }

    public ResponseEntity(boolean success) {
        this.success = success;
    }

    public ResponseEntity(T data) {
        this.data = data;
    }

    public ResponseEntity(int status) {
        this.status = status;
    }

    public ResponseEntity(T data, HttpStatus status) {
        this.data = data;
        this.status = status.value();
    }

    public static ResponseEntity ofSuccess(boolean s) {
        return new ResponseEntity(s);
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
}
