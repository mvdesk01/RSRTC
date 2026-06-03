package com.mtech.rsrtcsc.model.response;

public class ConcessionRateResponse {

        private String concessionRate;
        private String busTypeCd;
        private String concessionCode;


        // Getter Methods

        public String getConcessionRate() {
            return concessionRate;
        }

        public String getBusTypeCd() {
            return busTypeCd;
        }

        public String getConcessionCode() {
            return concessionCode;
        }

        // Setter Methods

        public void setConcessionRate( String concessionRate ) {
            this.concessionRate = concessionRate;
        }

        public void setBusTypeCd( String busTypeCd ) {
            this.busTypeCd = busTypeCd;
        }

        public void setConcessionCode( String concessionCode ) {
            this.concessionCode = concessionCode;
        }

}
