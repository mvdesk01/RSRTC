package com.mtech.rsrtcsc.repository.remote;

import java.io.Serializable;

public interface RSRTCConstants extends Serializable {
    String SIGNUP="MobileLoginRegistor";
    String LOGIN="MobileLogin";

    String GET_OTP = "GetOTP";
    String VERIFY_OTP = "VerifyOTP";
    String FORGOT="ForgetPass";
    String CARD_DATA="GetCardData";
    String ONLINE_RECHARGE="ReportOnlineRecharge";
    String DEPOT="GetDepotMaster";
    String PROOF="GetProofMaster";
    String CONCESSION_TYPE="GetConcessionTypeMaster";
    String SEND_SMS="SendSMSServiceNew";

    String STOP_NAME="stopName";
    String ROUTE_DETAIL="getRouteDetail";
    String SAVE_REGISTRATION="SaveRegistration";
    String GET_CONCESSION_MASTER="GetConcessionMaster";
    String BILL_DESK_REQUEST="BillDeskRequest";
    String BILL_DESK_RESPONSE="BillDeskResponse";
    String DOCUMENT_TYPE="GetDocumentType";
    String DOCUMENT_CODE="GetConcessionDoc";
    String BILLDESK_PAYLOAD="GetCheckSum";

    String GET_CONCESSION_NAME = "GetAllConcessionMaster";
    String GET_PASS_PERIOD = "GetPassPeriod";
    //String BILL_DESK_REQUEST_PAYMENT = "BillDeskRequestPayment";
    String BILL_DESK_REQUEST_PAYMENT = "BillDeskRequest";
    String UPDATE_ONLINE_REG_PAYMENT = "UpdateOnlineRegPayment";
    String BILL_DESK_RESPONSE_PAYMENT = "BillDeskResponsePayment";
    String GET_SPECIFIC_CONVESSION_TYPE_MASTER = "GetSpecificConcessionTypeMaster";

    String GET_CARD_STATUS_APP = "GetCardStatusAPP";
    String GET_DEPOT_ADDRESS = "GetDepotAddress";
    String GET_VIRTUAL_CARD = "GetVirtualCard";
    String GET_MOBILE_NO_CHECK = "GetMobileNoCheck";
    String GET_AADHAR_CHECK = "GetAadharCheck";

    String GET_POLICE_POST ="getpolicepost";
    String GET_POLICE_POSTING_LOCATION = "getpolicepostinglocation";
    String GET_POLICE_REGISTRATION = "SavePoliceRegistration";
    String GET_POLICE_CONCESSION="GetPoliceConcession";
    String GET_DISTRICT = "GetDistrict";
    String GET_TEHSIL = "GetTehsil";

    String GET_FARE="getFare";

    String GET_CONCESSION="GetConcession";
    String SAVE_RECHARGE_DETAILS="SaveRechargeDetails";


}
