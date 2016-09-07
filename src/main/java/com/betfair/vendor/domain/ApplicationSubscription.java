package com.betfair.vendor.domain;

import java.util.Date;

/**
 * Application Subscription details returned from the token operation
 */
public class ApplicationSubscription {

    private String subscriptionToken;
    private Date expiryDateTime;
    private Date expiredDateTime;
    private Date createdDateTime;
    private Date activationDateTime;
    private Date cancellationDateTime;
    private String subscriptionStatus;
    private String clientReference;
    private String vendorClientId;

    public String getSubscriptionToken() {
        return subscriptionToken;
    }

    public void setSubscriptionToken(String subscriptionToken) {
        this.subscriptionToken = subscriptionToken;
    }

    public Date getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(Date expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public Date getExpiredDateTime() {
        return expiredDateTime;
    }

    public void setExpiredDateTime(Date expiredDateTime) {
        this.expiredDateTime = expiredDateTime;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getActivationDateTime() {
        return activationDateTime;
    }

    public void setActivationDateTime(Date activationDateTime) {
        this.activationDateTime = activationDateTime;
    }

    public Date getCancellationDateTime() {
        return cancellationDateTime;
    }

    public void setCancellationDateTime(Date cancellationDateTime) {
        this.cancellationDateTime = cancellationDateTime;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getVendorClientId() {
        return vendorClientId;
    }

    public void setVendorClientId(String vendorClientId) {
        this.vendorClientId = vendorClientId;
    }

    @Override
    public String toString() {
        return "ApplicationSubscription{" +
                "subscriptionToken='" + subscriptionToken + '\'' +
                ", expiryDateTime=" + expiryDateTime +
                ", expiredDateTime=" + expiredDateTime +
                ", createdDateTime=" + createdDateTime +
                ", activationDateTime=" + activationDateTime +
                ", cancellationDateTime=" + cancellationDateTime +
                ", subscriptionStatus='" + subscriptionStatus + '\'' +
                ", clientReference='" + clientReference + '\'' +
                ", vendorClientId='" + vendorClientId + '\'' +
                '}';
    }
}
