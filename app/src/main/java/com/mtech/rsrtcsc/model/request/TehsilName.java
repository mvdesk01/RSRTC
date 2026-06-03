package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class TehsilName {

    private String DistrictCode ;

    public TehsilName (String districtCode){
        this.DistrictCode = districtCode;

    }

    public String getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(String districtCode) {
        DistrictCode = districtCode;
    }
}
