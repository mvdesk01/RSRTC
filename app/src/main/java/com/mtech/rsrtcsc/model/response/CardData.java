package com.mtech.rsrtcsc.model.response;

public class CardData {
        private String registrationID;

        private String emailID;
        private String cardNo;
        private String cardName;
        private String concessionName;
        private String fromStop;
        private String tillStop;
        private String busService;
        private String amount;

        private String depotName;

        private String rechargePeriod;
        private String routeNo;
        private String routeName;
        private String expiryDate;
    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getRechargePeriod() {
        return rechargePeriod;
    }

    public void setRechargePeriod(String rechargePeriod) {
        this.rechargePeriod = rechargePeriod;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(String rechargeDate) {
        this.rechargeDate = rechargeDate;
    }

    private  String rechargeDate;
        // Getter Methods

        public String getCardNo() {
            return cardNo;
        }

        public String getCardName() {
            return cardName;
        }

        public String getConcessionName() {
            return concessionName;
        }

        public String getFromStop() {
            return fromStop;
        }

        public String getTillStop() {
            return tillStop;
        }

        public String getBusService() {
            return busService;
        }

        public String getAmount() {
            return amount;
        }

        // Setter Methods

        public void setCardNo( String cardNo ) {
            this.cardNo = cardNo;
        }

        public void setCardName( String cardName ) {
            this.cardName = cardName;
        }

        public void setConcessionName( String concessionName ) {
            this.concessionName = concessionName;
        }

        public void setFromStop( String fromStop ) {
            this.fromStop = fromStop;
        }

        public void setTillStop( String tillStop ) {
            this.tillStop = tillStop;
        }

        public void setBusService( String busService ) {
            this.busService = busService;
        }

        public void setAmount( String amount ) {
            this.amount = amount;
        }

}
