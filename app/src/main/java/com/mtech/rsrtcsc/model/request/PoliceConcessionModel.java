package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class PoliceConcessionModel {
    private String Location;

    public PoliceConcessionModel(String applicantid){
        Location = applicantid;
    }
}
