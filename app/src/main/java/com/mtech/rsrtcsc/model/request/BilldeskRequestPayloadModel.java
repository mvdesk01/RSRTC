package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class BilldeskRequestPayloadModel {
    private String msg;

    public BilldeskRequestPayloadModel(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
