package com.betfair.vendor.domain;

/**
 * Error Data
 */
public class ErrorData {

    private String exceptionname;
    private AccountAPINGException AccountAPINGException;

    public String getExceptionname() {
        return exceptionname;
    }

    public void setExceptionname(String exceptionname) {
        this.exceptionname = exceptionname;
    }

    public AccountAPINGException getAccountAPINGException() {
        return AccountAPINGException;
    }

    public void setAccountAPINGException(AccountAPINGException AccountAPINGException) {
        this.AccountAPINGException = AccountAPINGException;
    }
}
