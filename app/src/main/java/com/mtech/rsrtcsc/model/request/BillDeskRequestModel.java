package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;

@Keep
public class BillDeskRequestModel {
    private String CardNo;
    private String UserId;
    private String Msg ;
    private String PayLoad;
    private String RequestType= PrefrenceKeyConstant.REQUEST_TYPE_BILLDESK;


    public BillDeskRequestModel(String payload) {
        Msg = payload;

    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }
}

