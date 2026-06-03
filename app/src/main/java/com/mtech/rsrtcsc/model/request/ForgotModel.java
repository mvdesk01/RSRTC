package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class ForgotModel {
    private String MobileNo;

    public ForgotModel(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
