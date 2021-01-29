package com.ntson.model;

public class Server {
    private String config;
    private String country;
    private String userName;
    private String password;


    public Server() {
    }

    public Server(String config, String country, String ovpnUserName, String ovpnUserPassword) {
        this.config = config;
        this.country = country;
        this.userName = ovpnUserName;
        this.password = ovpnUserPassword;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String ovpnUserName) {
        this.userName = ovpnUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String ovpnUserPassword) {
        this.password = ovpnUserPassword;
    }
}
