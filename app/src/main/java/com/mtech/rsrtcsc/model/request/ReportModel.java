package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class ReportModel {
    private String UserId;

    public ReportModel(String UserId) {
        this.UserId=UserId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
