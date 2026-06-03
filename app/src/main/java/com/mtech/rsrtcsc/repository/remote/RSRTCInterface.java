package com.mtech.rsrtcsc.repository.remote;

import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.BILLDESK_PAYLOAD;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.BILL_DESK_REQUEST;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.BILL_DESK_REQUEST_PAYMENT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.BILL_DESK_RESPONSE;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.BILL_DESK_RESPONSE_PAYMENT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.CARD_DATA;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.DEPOT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.DOCUMENT_CODE;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.DOCUMENT_TYPE;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.FORGOT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_AADHAR_CHECK;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_CARD_STATUS_APP;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_CONCESSION;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_CONCESSION_MASTER;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_CONCESSION_NAME;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_DEPOT_ADDRESS;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_DISTRICT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_FARE;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_MOBILE_NO_CHECK;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_OTP;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_POLICE_CONCESSION;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_POLICE_POST;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_POLICE_POSTING_LOCATION;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_POLICE_REGISTRATION;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_SPECIFIC_CONVESSION_TYPE_MASTER;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_TEHSIL;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.GET_VIRTUAL_CARD;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.LOGIN;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.ONLINE_RECHARGE;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.PROOF;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.ROUTE_DETAIL;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.SAVE_RECHARGE_DETAILS;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.SAVE_REGISTRATION;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.SEND_SMS;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.SIGNUP;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.STOP_NAME;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.UPDATE_ONLINE_REG_PAYMENT;
import static com.mtech.rsrtcsc.repository.remote.RSRTCConstants.VERIFY_OTP;

import com.mtech.rsrtcsc.model.request.AadharModel;
import com.mtech.rsrtcsc.model.request.ApplicationModel;
import com.mtech.rsrtcsc.model.request.BillDeskRequestModel;
import com.mtech.rsrtcsc.model.request.BillDeskRequestPayment;
import com.mtech.rsrtcsc.model.request.BillDeskResponseModel;
import com.mtech.rsrtcsc.model.request.BilldeskRequestPayloadModel;
import com.mtech.rsrtcsc.model.request.CardDataModel;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.ConcessionCodeModel;
import com.mtech.rsrtcsc.model.request.ConcessionName;
import com.mtech.rsrtcsc.model.request.ConcessionrateRequest;
import com.mtech.rsrtcsc.model.request.DepotAddress;
import com.mtech.rsrtcsc.model.request.Districtname;
import com.mtech.rsrtcsc.model.request.ForgotModel;
import com.mtech.rsrtcsc.model.request.GetFareRequest;
import com.mtech.rsrtcsc.model.request.GetOtpModel;
import com.mtech.rsrtcsc.model.request.LoginModel;
import com.mtech.rsrtcsc.model.request.PoliceApplicationModel;
import com.mtech.rsrtcsc.model.request.PoliceConcessionModel;
import com.mtech.rsrtcsc.model.request.RechargeRequest;
import com.mtech.rsrtcsc.model.request.ReportModel;
import com.mtech.rsrtcsc.model.request.RouteModel;
import com.mtech.rsrtcsc.model.request.SMSModel;
import com.mtech.rsrtcsc.model.request.SignupModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.request.SpinnerRequestModel;
import com.mtech.rsrtcsc.model.request.StopModel;
import com.mtech.rsrtcsc.model.request.TehsilName;
import com.mtech.rsrtcsc.model.request.UpdateOnlineRegPaymentModel;
import com.mtech.rsrtcsc.model.response.AuthModel;
import com.mtech.rsrtcsc.model.response.AuthModelVerifyOTP;
import com.mtech.rsrtcsc.model.response.BillDeskModel;
import com.mtech.rsrtcsc.model.response.CardData;
import com.mtech.rsrtcsc.model.response.CardModel;
import com.mtech.rsrtcsc.model.response.ConcessionRateResponse;
import com.mtech.rsrtcsc.model.response.GetFareResponse;
import com.mtech.rsrtcsc.model.response.RechargeApiResponse;
import com.mtech.rsrtcsc.model.response.RegistrationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RSRTCInterface {

    @POST(SIGNUP)
    Call<AuthModel> signup(@Body SignupModel model);

    @POST(LOGIN)
    Call<List<AuthModel>> login(@Body LoginModel model);

    @POST(GET_OTP)
    Call<AuthModel> GetOTP(@Body GetOtpModel model);

    @POST(SEND_SMS)
    Call<String> sendSMS(@Body SMSModel model);

    @POST(FORGOT)
    Call<List<AuthModel>> forgotPassword(@Body ForgotModel data);


    @POST(CARD_DATA)
    Call<List<CardData>>  getCardData(@Body CardDataModel model);


    @POST(ONLINE_RECHARGE)
    Call<List<CardModel>>  report(@Body ReportModel model);


    @POST(DEPOT)
    Call<List<SpinnerDataModel>> depotApi();

    @POST(GET_CONCESSION_NAME)
    Call<List<SpinnerDataModel>> GetAllConcessionMaster();

//    @POST(CONCESSION_TYPE)
//    Call<List<SpinnerDataModel>> getConcessionTypeMaster();

    @POST(STOP_NAME)
    Call<List<SpinnerDataModel>> stopName(@Body StopModel model);

    @POST(GET_CONCESSION_MASTER)
    Call<List<SpinnerDataModel>> getConcessionMaster(@Body ConcessionCodeModel model);

    @POST(ROUTE_DETAIL)
    Call<List<RouteModel>> getRouteDetail(@Body RouteModel model);

    @POST(SAVE_REGISTRATION)
    Call<List<RegistrationModel>> saveRegistration(@Body ApplicationModel model);

    @POST(GET_POLICE_REGISTRATION)
    Call<List<RegistrationModel>> SavePoliceRegistration(@Body PoliceApplicationModel model);

    @POST(BILL_DESK_REQUEST)
    Call<List<BillDeskModel>> billDeskRequest(@Body BillDeskRequestModel model);

    @POST(BILL_DESK_RESPONSE)
    Call<List<BillDeskModel>> billDeskResponse(@Body BillDeskResponseModel model);

    @POST(DOCUMENT_TYPE)
    Call<List<SpinnerDataModel>> getDocumentType(@Body SpinnerRequestModel model);


    @POST(DOCUMENT_CODE)
    Call<List<SpinnerDataModel>> getConcessionDoc(@Body SpinnerRequestModel model);

    @POST(BILLDESK_PAYLOAD)
    Call<List<BillDeskModel>> getCheckSum(@Body BilldeskRequestPayloadModel model);

    @POST(PROOF)
    Call<List<SpinnerDataModel>> getProofApi();

    @POST(GET_SPECIFIC_CONVESSION_TYPE_MASTER)
    Call<List<SpinnerDataModel>> GetSpecificConcessionTypeMaster(@Body ConcessionName model);

    @POST(BILL_DESK_REQUEST_PAYMENT)
    Call<List<BillDeskModel>>BillDeskRequest(@Body BillDeskRequestPayment model);

    @POST(UPDATE_ONLINE_REG_PAYMENT)
    Call<List<UpdateOnlineRegPaymentModel>>UpdateOnlineRegPayment(@Body UpdateOnlineRegPaymentModel model);

    @POST(BILL_DESK_RESPONSE_PAYMENT)
    Call<List<BillDeskModel>>BillDeskResponse(@Body BillDeskResponseModel model);

    @POST(GET_CARD_STATUS_APP)
    Call<List<SpinnerDataModel>> GetCardStatusAPP(@Body CardStatusModel model);

    @POST(GET_DEPOT_ADDRESS)
    Call<List<SpinnerDataModel>> GetDepotAddress (@Body DepotAddress model);

    @POST(GET_VIRTUAL_CARD)
    Call<List<SpinnerDataModel>> GetVirtualCard(@Body CardStatusModel model);

    @POST(GET_MOBILE_NO_CHECK)
    Call<List<SpinnerDataModel>>GetMobileNoCheck(@Body CardStatusModel model);

    @POST(GET_AADHAR_CHECK)
    Call<List<SpinnerDataModel>>GetAadharCheck(@Body AadharModel model);

    @POST(GET_POLICE_POST)
    Call<List<SpinnerDataModel>> getpolicepost();

    @POST(GET_POLICE_POSTING_LOCATION)
    Call<List<SpinnerDataModel>> getpolicepostinglocation();

    @POST(GET_POLICE_CONCESSION)
    Call<List<SpinnerDataModel>>GetPoliceConcession(@Body PoliceConcessionModel model);

    @POST(GET_DISTRICT)
    Call<List<SpinnerDataModel>>GetDistrict(@Body Districtname model);

    @POST(GET_TEHSIL)
    Call<List<SpinnerDataModel>>GetTehsil(@Body TehsilName model);


    @POST(GET_FARE)
    Call<GetFareResponse>Getfare(@Body GetFareRequest getFareRequest);

    @POST(GET_CONCESSION)
    Call<List<ConcessionRateResponse>>GetconcessionRate(@Body ConcessionrateRequest concessionrateRequest);

    @POST(SAVE_RECHARGE_DETAILS)
    Call<List<RechargeApiResponse>> saveRechargeDetails(@Body RechargeRequest request);

}
