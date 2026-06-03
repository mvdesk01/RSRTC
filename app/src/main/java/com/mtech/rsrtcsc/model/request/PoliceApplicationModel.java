package com.mtech.rsrtcsc.model.request;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
public class PoliceApplicationModel  {
    @SerializedName("DEPOID")
    @Expose
    private String DEPOID;
    @SerializedName("POINTOFSALEID")
    @Expose
    private String POINTOFSALEID;
    @SerializedName("ApplicantID")
    @Expose
    private String ApplicantID;
    @SerializedName("Title")
    @Expose
    private String Title;
    @SerializedName("FName")
    @Expose
    private String FName;
    @SerializedName("MName")
    @Expose
    private String MName;
    @SerializedName("LName")
    @Expose
    private String LName;
    @SerializedName("Gender")
    @Expose
    private String Gender;
    @SerializedName("DOB")
    @Expose
    private String DOB;
    @SerializedName("MobileNo")
    @Expose
    private String MobileNo;
    @SerializedName("EmailID")
    @Expose
    private String EmailID;
    @SerializedName("PhoneNo")
    @Expose
    private String PhoneNo;
    @SerializedName("Address")
    @Expose
    private String Address;
    @SerializedName("ProofID")
    @Expose
    private String ProofID;
    @SerializedName("ProofDetails")
    @Expose
    private String ProofDetails;
    @SerializedName("Photo")
    @Expose
    private String Photo;
    @SerializedName("PhotoIDProofID")
    @Expose
    private String PhotoIDProofID;
    @SerializedName("photoIDProofData")
    @Expose
    private String PhotoIDProofData;
    @SerializedName("ConcessionApplicableDocumentProofID")
    @Expose
    private String ConcessionApplicableDocumentProofID;


    @SerializedName("ConcessionApplicableDocumentProofFileData")
    @Expose
    private String ConcessionApplicableDocumentProofFileData;
    @SerializedName("AddressProofID")
    @Expose
    private String AddressProofID;
    @SerializedName("AddressProofFileData")
    @Expose
    private String AddressProofFileData;
    @SerializedName("Remarks")
    @Expose
    private String Remarks;
    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;
//    @SerializedName("CreatedOn")
//    @Expose
//    private String createdOn;
    @SerializedName("PassType")
    @Expose
    private String PassType;
    @SerializedName("PassHolder")
    @Expose
    private String PassHolder;
    @SerializedName("RFIDPassType")
    @Expose
    private String RFIDPassType;
    @SerializedName("PassValidity")
    @Expose
    private String PassValidity;
    @SerializedName("FromStop")
    @Expose
    private String FromStop;
    @SerializedName("FromStopVal")
    @Expose
    private String FromStopVal;
    @SerializedName("TillStop")
    @Expose
    private String TillStop;
    @SerializedName("TillStopVal")
    @Expose
    private String TillStopVal;
    @SerializedName("BusType")
    @Expose
    private String BusType;
    @SerializedName("RouteNo")
    @Expose
    private String RouteNo;
    @SerializedName("RouteName")
    @Expose
    private String RouteName;
    @SerializedName("KM")
    @Expose
    private String KM;
    @SerializedName("Depot")
    @Expose
    private String Depot;
    @SerializedName("TotalFare")
    @Expose
    private String TotalFare;
    @SerializedName("AdminFees")
    @Expose
    private String AdminFees;
    @SerializedName("CardFees")
    @Expose
    private String CardFees;
    @SerializedName("TransactionDate")
    @Expose
    private String TransactionDate;
    @SerializedName("ExpiryDate")
    @Expose
    private String ExpiryDate;
    @SerializedName("BaseFare")
    @Expose
    private String BaseFare;
    @SerializedName("Acc")
    @Expose
    private String Acc;
    @SerializedName("HR")
    @Expose
    private String HR;
    @SerializedName("Octroi")
    @Expose
    private String Octroi;
    @SerializedName("IT")
    @Expose
    private String IT;
    @SerializedName("Toll")
    @Expose
    private String Toll;
    @SerializedName("ConcessionType")
    @Expose
    private String ConcessionType;
    @SerializedName("ConcessionCode")
    @Expose
    private String ConcessionCode;
    @SerializedName("PassPeriod")
    @Expose
    private String PassPeriod;
    @SerializedName("StartDate")
    @Expose
    private String StartDate;
    @SerializedName("OPFlag")
    @Expose
    private String OPFlag;
    @SerializedName("HexPhoto")
    @Expose
    private String HexPhoto;
//    @SerializedName("OutPara")
//    @Expose
//    private String outPara;
    @SerializedName("AadharNo")
    @Expose
    private String AadharNo;
    @SerializedName("FatherName")
    @Expose
    private String FatherName;

    @SerializedName("PhotoIDNo")
    @Expose
    private String PhotoIDNo;
    @SerializedName("ConAppDocProofNo")
    @Expose
    private String ConAppDocProofNo;
    @SerializedName("AddressProofNo")
    @Expose
    private String AddressProofNo;
    @SerializedName("PoliceEmployeeID")
    @Expose
    private String PoliceEmployeeID ;

    @SerializedName("PolicePost")
    @Expose
    private String PolicePost ;

    @SerializedName("PoliceLocation")
    @Expose
    private String PoliceLocation;

    public PoliceApplicationModel(String depoid, String pointofsaleid, String applicantID, String title, String first_name, String middle_name, String last_name, String gender, String dob, String mobileNo, String emailID, String phoneNo, String address, String proofID, String proofDetails, String photo, String photoIDProofID, String photoIDProofData, String concessionApplicableDocumentProofID, String concessionApplicableDocumentProofFileData, String addressProofID, String addressProofFileData, String remarks, String createdBy,  String passType, String passHolder, String rFIDPassType, String passValidity, String fromStop, String fromStopVal, String tillStop, String tillStopVal, String busType, String routeNo, String routeName, String km, String depot, String totalFare, String adminFees, String cardFees, String transactionDate, String expiryDate, String baseFare, String acc, String hr, String octroi, String it, String toll, String concessionType, String concessionCode, String passPeriod, String startDate, String oPFlag, String hexPhoto, String aadharNo, String fathername, String photoIdno, String conAppDocProofNo, String addressProofNo, String employeeId, String post, String postingLocation) {
        this.DEPOID = depoid;
        this.POINTOFSALEID = pointofsaleid;
        this.ApplicantID = applicantID;
        this.Title = title;
        this.FName = first_name;
        this.MName = middle_name;
        this.LName = last_name;
        this.Gender = gender;
        this.DOB = dob;
        this.MobileNo = mobileNo;
        this.EmailID = emailID;
        this.PhoneNo = phoneNo;
        this.Address = address;
        this.ProofID = proofID;
        this.ProofDetails = proofDetails;
        this.Photo = photo;
        this.PhotoIDProofID = photoIDProofID;
        this.PhotoIDProofData = photoIDProofData;
        this.ConcessionApplicableDocumentProofID = concessionApplicableDocumentProofID;
        this.ConcessionApplicableDocumentProofFileData = concessionApplicableDocumentProofFileData;
        this.AddressProofID = addressProofID;
        this.AddressProofFileData = addressProofFileData;
        this.Remarks = remarks;
        this.CreatedBy = createdBy;
//        this.createdOn = createdOn;
        this.PassType = passType;
        this.PassHolder = passHolder;
        this.RFIDPassType = rFIDPassType;
        this.PassValidity = passValidity;
        this.FromStop = fromStop;
        this.FromStopVal = fromStopVal;
        this.TillStop = tillStop;
        this.TillStopVal = tillStopVal;
        this.BusType = busType;
        this.RouteNo = routeNo;
        this.RouteName = routeName;
        this.KM = km;
        this.Depot = depot;
        this.TotalFare = totalFare;
        this.AdminFees = adminFees;
        this.CardFees = cardFees;
        this.TransactionDate = transactionDate;
        this.ExpiryDate = expiryDate;
        this.BaseFare = baseFare;
        this.Acc = acc;
        this.HR = hr;
        this.Octroi = octroi;
        this.IT = it;
        this.Toll = toll;
        this.ConcessionType = concessionType;
        this.ConcessionCode = concessionCode;
        this.PassPeriod = passPeriod;
        this.StartDate = startDate;
        this.OPFlag = oPFlag;
        this.HexPhoto = hexPhoto;
//        this.outPara = outPara;
        this.AadharNo = aadharNo;
        this.FatherName = fathername;
        this.PhotoIDNo = photoIdno;
        this.ConAppDocProofNo = conAppDocProofNo;
        this.AddressProofNo = addressProofNo;
        this.PoliceEmployeeID = employeeId;
        this.PolicePost = post;
        this.PoliceLocation = postingLocation;

    }

    public PoliceApplicationModel(){

    }

    public String getDepoid() {
        return DEPOID;
    }

    public void setDepoid(String depoid) {
        this.DEPOID = depoid;
    }

    public String getPointofsaleid() {
        return POINTOFSALEID;
    }

    public void setPointofsaleid(String pointofsaleid) {
        this.POINTOFSALEID = pointofsaleid;
    }

    public String getApplicantID() {
        return ApplicantID;
    }

    public void setApplicantID(String applicantID) {
        this.ApplicantID = applicantID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getFirst_name() {
        return FName;
    }

    public void setFirst_name(String first_name) {
        this.FName = first_name;
    }

    public String getMiddle_name() {
        return MName;
    }

    public void setMiddle_name(String middle_name) {
        this.MName = middle_name;
    }



    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getDob() {
        return DOB;
    }

    public void setDob(String dob) {
        this.DOB = dob;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.MobileNo = mobileNo;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        this.EmailID = emailID;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.PhoneNo = phoneNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getProofID() {
        return ProofID;
    }

    public void setProofID(String proofID) {
        this.ProofID = proofID;
    }

    public String getProofDetails() {
        return ProofDetails;
    }

    public void setProofDetails(String proofDetails) {
        this.ProofDetails = proofDetails;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }

    public String getPhotoIDProofID() {
        return PhotoIDProofID;
    }

    public void setPhotoIDProofID(String photoIDProofID) {
        this.PhotoIDProofID = photoIDProofID;
    }

    public String getPhotoIDProofData() {
        return PhotoIDProofData;
    }

    public void setPhotoIDProofData(String photoIDProofData) {
        this.PhotoIDProofData = photoIDProofData;
    }

    public String getConcessionApplicableDocumentProofID() {
        return ConcessionApplicableDocumentProofID;
    }

    public void setConcessionApplicableDocumentProofID(String concessionApplicableDocumentProofID) {
        this.ConcessionApplicableDocumentProofID = concessionApplicableDocumentProofID;
    }

    public String getConcessionApplicableDocumentProofFileData() {
        return ConcessionApplicableDocumentProofFileData;
    }

    public void setConcessionApplicableDocumentProofFileData(String concessionApplicableDocumentProofFileData) {
        this.ConcessionApplicableDocumentProofFileData = concessionApplicableDocumentProofFileData;
    }

    public String getAddressProofID() {
        return AddressProofID;
    }

    public void setAddressProofID(String addressProofID) {
        this.AddressProofID = addressProofID;
    }

    public String getAddressProofFileData() {
        return AddressProofFileData;
    }

    public void setAddressProofFileData(String addressProofFileData) {
        this.AddressProofFileData = addressProofFileData;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        this.Remarks = remarks;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = createdBy;
    }

//    public String getCreatedOn() {
//        return createdOn;
//    }
//
//    public void setCreatedOn(String createdOn) {
//        this.createdOn = createdOn;
//    }

    public String getPassType() {
        return PassType;
    }

    public void setPassType(String passType) {
        this.PassType = passType;
    }

    public String getPassHolder() {
        return PassHolder;
    }

    public void setPassHolder(String passHolder) {
        this.PassHolder = passHolder;
    }

    public String getrFIDPassType() {
        return RFIDPassType;
    }

    public void setrFIDPassType(String rFIDPassType) {
        this.RFIDPassType = rFIDPassType;
    }

    public String getPassValidity() {
        return PassValidity;
    }

    public void setPassValidity(String passValidity) {
        this.PassValidity = passValidity;
    }

    public String getFromStop() {
        return FromStop;
    }

    public void setFromStop(String fromStop) {
        this.FromStop = fromStop;
    }

    public String getFromStopVal() {
        return FromStopVal;
    }

    public void setFromStopVal(String fromStopVal) {
        this.FromStopVal = fromStopVal;
    }

    public String getTillStop() {
        return TillStop;
    }

    public void setTillStop(String tillStop) {
        this.TillStop = tillStop;
    }

    public String getTillStopVal() {
        return TillStopVal;
    }

    public void setTillStopVal(String tillStopVal) {
        this.TillStopVal = tillStopVal;
    }

    public String getBusType() {
        return BusType;
    }

    public void setBusType(String busType) {
        this.BusType = busType;
    }

    public String getRouteNo() {
        return RouteNo;
    }

    public void setRouteNo(String routeNo) {
        this.RouteNo = routeNo;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        this.RouteName = routeName;
    }

    public String getKm() {
        return KM;
    }

    public void setKm(String km) {
        this.KM = km;
    }

    public String getDepot() {
        return Depot;
    }

    public void setDepot(String depot) {
        this.Depot = depot;
    }

    public String getTotalFare() {
        return TotalFare;
    }

    public void setTotalFare(String totalFare) {
        this.TotalFare = totalFare;
    }

    public String getAdminFees() {
        return AdminFees;
    }

    public void setAdminFees(String adminFees) {
        this.AdminFees = adminFees;
    }

    public String getCardFees() {
        return CardFees;
    }

    public void setCardFees(String cardFees) {
        this.CardFees = cardFees;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.TransactionDate = transactionDate;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.ExpiryDate = expiryDate;
    }

    public String getBaseFare() {
        return BaseFare;
    }

    public void setBaseFare(String baseFare) {
        this.BaseFare = baseFare;
    }

    public String getAcc() {
        return Acc;
    }

    public void setAcc(String acc) {
        this.Acc = acc;
    }

    public String getHr() {
        return HR;
    }

    public void setHr(String hr) {
        this.HR = hr;
    }

    public String getOctroi() {
        return Octroi;
    }

    public void setOctroi(String octroi) {
        this.Octroi = octroi;
    }

    public String getIt() {
        return IT;
    }

    public void setIt(String it) {
        this.IT = it;
    }

    public String getToll() {
        return Toll;
    }

    public void setToll(String toll) {
        this.Toll = toll;
    }

    public String getConcessionType() {
        return ConcessionType;
    }

    public void setConcessionType(String concessionType) {
        this.ConcessionType = concessionType;
    }

    public String getConcessionCode() {
        return ConcessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        this.ConcessionCode = concessionCode;
    }

    public String getPassPeriod() {
        return PassPeriod;
    }

    public void setPassPeriod(String passPeriod) {
        this.PassPeriod = passPeriod;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }

    public String getoPFlag() {
        return OPFlag;
    }

    public void setoPFlag(String oPFlag) {
        this.OPFlag = oPFlag;
    }

    public String getHexPhoto() {
        return HexPhoto;
    }

    public void setHexPhoto(String hexPhoto) {
        this.HexPhoto = hexPhoto;
    }

//    public String getOutPara() {
//        return outPara;
//    }
//
//    public void setOutPara(String outPara) {
//        this.outPara = outPara;
//    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.AadharNo = aadharNo;
    }

    public String getFathername() {
        return FatherName;
    }

    public void setFathername(String fathername) {
        this.FatherName = fathername;
    }

    public String getPhotoIdno() {
        return PhotoIDNo;
    }

    public void setPhotoIdno(String photoIdno) {
        this.PhotoIDNo = photoIdno;
    }

    public String getConAppDocProofNo() {
        return ConAppDocProofNo;
    }

    public void setConAppDocProofNo(String conAppDocProofNo) {
        this.ConAppDocProofNo = conAppDocProofNo;
    }

    public String getAddressProofNo() {
        return AddressProofNo;
    }

    public void setAddressProofNo(String addressProofNo) {
        this.AddressProofNo = addressProofNo;
    }

    public String getPoliceEmployeeID() {
        return PoliceEmployeeID;
    }

    public void setPoliceEmployeeID(String policeEmployeeID) {
        PoliceEmployeeID = policeEmployeeID;
    }

    public String getPolicePost() {
        return PolicePost;
    }

    public void setPolicePost(String policePost) {
        PolicePost = policePost;
    }

    public String getPoliceLocation() {
        return PoliceLocation;
    }

    public void setPoliceLocation(String policeLocation) {
        this.PoliceLocation = policeLocation;
    }
}
