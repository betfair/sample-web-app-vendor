package com.betfair.vendor.domain;

/**
 * Container for a Json RPC response
 */
public abstract class ResponseContainer {

    private String jsonrpc;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @Override
    public String toString() {
        return "ResponseContainer{" +
                ", jsonrpc='" + jsonrpc + '\'' +
                '}';
    }
}
