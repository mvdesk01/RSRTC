package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class Districtname {

    private String DistrictName;

    public Districtname(String districtname){
        this.DistrictName = districtname;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }
}
