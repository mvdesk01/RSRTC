package com.mtech.rsrtcsc.model.response;

import com.google.gson.annotations.SerializedName;

public class RechargeApiResponse {
    @SerializedName("outMsg")
    private String outMessage;

    public String getOutMessage() {
        return outMessage;
    }

    public void setOutMessage(String outMessage) {
        this.outMessage = outMessage;
    }
}
