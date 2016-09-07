package com.betfair.vendor.domain;

/**
 * An Authorisation Code
 */
public class AuthorisationCode {

    private String code;

    public AuthorisationCode() {
    }

    public AuthorisationCode(String authCode) {
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
        return "AuthorisationCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
