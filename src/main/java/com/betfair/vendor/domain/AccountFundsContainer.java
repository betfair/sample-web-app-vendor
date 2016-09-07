package com.betfair.vendor.domain;

/**
 * A container for Account Funds details returned from the getAccountFunds operation
 */
public class AccountFundsContainer {

    private String id;
    private AccountFunds result;
    private AccountApiError error;

    public AccountFunds getResult() {
        return result;
    }

    public void setResult(AccountFunds result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountApiError getError() {
        return error;
    }

    public void setError(AccountApiError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AccountFundsContainer{" +
                ", id='" + id + '\'' +
                ", result=" + result +
                ", error=" + error +
                '}';
    }
}
