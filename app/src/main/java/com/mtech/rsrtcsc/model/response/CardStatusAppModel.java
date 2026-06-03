package com.mtech.rsrtcsc.model.response;

import androidx.annotation.Keep;

@Keep
public class CardStatusAppModel {
    private String applicantID;
    private String cName;
    private String reg_Date;
    private String mobileNo;
    private String paymentStatus;
    private String transactionId;
    private String application_Status;
    private String depot;
    private String card_Status;
    private String card_Auto_ID;
    private String cardFees;
    private String fromStop;
    private String tillStop;
    private String rechargeAmt;
    private String rechargeDate;
    private String contactAddr;


    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getReg_Date() {
        return reg_Date;
    }

    public void setReg_Date(String reg_Date) {
        this.reg_Date = reg_Date;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplication_Status() {
        return application_Status;
    }

    public void setApplication_Status(String application_Status) {
        this.application_Status = application_Status;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getCard_Status() {
        return card_Status;
    }

    public void setCard_Status(String card_Status) {
        this.card_Status = card_Status;
    }

    public String getCard_Auto_ID() {
        return card_Auto_ID;
    }

    public void setCard_Auto_ID(String card_Auto_ID) {
        this.card_Auto_ID = card_Auto_ID;
    }

    public String getCardFees() {
        return cardFees;
    }

    public void setCardFees(String cardFees) {
        this.cardFees = cardFees;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getTillStop() {
        return tillStop;
    }

    public void setTillStop(String tillStop) {
        this.tillStop = tillStop;
    }

    public String getRechargeAmt() {
        return rechargeAmt;
    }

    public void setRechargeAmt(String rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(String rechargeDate) {
        this.rechargeDate = rechargeDate;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }



}
