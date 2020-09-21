package com.ntson.model;

public class Error {
    private String message;
    public Error(){

    }

    public Error(String message){
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
