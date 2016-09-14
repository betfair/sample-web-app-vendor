package com.betfair.vendor.domain;

/**
 * An Authorization Code
 */
public class AuthorizationCode {

    private String code;

    public AuthorizationCode() {
    }

    public AuthorizationCode(String authCode) {
        this.code = authCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "AuthorizationCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
