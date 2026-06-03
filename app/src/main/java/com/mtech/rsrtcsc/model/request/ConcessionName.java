package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class ConcessionName {

    private String ConcessionCode;

    public ConcessionName(String concessioncode) {
        ConcessionCode=concessioncode;

    }

    public String getConcessionCode() {
        return ConcessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        ConcessionCode = concessionCode;
    }

}
