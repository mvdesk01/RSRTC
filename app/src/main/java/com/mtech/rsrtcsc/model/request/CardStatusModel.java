package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class CardStatusModel {
    private String MobileNo;

    public CardStatusModel(String applicantid){
        MobileNo = applicantid;
    }



    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
