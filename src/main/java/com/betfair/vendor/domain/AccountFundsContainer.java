package com.betfair.vendor.domain;

/**
 * Created by MezeretN on 29/07/2016.
 */
public class AccountFundsContainer {

    private String id;

    private AccountFunds result;

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

    @Override
    public String toString() {
        return "AccountFundsContainer{" +
                ", id='" + id + '\'' +
                ", result=" + result +
                '}';
    }
}

