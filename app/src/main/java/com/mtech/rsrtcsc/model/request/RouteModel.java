package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class RouteModel extends com.mtech.rsrtcsc.model.response.RouteModel {
    private String fromStop;
     private String tillStop;
    private String busType;

    public RouteModel(String busType,String fromStop, String tillStop) {
        this.fromStop = fromStop;
        this.tillStop = tillStop;
        this.busType = busType;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getTillStop() {
        return tillStop;
    }

    public void setTillStop(String tillStop) {
        this.tillStop = tillStop;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }
}
