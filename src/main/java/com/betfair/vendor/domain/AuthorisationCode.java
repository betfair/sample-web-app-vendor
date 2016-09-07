package com.betfair.vendor.domain;

/**
 * Created by MezeretN on 28/07/2016.
 */
public class AuthorisationCode {
    public String getCode() {
        return code;
    }

    public AuthorisationCode(String authCode) {
        this.code = authCode;
    }

    public AuthorisationCode() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String code;

    @Override
    public String toString() {
        return "AuthorisationCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
