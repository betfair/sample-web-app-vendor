package com.betfair.vendor.domain;

/**
 * Token Types
 */
public enum TokenType {

    BEARER("BEARER"),
    UNRECOGNIZED_VALUE(null);

    private String message;

    TokenType(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
