package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class ConcessionCodeModel {
    private String ConcessionType;
    private String BusType;


    public ConcessionCodeModel(String concessionType) {
        ConcessionType = concessionType;
        this.BusType="EXP";
    }

    public String getConcessionType() {
        return ConcessionType;
    }

    public void setConcessionType(String concessionType) {
        ConcessionType = concessionType;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        this.BusType = busType;
    }
}
