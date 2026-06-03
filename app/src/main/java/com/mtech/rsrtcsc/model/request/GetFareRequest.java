package com.mtech.rsrtcsc.model.request;

public class GetFareRequest
{

        private String fromStop;
        private String tillStop;
        private String routeNo;
        private String busType;


        // Getter Methods

        public String getFromStop() {
            return fromStop;
        }

        public String getTillStop() {
            return tillStop;
        }

        public String getRouteNo() {
            return routeNo;
        }

        public String getBusType() {
            return busType;
        }

        // Setter Methods

        public void setFromStop( String fromStop ) {
            this.fromStop = fromStop;
        }

        public void setTillStop( String tillStop ) {
            this.tillStop = tillStop;
        }

        public void setRouteNo( String routeNo ) {
            this.routeNo = routeNo;
        }

        public void setBusType( String busType ) {
            this.busType = busType;
        }

}
