package com.mtech.rsrtcsc.ui.activity.billdesk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityStatusBinding;
import com.mtech.rsrtcsc.model.request.BillDeskResponseModel;
import com.mtech.rsrtcsc.model.request.RechargeRequest;
import com.mtech.rsrtcsc.model.request.SMSModel;
import com.mtech.rsrtcsc.model.request.UpdateOnlineRegPaymentModel;
import com.mtech.rsrtcsc.model.response.BillDeskModel;
import com.mtech.rsrtcsc.model.response.RechargeApiResponse;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  RechargeStatusActivity extends BaseActivity<ActivityStatusBinding> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();
    private String status;

    private String TAG="RechargeStatus :";

    @Override
    protected ActivityStatusBinding getActivityBinding() {
        return ActivityStatusBinding.inflate(getLayoutInflater());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void init() {
        status = getIntent().getExtras().getString("status");
        Log.d(TAG,"Status :"+status);
        if(status.contains("0300"))
        {



              /*  Toast.makeText(RechargeStatusActivity.this, "Payment Failure", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
            Intent intent = new Intent(RechargeStatusActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
*/
            Log.d(TAG,"Status  : contains 0300"+status);
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            binding.status.setText("Payment Successful");
            billDeskResponseApi();
            SaveRechargeDetails();
        }
        else
        {
            /*Log.d(TAG,"Status  : contains 0300"+status);
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            binding.status.setText("Payment Successful");
            billDeskResponseApi();*/
            Toast.makeText(RechargeStatusActivity.this, "Payment Failure", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
            Intent intent = new Intent(RechargeStatusActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
            billdeskresponse();
        }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveRechargeDetails()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
/*
    "TransactionDate":"07/11/2024 10:30:45",
    "RegistrationID":"2147980",
    "BaseFare":"270.0",
    "AcAmount":"120.0",
    "TollAmount":"120.0",
    "OctAmount":"0.0",
    "HRAmount":"60.0",
    "ITAmount":"0",
    "TotalAmount":"570.0"
*/
        RechargeRequest rechargeRequest=new RechargeRequest();
        rechargeRequest.setTransactionDate(formattedDateTime);
        rechargeRequest.setRegistrationID(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.REGISTRATIONID));
        rechargeRequest.setBaseFare(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.BASEFARE));
        rechargeRequest.setAcAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.ACAMOUNT));
        rechargeRequest.setTollAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.TOLLAMOUNT));
        rechargeRequest.setOctAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.OCTAMOUNT));
        rechargeRequest.setHrAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.HRAMOUNT));
        rechargeRequest.setItAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.ITAMOUNT));
        rechargeRequest.setTotalAmount(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.TOTALAMOUNT));
        rechargeRequest.setExpireDate(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.EXPIRYDATE));
        rechargeRequest.setCardAutoID(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.CARDAUTOID));
        rechargeRequest.setPeriodRange(PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.PERIODRANGE));


        Log.d(TAG,"SaveRechargeDetails  : formattedDateTime :"+formattedDateTime);
        Log.d(TAG,"SaveRechargeDetails  : RegistrationID :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.REGISTRATIONID));
        Log.d(TAG,"SaveRechargeDetails  : BaseFare :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.BASEFARE));
        Log.d(TAG,"SaveRechargeDetails  : AcAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.ACAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : TollAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.TOLLAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : OctAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.OCTAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : HrAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.HRAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : ItAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.ITAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : TotalAmount :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.TOTALAMOUNT));
        Log.d(TAG,"SaveRechargeDetails  : Expiry Date :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.EXPIRYDATE));
        Log.d(TAG,"SaveRechargeDetails  : CardAutoID :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.CARDAUTOID));
        Log.d(TAG,"SaveRechargeDetails  : CardAutoID :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this, PrefrenceKeyConstant.PERIODRANGE));


        apiInterfaceRSTC.saveRechargeDetails(rechargeRequest).enqueue(new Callback<List<RechargeApiResponse>>()
        {
            @Override
            public void onResponse(Call<List<RechargeApiResponse>> call, Response<List<RechargeApiResponse>> response)
            {
              if (response.isSuccessful()) {
                    List<RechargeApiResponse> rechargeApiResponses = response.body();
                    if (rechargeApiResponses != null && !rechargeApiResponses.isEmpty()) {
                        // Since the response is an array, you can loop through it (if needed)
                        for (RechargeApiResponse apiResponse : rechargeApiResponses) {
                            Log.d(TAG, "saveRechargeDetails :success " + apiResponse.getOutMessage());

                            if(apiResponse.getOutMessage()=="FAILED to Saved")
                            {
                                Log.d(TAG, "saveRechargeDetails :success " + apiResponse.getOutMessage());

                                showDialog("Success: ");

                            }else
                            {
                            showDialog("Success: ");
                            }
                        }
                    } else {
                        Log.d(TAG, "Response is empty");
                    }
                } else {
                    Log.d(TAG, "Response failed with code: " + response.code());
                //    showDialog("Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<RechargeApiResponse>> call, Throwable t)
            {
                CommonUtils.dismissLoadingDialog();
                Log.d(TAG,"saveRechargeDetails  : saveRechargeDetails Response is Failed.."+t.getMessage());
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());

            }
        });
}
    @Override
    protected void initCtrl() {

    }
    private void billdeskresponse() {
        apiInterfaceRSTC.BillDeskResponse(new BillDeskResponseModel((status))).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().size();i++){
                        String str = response.body().get(i).getMsg();
                        System.out.println("Record Save Successfully"+str);
                        Log.d(TAG,"Status  : billdeskresponse Successful");

                    }
                }
            }
            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t)
            {
                CommonUtils.dismissLoadingDialog();
                Log.d(TAG,"Status  : billdeskresponse failed..");

                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void billdeskresponseupdate(){
        String[] str = status.split(Pattern.quote("|"));
        String AppId = str[2];
        String TransId = str[17];
        apiInterfaceRSTC.UpdateOnlineRegPayment(new UpdateOnlineRegPaymentModel(AppId,TransId)).enqueue(new Callback<List<UpdateOnlineRegPaymentModel>>()
        {
            @Override
            public void onResponse(Call<List<UpdateOnlineRegPaymentModel>> call, Response<List<UpdateOnlineRegPaymentModel>> response)
            {
                Log.d(TAG,"billdeskresponseupdate successs  :"+status);
            }
            @Override
            public void onFailure(Call<List<UpdateOnlineRegPaymentModel>> call, Throwable t)
            {
                Log.d(TAG,"billdeskresponseupdate successs  :"+t.getMessage());

            }
        });
    }
    private void billDeskResponseApi()
    {
        String[] str = status.split(Pattern.quote("|"));
        Log.d(TAG,"billDeskResponseApi Status :"+status);

        Log.d(TAG,"billDeskResponseApi str :"+str[17]);
        Log.d(TAG,"phone No :"+PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this,PrefrenceKeyConstant.PHONE_NO));

        CommonUtils.showLoadingDialog(this);
        apiInterface.billDeskResponse(new BillDeskResponseModel(status)).enqueue(new Callback<List<BillDeskModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response)
            {
                CommonUtils.dismissLoadingDialog();
                if(response.isSuccessful()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        String outMsg = response.body().get(i).getMsg();
                        Log.d(TAG,"billDeskResponseApi outMsg :"+outMsg);

                        billdeskresponseupdate();
                        if (response.isSuccessful())
                            // showDialog(outMsg);
                            smsApiInterface.sendSMS(new SMSModel("Dear "+ PrefrenceHelper.getPrefrenceStringValue
                                    (RechargeStatusActivity.this, PrefrenceKeyConstant.FULL_NAME) +
                                    ", your RFID application no "+Integer.parseInt(str[17])+
                                    " has been successfully recharged.You need to go to the depot within 24 hours to activate this card",
                                    PrefrenceHelper.getPrefrenceStringValue(RechargeStatusActivity.this,PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<String>()
                            {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response)

                                {
                                    CommonUtils.dismissLoadingDialog();
                                    Log.d(TAG," sendSMS : Success "+response.body().toString());

                                    if(response.isSuccessful())
                                        CommonUtils.showSnackBar(binding.getRoot()," SMS Sent...! ");

                                    // showDialog(outMsg);
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t)
                                {
                                    CommonUtils.dismissLoadingDialog();
                                    CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
                                    Log.d(TAG," sendSMS failed:"+t.getMessage());

                                }
                            });
                    }
                }
                else CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void showDialog(String message){
        String[] str = status.split(Pattern.quote("|"));
        String [] str1 = str[4].split(Pattern.quote("."));
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setCancelable(false)
                .setMessage("Recharge of "+ PrefrenceHelper.getPrefrenceStringValue(this, PrefrenceKeyConstant.FULL_NAME) + " on the " +str[13]+ " done successfully "+ " for the amount rs." +Integer.parseInt(str1[0])+" .")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
                        Intent intent = new Intent(RechargeStatusActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .show();
    }
}