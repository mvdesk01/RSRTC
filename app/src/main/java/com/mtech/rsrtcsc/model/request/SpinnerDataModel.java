package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

@Keep
public class SpinnerDataModel {

    // Depot
    private String depotNum;
    private String depotName;
    private String postName;
    private String postinG_LOCATION_NAME;

    // Proof
    private String proofId;
    private String proofName;
    private String districtName;
    private String districtCode;
    private String tehsilName;

    public String getTehsilName() {
        return tehsilName;
    }

    public void setTehsilName(String tehsilName) {
        this.tehsilName = tehsilName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostinG_LOCATION_NAME() {
        return postinG_LOCATION_NAME;
    }

    public void setPostinG_LOCATION_NAME(String postinG_LOCATION_NAME) {
        this.postinG_LOCATION_NAME = postinG_LOCATION_NAME;
    }

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String concessionName;
    private String concessionCode;

    // Stop Name
    private String busStopName;
    private String busStopCode;

    // Document Upload
    private String srNo;
    private String documentName;

    private String rangePeriod;
    private String concessionTypeId;
    private String concessionTypeName;
    private String depotCode;

    private String applicantID;
    private String cName;
    private String reg_Date;
    private String mobileNo;
    private String paymentStatus;
    private String transactionId;
    private String application_Status;
    private String depot;
    private String card_Status;
    private String  card_Auto_ID;
    private String cardFees;
    public String fromStop;
    public String tillStop;
    private String rechargeAmt;
    private String rechargeDate;
    private String concession_Name;


    private String regDate;
    private String applicationStatus;
    private String policeEmployeeID;
    private String policePost;
    private String policeLocation;
    private String expiryDate;
    private String remarks;
    private String aadharNo;
    private String applicantPhoto;

    public String getRemarks() {
        return remarks;
    }

    public String getApplicantPhoto() {
        return applicantPhoto;
    }

    public void setApplicantPhoto(String applicantPhoto) {
        this.applicantPhoto = applicantPhoto;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getPoliceEmployeeID() {
        return policeEmployeeID;
    }

    public void setPoliceEmployeeID(String policeEmployeeID) {
        this.policeEmployeeID = policeEmployeeID;
    }

    public String getPolicePost() {
        return policePost;
    }

    public void setPolicePost(String policePost) {
        this.policePost = policePost;
    }

    public String getPoliceLocation() {
        return policeLocation;
    }

    public void setPoliceLocation(String policeLocation) {
        this.policeLocation = policeLocation;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardAutoID() {
        return cardAutoID;
    }

    public void setCardAutoID(String cardAutoID) {
        this.cardAutoID = cardAutoID;
    }

    private String cardAutoID;

    public String getConcession_Name() {
        return concession_Name;
    }

    public void setConcession_Name(String concession_Name) {
        this.concession_Name = concession_Name;
    }



    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getReg_Date() {
        return reg_Date;
    }

    public void setReg_Date(String reg_Date) {
        this.reg_Date = reg_Date;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getApplication_Status() {
        return application_Status;
    }

    public void setApplication_Status(String application_Status) {
        this.application_Status = application_Status;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getCard_Status() {
        return card_Status;
    }

    public void setCard_Status(String card_Status) {
        this.card_Status = card_Status;
    }

    public String getCard_Auto_ID() {
        return card_Auto_ID;
    }

    public void setCard_Auto_ID(String card_Auto_ID) {
        this.card_Auto_ID = card_Auto_ID;
    }

    public String getCardFees() {
        return cardFees;
    }

    public void setCardFees(String cardFees) {
        this.cardFees = cardFees;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStopn) {fromStop = fromStopn;
    }

    public String getTillStop() {
        return tillStop;
    }

    public void setTillStop(String tillStopn) {
        tillStop = tillStopn;
    }

    public String getRechargeAmt() {
        return rechargeAmt;
    }

    public void setRechargeAmt(String rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    public String getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(String rechargeDate) {
        this.rechargeDate = rechargeDate;
    }

    public String getContactAddr() {
        return contactAddr;
    }

    public void setContactAddr(String contactAddr) {
        this.contactAddr = contactAddr;
    }

    private String contactAddr;

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public SpinnerDataModel(String n, String c) {
        concessionName = n;
        concessionCode = c;

    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getDepotNum() {
        return depotNum;
    }

    public void setDepotNum(String depotNum) {
        this.depotNum = depotNum;
    }

    public String getProofId() {
        return proofId;
    }

    public void setProofId(String proofId) {
        this.proofId = proofId;
    }

    public String getConcessionName() {
        return concessionName;
    }

    public void setConcessionName(String concessionName) {
        this.concessionName = concessionName;
    }

    public String getConcessionCode() {
        return concessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        this.concessionCode = concessionCode;
    }

    public String getProofName() {
        return proofName;
    }

    public void setProofName(String proofName) {
        this.proofName = proofName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getConcessionTypeId() {
        return concessionTypeId;
    }

    public void setConcessionTypeId(String concessionTypeId) {
        this.concessionTypeId = concessionTypeId;
    }

    public String getConcessionTypeName() {
        return concessionTypeName;
    }

    public void setConcessionTypeName(String concessionTypeName) {
        this.concessionTypeName = concessionTypeName;
    }

    public String getRangePeriod() {
        return rangePeriod;
    }

    public void setRangePeriod(String rangePeriod) {
        this.rangePeriod = rangePeriod;
    }


}
