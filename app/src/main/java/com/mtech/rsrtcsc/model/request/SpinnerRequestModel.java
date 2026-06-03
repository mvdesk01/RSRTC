package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class SpinnerRequestModel {
    private String ConcessionCode;

    public SpinnerRequestModel(String concessionCode) {
        ConcessionCode = concessionCode;
    }

    public String getConcessionCode() {
        return ConcessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        ConcessionCode = concessionCode;
    }
}
