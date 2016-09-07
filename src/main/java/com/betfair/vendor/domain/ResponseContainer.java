package com.betfair.vendor.domain;


public abstract class ResponseContainer {

    private Error error;
    private String jsonrpc;

    public Error getError() {
        return error;
    }
    public void setError(Error error) {
        this.error = error;
    }
    public String getJsonrpc() {
        return jsonrpc;
    }
    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @Override
    public String toString() {
        return "ResponseContainer{" +
                "error=" + error +
                ", jsonrpc='" + jsonrpc + '\'' +
                '}';
    }
}