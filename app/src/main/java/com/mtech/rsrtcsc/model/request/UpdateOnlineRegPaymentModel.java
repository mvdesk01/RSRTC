package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class UpdateOnlineRegPaymentModel {
    private String ApplicantId;
    private  String TransactionId;
    private String msg;

    public UpdateOnlineRegPaymentModel(String appId, String transId) {
        ApplicantId = transId;
        TransactionId = appId;
    }

    public String getApplicantId() {
        return ApplicantId;
    }

    public void setApplicantId(String applicantId) {
        ApplicantId = applicantId;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }
}
