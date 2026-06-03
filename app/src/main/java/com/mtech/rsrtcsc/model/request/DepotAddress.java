package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class DepotAddress {
    private String DepotCode;

    public String getDepotCode() {
        return DepotCode;
    }

    public void setDepotCode(String depotCode) {
        DepotCode = depotCode;
    }

    public DepotAddress (String depotaddress){
        DepotCode = depotaddress;
    }
}
