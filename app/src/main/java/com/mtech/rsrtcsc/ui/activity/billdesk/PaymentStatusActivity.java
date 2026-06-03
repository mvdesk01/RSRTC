package com.mtech.rsrtcsc.ui.activity.billdesk;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityStatusBinding;

import com.mtech.rsrtcsc.model.request.BillDeskResponseModel;
import com.mtech.rsrtcsc.model.request.SMSModel;
import com.mtech.rsrtcsc.model.request.UpdateOnlineRegPaymentModel;
import com.mtech.rsrtcsc.model.response.BillDeskModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PaymentStatusActivity extends BaseActivity<ActivityStatusBinding> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();
    private String status;

    @Override
    protected ActivityStatusBinding getActivityBinding() {
        return ActivityStatusBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        status = getIntent().getExtras().getString("status");
        if(status.contains("0300")){
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            binding.status.setText("Payment Successful");
            billDeskResponseApi();
        }else{
            Toast.makeText(PaymentStatusActivity.this, "Payment Failure", Toast.LENGTH_SHORT).show();
           // startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
            Intent intent = new Intent(PaymentStatusActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        billdeskresponse();
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
                    }
                }
            }
            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }
    private void billdeskresponseupdate(){
        String[] str = status.split(Pattern.quote("|"));
        String AppId = str[2];
        String TransId = str[17];
        apiInterfaceRSTC.UpdateOnlineRegPayment(new UpdateOnlineRegPaymentModel(AppId,TransId)).enqueue(new Callback<List<UpdateOnlineRegPaymentModel>>() {
            @Override
            public void onResponse(Call<List<UpdateOnlineRegPaymentModel>> call, Response<List<UpdateOnlineRegPaymentModel>> response) {

            }

            @Override
            public void onFailure(Call<List<UpdateOnlineRegPaymentModel>> call, Throwable t) {

            }
        });
    }

    private void billDeskResponseApi() {
        String[] str = status.split(Pattern.quote("|"));
        CommonUtils.showLoadingDialog(this);
        apiInterface.billDeskResponse(new BillDeskResponseModel(status)).enqueue(new Callback<List<BillDeskModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response)
            {
                CommonUtils.dismissLoadingDialog();
                if(response.isSuccessful())
                {
                    for (int i = 0; i < response.body().size(); i++)
                    {
                        String outMsg = response.body().get(i).getMsg();
                        billdeskresponseupdate();
                        if (response.isSuccessful())
                            // showDialog(outMsg);
                            smsApiInterface.sendSMS(new SMSModel("Dear "+ PrefrenceHelper.getPrefrenceStringValue(PaymentStatusActivity.this, PrefrenceKeyConstant.FULL_NAME) +", your RFID application no "+Integer.parseInt(str[17])+" is successfully registered with RSRTC.You will get confirmation when RSRTC Approve your application.RSRTCR",PrefrenceHelper.getPrefrenceStringValue(PaymentStatusActivity.this,PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response)
                                {
                                    CommonUtils.dismissLoadingDialog();
                                    if(response.isSuccessful())
                                        showDialog(outMsg);
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t)
                                {
                                    CommonUtils.dismissLoadingDialog();
                                    CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
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
                .setMessage("Registration of "+ PrefrenceHelper.getPrefrenceStringValue(this, PrefrenceKeyConstant.FULL_NAME) + " on the " +str[13]+ " done successfully against registeration Id " +Integer.parseInt(str[17])+ " for the amount rs." +Integer.parseInt(str1[0])+" .")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       // startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
                        Intent intent = new Intent(PaymentStatusActivity.this, MainActivity.class)
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





//    private void billdeskresponseupdate() {
//        String[] str = status.split(Pattern.quote("|"));
//        String AppId = str[2];
//        String TransId = str[17];
//        apiInterfaceRSTC.UpdateOnlineRegPayment(new UpdateOnlineRegPaymentModel(AppId,TransId)).enqueue(new Callback<List<BillDeskModel>>() {
//            @Override
//            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(PaymentStatusActivity.this, "Updated", Toast.LENGTH_SHORT).show();
//
//                }
//
//
//            }
//            @Override
//            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
//
//            }
//        });
//
//
//    }


//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void billdeskresponseupdate() {
//        dismissLoadingDialog();
//        try {
//            ConnectionHelper helper = new ConnectionHelper();
//            Connection connect = helper.connectionClass();
//            if (connect != null){
//                String[] str = status.split(Pattern.quote("|"));
//                String query = "UPDATE MMemberNewRegistration SET PaymentStatus='Y' , TRANSACTIONID='" +str[2]+ "', Application_Status='Pending' where ApplicantId = '" +str[17]+ "'";
//                Statement statement = connect.createStatement();
//                ResultSet rs = statement.executeQuery(query);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }