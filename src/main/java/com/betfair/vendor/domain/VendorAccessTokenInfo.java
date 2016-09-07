package com.betfair.vendor.domain;

public class VendorAccessTokenInfo {

    private String access_token;
    private TokenType token_type;
    private Long expires_in;
    private String refresh_token;
    private ApplicationSubscription application_subscription;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public TokenType getToken_type() {
        return token_type;
    }

    public void setToken_type(TokenType token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public ApplicationSubscription getApplication_subscription() {
        return application_subscription;
    }

    public void setApplication_subscription(ApplicationSubscription application_subscription) {
        this.application_subscription = application_subscription;
    }

    @Override
    public String toString() {
        return "VendorAccessTokenInfo{" +
                "access_token='" + access_token + '\'' +
                ", token_type=" + token_type +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", application_subscription=" + application_subscription +
                '}';
    }
}
