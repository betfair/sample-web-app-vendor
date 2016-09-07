package com.betfair.vendor.domain;


public class VendorAccessTokenInfoContainer extends ResponseContainer {
    private String id;

    private VendorAccessTokenInfo result;

    public VendorAccessTokenInfo getResult() {
        return result;
    }

    public void setResult(VendorAccessTokenInfo result) {
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
        return "VendorAccessTokenInfoContainer{" +
                ", id='" + id + '\'' +
                ", result=" + result +
                '}';
    }
}
