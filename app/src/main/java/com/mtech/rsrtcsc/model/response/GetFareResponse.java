package com.mtech.rsrtcsc.model.response;

public class GetFareResponse {

        private String intrastate;
        private String interstate;
        private String acc;
        private String hr;
        private String octroi;
        private String it;
        private String toll;


        // Getter Methods

        public String getIntrastate() {
            return intrastate;
        }

        public String getInterstate() {
            return interstate;
        }

        public String getAcc() {
            return acc;
        }

        public String getHr() {
            return hr;
        }

        public String getOctroi() {
            return octroi;
        }

        public String getIt() {
            return it;
        }

        public String getToll() {
            return toll;
        }

        // Setter Methods

        public void setIntrastate( String intrastate ) {
            this.intrastate = intrastate;
        }

        public void setInterstate( String interstate ) {
            this.interstate = interstate;
        }

        public void setAcc( String acc ) {
            this.acc = acc;
        }

        public void setHr( String hr ) {
            this.hr = hr;
        }

        public void setOctroi( String octroi ) {
            this.octroi = octroi;
        }

        public void setIt( String it ) {
            this.it = it;
        }

        public void setToll( String toll ) {
            this.toll = toll;
        }


}
