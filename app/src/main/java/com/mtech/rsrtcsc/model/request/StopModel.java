package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class StopModel {
    private String stopName;

    public StopModel(String stopNamee) {
        this.stopName = stopNamee;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
