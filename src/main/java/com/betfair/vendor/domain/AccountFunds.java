package com.betfair.vendor.domain;

/**
 * Created by MezeretN on 29/07/2016.
 */
public class AccountFunds {
    private String availableToBetBalance;
    private double exposure;
    private double retainedCommission;
    private double exposureLimit;
    private double discountRate;
    private int pointsBalance;
    private String wallet;

    public String getAvailableToBetBalance() {
        if (availableToBetBalance != null) {
            return availableToBetBalance;
        } else {
            return "";
        }
    }

    public void setAvailableToBetBalance(String availableToBetBalance) {
        this.availableToBetBalance = availableToBetBalance;
    }

    public double getExposure() {
        return exposure;
    }

    public void setExposure(double exposure) {
        this.exposure = exposure;
    }

    public double getRetainedCommission() {
        return retainedCommission;
    }

    public void setRetainedCommission(double retainedCommission) {
        this.retainedCommission = retainedCommission;
    }

    public double getExposureLimit() {
        return exposureLimit;
    }

    public void setExposureLimit(double exposureLimit) {
        this.exposureLimit = exposureLimit;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getPointsBalance() {
        return pointsBalance;
    }

    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }
}
