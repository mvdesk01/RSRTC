package com.mtech.rsrtcsc.model.request;

public class ConcessionrateRequest {

        private String ConcessionCode;
        private String BusType;


        // Getter Methods

        public String getConcessionCode() {
            return ConcessionCode;
        }

        public String getBusType() {
            return BusType;
        }

        // Setter Methods

        public void setConcessionCode( String ConcessionCode ) {
            this.ConcessionCode = ConcessionCode;
        }

        public void setBusType( String BusType ) {
            this.BusType = BusType;
        }

}
