package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class BillDeskResponseModel {
    private String Msg;
    private String TransId;
    private  String AppId;



    public BillDeskResponseModel(String msg) {
        Msg = msg;

    }



    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

}
