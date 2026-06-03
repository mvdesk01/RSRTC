package com.mtech.rsrtcsc.model.response;

import androidx.annotation.Keep;

@Keep
public class RegistrationModel {
    private String outMsg;
    private String appId;

    public String getOutMsg() {
        return outMsg;
    }

    public void setOutMsg(String outMsg) {
        this.outMsg = outMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
