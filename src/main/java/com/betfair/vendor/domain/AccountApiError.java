package com.betfair.vendor.domain;

/**
 * An error returned from the Account API
 */
public class AccountApiError {

    private String code;
    private String message;
    private ErrorData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorData getData() {
        return data;
    }

    public void setData(ErrorData data) {
        this.data = data;
    }
}
