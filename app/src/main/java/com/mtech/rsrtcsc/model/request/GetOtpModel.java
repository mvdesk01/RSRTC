package com.mtech.rsrtcsc.model.request;

public class GetOtpModel {
    private String EmailId;
    private String MobileNo;

    public GetOtpModel(String emailId, String mobileNo) {
        EmailId = emailId;
        MobileNo = mobileNo;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
