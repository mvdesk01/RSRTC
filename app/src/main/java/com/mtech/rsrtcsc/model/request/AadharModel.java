package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class AadharModel {
    private String AadharNo;

    public AadharModel(String aadharNo){
        AadharNo = aadharNo;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }
}
