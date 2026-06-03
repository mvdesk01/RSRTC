package com.mtech.rsrtcsc.model.request;

import com.google.gson.annotations.SerializedName;

public class RechargeRequest {
    @SerializedName("TransactionDate")
    private String transactionDate;

    @SerializedName("RegistrationID")
    private String registrationID;

    @SerializedName("BaseFare")
    private String baseFare;

    @SerializedName("AcAmount")
    private String acAmount;

    @SerializedName("TollAmount")
    private String tollAmount;

    @SerializedName("OctAmount")
    private String octAmount;

    @SerializedName("HRAmount")
    private String hrAmount;

    @SerializedName("ITAmount")
    private String itAmount;

    @SerializedName("TotalAmount")
    private String totalAmount;
    @SerializedName("ExpireDate")
    private String ExpireDate;
    @SerializedName("CardAutoID")
    private String cardAutoID;
    // Constructor, Getters and Setters

    @SerializedName("PeriodRange")
    private String periodRange;
    public RechargeRequest() {
    }

    public RechargeRequest(String transactionDate, String registrationID, String baseFare,
                           String acAmount, String tollAmount, String octAmount,
                           String hrAmount, String itAmount, String totalAmount) {
        this.transactionDate = transactionDate;
        this.registrationID = registrationID;
        this.baseFare = baseFare;
        this.acAmount = acAmount;
        this.tollAmount = tollAmount;
        this.octAmount = octAmount;
        this.hrAmount = hrAmount;
        this.itAmount = itAmount;
        this.totalAmount = totalAmount;
    }

    public String getPeriodRange() {
        return periodRange;
    }

    public void setPeriodRange(String periodRange) {
        this.periodRange = periodRange;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getCardAutoID() {
        return cardAutoID;
    }

    public void setCardAutoID(String cardAutoID) {
        this.cardAutoID = cardAutoID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getAcAmount() {
        return acAmount;
    }

    public void setAcAmount(String acAmount) {
        this.acAmount = acAmount;
    }

    public String getTollAmount() {
        return tollAmount;
    }

    public void setTollAmount(String tollAmount) {
        this.tollAmount = tollAmount;
    }

    public String getOctAmount() {
        return octAmount;
    }

    public void setOctAmount(String octAmount) {
        this.octAmount = octAmount;
    }

    public String getHrAmount() {
        return hrAmount;
    }

    public void setHrAmount(String hrAmount) {
        this.hrAmount = hrAmount;
    }

    public String getItAmount() {
        return itAmount;
    }

    public void setItAmount(String itAmount) {
        this.itAmount = itAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}

