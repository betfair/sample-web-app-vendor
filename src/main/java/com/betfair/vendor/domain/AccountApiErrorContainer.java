package com.betfair.vendor.domain;

/**
 * A container for an error returned from the Account API
 */
public class AccountApiErrorContainer extends ResponseContainer {

    private String id;
    private AccountApiError error;

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
                ", error=" + error +
                '}';
    }
}
