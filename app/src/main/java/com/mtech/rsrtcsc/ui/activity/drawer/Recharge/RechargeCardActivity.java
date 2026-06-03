package com.mtech.rsrtcsc.ui.activity.drawer.Recharge;

import static com.mtech.rsrtcsc.repository.cache.PrefrenceHelper.saveRechargeData;
import static com.mtech.rsrtcsc.utils.CommonUtils.isOnline;
import static com.mtech.rsrtcsc.utils.CommonUtils.showSnackBar;

import static net.sourceforge.jtds.util.Logger.println;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.billdesk.sdk.PaymentOptions;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityRechargeCardBinding;
import com.mtech.rsrtcsc.model.request.BillDeskRequestModel;
import com.mtech.rsrtcsc.model.request.BillDeskRequestPayment;
import com.mtech.rsrtcsc.model.request.BilldeskRequestPayloadModel;
import com.mtech.rsrtcsc.model.request.CardDataModel;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.ConcessionrateRequest;
import com.mtech.rsrtcsc.model.request.GetFareRequest;
import com.mtech.rsrtcsc.model.request.RechargeRequest;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.response.BillDeskModel;
import com.mtech.rsrtcsc.model.response.CardData;
import com.mtech.rsrtcsc.model.response.ConcessionRateResponse;
import com.mtech.rsrtcsc.model.response.GetFareResponse;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.billdesk.BilldeskCallBackForRecharge;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.CustomLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RechargeCardActivity extends BaseActivity<ActivityRechargeCardBinding> implements View.OnClickListener, TextWatcher, Callback<List<CardData>>, AdapterView.OnItemSelectedListener {
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private String payload = PrefrenceKeyConstant.BDSKUATY;
    private String registrationID;
    private String emaiId;
    private String payloadRest = "|NA|NA|NA|INR|NA|R|"+ PrefrenceKeyConstant.BDSKUATY.toLowerCase(Locale.ROOT)+"|NA|NA|F|NA|";
    private  String requestmsg;
    private String billdestMsg;
    private String msg;
    private String TAGval="Payment :";
    private RSRTCInterface apiInterfaceService= new RSRTCConnection().createServiceRoute();
    private List<SpinnerDataModel> concession= new ArrayList();
    private List<GetFareResponse> GetFareResponseList= new ArrayList();
    private List<ConcessionRateResponse> concessionRateResponsesList= new ArrayList();
    private List<CardData> CardData=new ArrayList<>();
    TextView name,concessionType,depoName,fromStop,tillStop,busService,amount,rechargePeriod,rechargeDate,exp_date,rechargeExpirydate;
    LinearLayout noData;
    ScrollView scrollView;
    TextView base_fare,tvacc,tvhr,tvoctroi,tvit,tvtoll,tvtotalamount,tvtotal_fare,tvadmin_fees,tvcard_fees,tvtrans_date,cardId;
    StringBuilder fromStopp = new StringBuilder();
    StringBuilder conRate = new StringBuilder();
    String dataParsed1 = "";
    String dataParsed2 = "";
    List<String> list = new ArrayList<>();
    Button back,next,clear, fareCalculate;
    String intrastate,interstate,acc,hr,octroi,it,toll;
    Float finalAmount,amount1,amount2,amt,finalAmount2,totalFare;
    Float ac,tl,oct,hrr;
    Float Acs,Toll,Octroi,Hr;
    String concessRate,busTypecd,concessCode;
   Date oneMonth,twoMonth,threeMonth;
    String Id,routeNo,fromstopCode,tillStopCode,routename;
    private String selectedItem,concessionCodeValue;
    String cardFees;
    String aa = "0";
    int compareDay;
    private boolean isDateAfter=false;

    @Override
    protected ActivityRechargeCardBinding getActivityBinding() {
        return ActivityRechargeCardBinding.inflate(getLayoutInflater());
    }
    @Override
    protected void init()
    {
     //  binding.setData(new CardDataModel("23240200005286"));
  //     binding.setData(new CardDataModel("29210200012551"));
        binding.ivHumberger.setOnClickListener(this);
        binding.pasperiodddd.setOnItemSelectedListener(this);
        name=findViewById(R.id.name);
        concessionType=findViewById(R.id.concessionType);
        depoName=findViewById(R.id.deponame);
        fromStop=findViewById(R.id.fromStop);
        tillStop=findViewById(R.id.tillStop);
        busService=findViewById(R.id.busservice);
        amount=findViewById(R.id.amount);
        exp_date=findViewById(R.id.exp_date);
        rechargePeriod=findViewById(R.id.rechargePeriod);
        rechargeDate=findViewById(R.id.rechargedate);
        base_fare=findViewById(R.id.base_fare);
        tvacc=findViewById(R.id.tvacc);
        tvhr=findViewById(R.id.tvhr);
        tvoctroi=findViewById(R.id.tvoctroi);
        tvit=findViewById(R.id.tvit);
        tvtoll=findViewById(R.id.tvtoll);
        tvtotalamount=findViewById(R.id.tvtotalamount);
        tvtotal_fare=findViewById(R.id.tvtotal_fare);
        tvadmin_fees=findViewById(R.id.tvadmin_fees);
        tvcard_fees=findViewById(R.id.tvcard_fees);
        tvtrans_date=findViewById(R.id.tvtrans_date);
        rechargeExpirydate=findViewById(R.id.rechargeExpirydate);
        noData=findViewById(R.id.noData);
        scrollView=findViewById(R.id.scrollview);
        binding.Rechargenow.setOnClickListener(this);
        binding.calculatefare.setOnClickListener(this);
        cardId=findViewById(R.id.cardId);
        CommonUtils.setSpinner(binding.pasperiodddd, R.array.monthperiod);


        if(!isOnline(this)){

            showSnackBar(binding.getRoot(),"Please turn on internet");
            scrollView.setVisibility(View.GONE);

        } else
        {
            showLoadingDialog(this);

            getCardId();
            //getcardDetails();
        }

        Log.d("RechargeCardActivity","Selected days :"+selectedItem);
    }
    private void getcardDetails()
    {
        CommonUtils.showLoadingDialog(this);
        Log.d("getCadData","binding.getData()"+binding.getData().getCardNo());
        Log.d("getCadData","binding.getData()"+binding.getData());


        apiInterface.getCardData(binding.getData()).enqueue(this);
    }

    @SuppressLint("SuspiciousIndentation")
    public static void showLoadingDialog(Activity activity)
    {
        if(CommonUtils.customProgressBar==null)
        CommonUtils.customProgressBar = CustomLoader.show(activity, true);
        try
        {
            CommonUtils.customProgressBar.setCancelable(false);
            CommonUtils.customProgressBar.show();
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }

    }
    private void getCardId() {
      String mobileNo=PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO);
        Log.d("RechargeCardActivity :" ,"mobileNo :"+mobileNo.toString());

        apiInterface.GetVirtualCard(new CardStatusModel(mobileNo)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                dismissLoadingDialog();

                if(response.isSuccessful())
                {
                    Log.d("RechargeCardActivity :" ,"response.body() :"+response.body().toString());

                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    Log.d("RechargeCardActivity :" ,"getCardId  concession.size() :"+concession.size());
                     if (concession.size()==0)
                     {
                         //here show details not found  and make invisible All Layouts...
                         scrollView.setVisibility(View.GONE);
                         noData.setVisibility(View.VISIBLE);

                     }
                     else
                     {
                         noData.setVisibility(View.GONE);
                         scrollView.setVisibility(View.VISIBLE);
                         for(SpinnerDataModel model : response.body())
                         {
                             Id = model.getCard_Auto_ID();
                            //Id = "23240200005286";

                             String AppId ="";
                             AppId  = model.getApplicantID();
                             binding.setData(new CardDataModel(Id));
                            getcardDetails();
                             Log.d("RechargeCardActivity :" ,"Id :"+Id);
                             Log.d("RechargeCardActivity :" ,"AppId :"+AppId);
                         }
                     }
                }
                else
                {
                    Log.d("RechargeCardActivity :" ,"getCardId not success :");

                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t)
            {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });

    }
    public static void dismissLoadingDialog(){
        try
        {
            if (null != CommonUtils.customProgressBar && CommonUtils.customProgressBar.isShowing()) {
                CommonUtils.customProgressBar.dismiss();
                CommonUtils.customProgressBar=null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void initCtrl() {
   //     binding.tieEmail.addTextChangedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(RechargeCardActivity.this, MainActivity.class));
        }
        else if (v.getId() == R.id.back) {
            startActivity(new Intent(RechargeCardActivity.this, MainActivity.class));
        }else if (v.getId() == R.id.calculatefare) {
            try{
                if(concessRate==null)
                {
                    getconcessionRate();
                }
                else {
                    String date1Str = rechargeExpirydate.getText().toString(); // Example date in dd/MM/yy format
                    // String date2Str = "20/10/23"; // Example date in dd/MM/yy format

                    DateTimeFormatter formatter = null;
                    formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

                    String cardRechargeDate=rechargeDate.getText().toString();
                    LocalDate today = LocalDate.now();
                    //    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yy");
                    LocalDate RechargeDate = LocalDate.parse(cardRechargeDate, formatter);
                    String todayDate = today.format(formatter);
                    LocalDate todayDatee = LocalDate.parse(todayDate, formatter);
                    long daysBetweenn = 0;
                    daysBetweenn = ChronoUnit.DAYS.between(RechargeDate, todayDatee);
                    int compareDayy = (int) daysBetweenn;
                    Log.e("RechargeCardActivity : ", "daysBetween=  compareDayyyyyyyyy "+ daysBetweenn);

                    DateTimeFormatter formatterr = null;
                    formatterr = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    LocalDate todaydate = LocalDate.now();
                    String ftodayDate = todaydate.format(formatterr);
                    LocalDate fftodayDatee = LocalDate.parse(ftodayDate, formatterr);

                    LocalDate date1 = null;
                    date1 = LocalDate.parse(date1Str, formatterr);
                    long daysBetween = 0;
                    daysBetween = ChronoUnit.DAYS.between(fftodayDatee,date1);
                    compareDay = (int) daysBetween;
                    if(fftodayDatee.isAfter(date1))
                    {
                        Log.e("RechargeCardActivity : ", "after date................ " );
                           isDateAfter=true;
                    }
                    if(fftodayDatee.isBefore(date1) ||fftodayDatee.isEqual(date1))
                    {
                        Log.e("RechargeCardActivity : ", "before date................ " );
                        isDateAfter=false;

                    }

                    Log.e("RechargeCardActivity : ", "daysBetween= compareDay "+ daysBetween);
                  if(compareDayy==0)
                    {
                        CommonUtils.showSnackBar(binding.getRoot()," You Have Already Recharged Today..! ");
                    }
                    else if(compareDay >=7 )
                    {
                        CommonUtils.showSnackBar(binding.getRoot()," Recharge Not Allowed 7 Days Before..! ");
                    }
                    else

                    {//PayNow

                        fareCalculate();
                    }
                }
            }catch(Exception e){
                Log.e("RechargeCardActivity : ", "Exception at Farecalculate : "+ e.getMessage().toString());

            }

            //calculate fare
        }
        else if (v.getId() == R.id.Rechargenow)
        {
            try {

                if(tvtotalamount.getText().toString().equals(""))
                {
                    CommonUtils.showSnackBar(binding.getRoot()," Before Proceeding, Please Calculate The Fare..!  ");
                }
                else
                {
                   /* "RegistrationID":"2147980",
                        "BaseFare":"90.00",
                        "AcAmount":"45.00",
                        "TollAmount":"0.00",
                        "OctAmount":"0.00",
                        "HRAmount":"0.00",
                        "ITAmount":"0.00",
                        "TotalAmount":"135.00"  */
                    RechargeRequest rechargeRequest=new RechargeRequest();
                    rechargeRequest.setRegistrationID(registrationID);
                    rechargeRequest.setBaseFare(base_fare.getText().toString());
                    rechargeRequest.setAcAmount(tvacc.getText().toString());
                    rechargeRequest.setTollAmount(tvtoll.getText().toString());
                    rechargeRequest.setOctAmount(tvoctroi.getText().toString());
                    rechargeRequest.setHrAmount(tvhr.getText().toString());
                    rechargeRequest.setItAmount(tvit.getText().toString());
                    rechargeRequest.setTotalAmount(tvtotalamount.getText().toString());
                    rechargeRequest.setExpireDate(exp_date.getText().toString());
                    rechargeRequest.setCardAutoID(cardId.getText().toString());
                   rechargeRequest.setPeriodRange(selectedItem);

                    saveRechargeData(RechargeCardActivity.this,rechargeRequest);

                    rechargeNow();
                }
            }catch(Exception e)
            {
                Log.e("RechargeCardActivity : ", "Exception at rechargeNow : "+ e.getMessage().toString());
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private  void rechargeNow()
    {
            // lastRecharge
            //showDialog();
            String message = "Your recharge amount is " + tvtotalamount.getText().toString() + " for " + selectedItem + ".\n";
            showRechargeDialog(message);
       // }
    }
    private void showRechargeDialog(String message) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_recharge, null);
        // Set the message in the dialog
        TextView rechargeMessage = dialogView.findViewById(R.id.rechargeMessage);
        rechargeMessage.setText(message);
        // Create the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        // Set button listeners
        Button buttonYes = dialogView.findViewById(R.id.buttonYes);
        Button buttonNo = dialogView.findViewById(R.id.buttonNo);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processRechargePayment();
                dialog.dismiss();
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }
    private void processRechargePayment() {

        if(!isOnline(this)){

            showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        else
        {
            Log.e("RechargeCardActivity : ", "PrefrenceKeyConstant.EMAIL_ID : "+ PrefrenceKeyConstant.EMAIL_ID);
            generatePayloadBillDesk();
            billdeskrequestformsg();
            billDeskRequest();
        }


    }
    private void generatePayloadBillDesk() {

       //tvtotalamount.setText("1");
        CommonUtils.showLoadingDialog(this);
        @SuppressLint("DefaultLocale") String str = String.format("%014d", Integer.parseInt(registrationID));
         Log.d(TAGval,"str: "+str);
         String paynow = "|"+PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO)+"|MOB|NA|NA|NA|"+ PrefrenceKeyConstant.BILL_DESK_DUMP_URL;
        Log.d(TAGval,"phone no: "+PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO));
        Log.d(TAGval,"BILL_DESK_DUMP_URL: "+PrefrenceKeyConstant.BILL_DESK_DUMP_URL);
        Log.d(TAGval,"payload1: : "+payload);
        println("BILL_DESK_DUMP_URL: "+PrefrenceKeyConstant.BILL_DESK_DUMP_URL);

        //payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + PrefrenceKeyConstant.pay + payloadRest +str +paynow;
        payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + tvtotalamount.getText().toString() + payloadRest +str +paynow;
        Log.d(TAGval,"payload2: Date  "+new SimpleDateFormat("yyMMddHHmmssms").format(new Date()));

        Log.d(TAGval,"payload2: fees "+tvtotalamount.getText().toString());
        Log.d(TAGval,"payload2: payloadRest "+payloadRest);
        Log.d(TAGval,"payload2: paynow "+paynow);

        Log.d(TAGval,"payload2: final "+payload);

        apiInterface.getCheckSum(new BilldeskRequestPayloadModel(payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()) {
                    Log.d(TAGval,"getCheckSum response payload : Success ");

                    for(int i=0;i<response.body().size();i++){
                        payload= payload +  "|" + response.body().get(i).getMsg().toUpperCase(Locale.ROOT);
                        requestmsg = payload +  "|" + response.body().get(i).getMsg().toUpperCase(Locale.ROOT) ;
                        billdestMsg = response.body().get(i).getMsg().toUpperCase(Locale.ROOT);
                        msg = response.body().get(i).getMsg();
                        Log.e("response",response.body().get(i).getMsg().toUpperCase(Locale.ROOT));
                        Log.d(TAGval,"getCheckSum response: "+response.body().get(i).getMsg().toUpperCase(Locale.ROOT));
                        Log.d(TAGval,"getCheckSum response payload : "+payload);
                        break;
                    }
                }
                else
                {
                    Log.d(TAGval,"getCheckSum response payload : failed ");
                    CommonUtils.dismissLoadingDialog();
                    CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error3));
                }
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                Log.d(TAGval,"getCheckSum response payload : Failure ");

                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void billdeskrequestformsg() {
        try {


            Log.d(TAGval,"billdeskrequestformsg  CardNo "+registrationID);
            Log.d(TAGval,"billdeskrequestformsg  UserId "+ PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO));
            Log.d(TAGval,"billdeskrequestformsg Msg "+payload);
            Log.d(TAGval,"billdeskrequestformsg RequestType "+PrefrenceKeyConstant.REQUEST_TYPE_BILLDESKK);

        }
        catch (Exception e){

        }
        apiInterface.BillDeskRequest(new BillDeskRequestPayment(registrationID,PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO),payload,PrefrenceKeyConstant.REQUEST_TYPE_BILLDESKK)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().size();i++)
                    {

                        String str = response.body().get(i).getMsg();
                        System.out.println("Insert"+str);
                        Log.d(TAGval,"BillDeskRequest response  success : Inserted ");

                    }

                }
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                Log.d(TAGval,"BillDeskRequest response  failed : not Inserted ");

            }
        });

    }

    private void billDeskRequest() {
        CommonUtils.showLoadingDialog(this);
        Log.d(TAGval,"TAG billDeskRequest Msg "+ payload);
        Log.d(TAGval,"TAG billDeskRequest user-email "+ emaiId);
        Log.d(TAGval,"TAG billDeskRequest user-mobile "+PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO));

        apiInterface.billDeskRequest(new BillDeskRequestModel(payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                CommonUtils.dismissLoadingDialog();
                Intent sdkIntent = new Intent(RechargeCardActivity.this, PaymentOptions.class);
                sdkIntent.putExtra("msg", payload);
                sdkIntent.putExtra("user-email", emaiId);
                sdkIntent.putExtra("user-mobile",PrefrenceHelper.getPrefrenceStringValue(RechargeCardActivity.this, PrefrenceKeyConstant.PHONE_NO));
                sdkIntent.putExtra("callback",new BilldeskCallBackForRecharge());
                startActivity(sdkIntent);

                Log.d(TAGval,"billDeskRequest response  success :  ");

            }
            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
                Log.d(TAGval,"billDeskRequest response  failure :  ");

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fareCalculate() {
        Log.e("RechargeCardActivity : ", "amount acc= "+ acc);

        ac = Float.parseFloat(acc.trim()) * 2;
        tl = Float.parseFloat(toll.trim()) * 2;
        oct = Float.parseFloat(octroi.trim()) * 2;
        hrr = Float.parseFloat(hr.trim()) * 2;

        Log.e("RechargeCardActivity ","ac = "+  ac);
        Log.e("RechargeCardActivity "," tl = "+ tl);
        Log.e("RechargeCardActivity ",  "oct = "+  oct);
        Log.e("RechargeCardActivity ", "hrr = "+ hrr);

        amount1 = Float.parseFloat(interstate );
        amount2 = Float.parseFloat( intrastate);
        amt = amount1 + amount2;
        Log.e("RechargeCardActivity : "+ "", ""+ amt+"concessRate : "+concessRate);

        finalAmount = 2 * (amt - (amt * Float.parseFloat(concessRate)/100));
        String date1Str = rechargeExpirydate.getText().toString(); // Example date in dd/MM/yy format

        DateTimeFormatter formatterr = null;
        formatterr = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate todaydate = LocalDate.now();
        String ftodayDate = todaydate.format(formatterr);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date datex;
        try {
            datex = formatter.parse(date1Str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    Date todaydatee;
        try {
            todaydatee = formatter.parse(ftodayDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datex);
        Log.d("RechargeCardActivity ", "date1Str"+date1Str);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(todaydatee);
        Log.d("RechargeCardActivity ", "date1Str"+date1Str);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy ");
        if (selectedItem.equals("30 Days"))
        {
            finalAmount = 30 * finalAmount;
            Acs = 30 * ac;
            Toll = 30 * tl;
            Octroi = 30 * oct;
            Hr = 30 * hrr;
                if(!isDateAfter==true)
                {
                    calendar.add(Calendar.MONTH, 1); // to get previous year add -1
                    oneMonth = calendar.getTime();


                    exp_date.setText(format.format(oneMonth));
                }
                else{
                    calendar1.add(Calendar.MONTH, 1); // to get previous year add -1
                    oneMonth = calendar1.getTime();


                    exp_date.setText(format.format(oneMonth));
                }


        }
        else if (selectedItem.equals("60 Days"))
        {
            finalAmount = 60 * finalAmount;
            Acs = 60 * ac;
            Toll = 60 * tl;
            Octroi = 60 * oct;
            Hr = 60 * hrr;
            calendar.add(Calendar.MONTH, 2); // to get previous year add -1
            twoMonth = calendar.getTime();

            exp_date.setText(format.format(twoMonth));
        }
        else if (selectedItem.equals("90 Days"))
        {
            finalAmount = 90 * finalAmount;
            Acs = 90 * ac;
            Toll = 90 * tl;
            Octroi = 90 * oct;
            Hr = 90 * hrr;
            calendar.add(Calendar.MONTH, 3); // to get previous year add -1
            threeMonth = calendar.getTime();
            exp_date.setText(format.format(threeMonth));
        }
        base_fare.setText((finalAmount.toString()));
        tvacc.setText(Acs.toString());
        tvhr.setText(Hr.toString());
        tvoctroi.setText(Octroi.toString());
        tvtoll.setText(Toll.toString());

        totalFare = finalAmount + Acs + Toll + Octroi + Hr;
        tvtotal_fare.setText(totalFare.toString());
        tvtotalamount.setText(totalFare.toString());
        tvtrans_date.setText(ftodayDate);
        tvadmin_fees.setText(aa);
        cardFees = totalFare + aa;
        tvcard_fees.setText(aa);


    }

    private void getconcessionRate() {

        ConcessionrateRequest concessionrateRequest=new ConcessionrateRequest();
        concessionrateRequest.setConcessionCode(concessionCodeValue);
        concessionrateRequest.setBusType(busService.getText().toString());

        apiInterface.GetconcessionRate(concessionrateRequest).enqueue(new Callback<List<ConcessionRateResponse>>() {
            @Override
            public void onResponse(Call<List<ConcessionRateResponse>> call, Response<List<ConcessionRateResponse>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()){
                    Log.d("RechargeCardActivity :" ,"response.body() :"+response.body().toString());
                    concessionRateResponsesList=response.body();
                  for (ConcessionRateResponse concessionRateResponse :response.body())
                  {
                   // ConcessionRateResponse getFareResponse = response.body();
                    Log.e("RechargeCardActivity : ", "ConcessionRate = "+ ""+""+concessionRateResponse.getConcessionRate());
                    Log.e("RechargeCardActivity : ", "ConcessionCode = "+concessionRateResponse.getConcessionCode());
                    Log.e("RechargeCardActivity : ", "BusTypeCd = "+concessionRateResponse.getBusTypeCd());
                      concessRate=concessionRateResponse.getConcessionRate();


                  }
                }
                else{
                    Log.d("RechargeCardActivity :" ,"not success"+response.body().toString());

                }
            }
            @Override
            public void onFailure(Call<List<ConcessionRateResponse>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }
    public void getfare()
    {
        Log.e("RechargeCardActivity : ", "fromstopCode = "+ fromstopCode);
        Log.e("RechargeCardActivity : ", "tillStopCode = "+tillStopCode);
        Log.e("RechargeCardActivity : ", "routeNo = "+routeNo);
        Log.e("RechargeCardActivity : ", "busService = "+busService.getText().toString());

        GetFareRequest getFareRequest=new GetFareRequest();
        getFareRequest.setFromStop(fromstopCode);
        getFareRequest.setTillStop(tillStopCode);
        getFareRequest.setRouteNo(routeNo);
        getFareRequest.setBusType(busService.getText().toString());
        apiInterfaceService.Getfare(getFareRequest).enqueue(new Callback<GetFareResponse>() {
            @Override
            public void onResponse(Call<GetFareResponse> call, Response<GetFareResponse> response) {
                dismissLoadingDialog();
                CommonUtils.dismissLoadingDialog();
                if(response.isSuccessful())
                {
                    Log.d("RechargeCardActivity :" ,"response.body() :"+response.body().toString());
                    GetFareResponse getFareResponse = response.body();
                    Log.e("RechargeCardActivity : ", "intrastate = "+ ""+""+getFareResponse.getIntrastate());
                    Log.e("RechargeCardActivity : ", "interstate = "+getFareResponse.getInterstate());
                    Log.e("RechargeCardActivity : ", "acc = "+getFareResponse.getAcc());
                    Log.e("RechargeCardActivity : ", "hr = "+getFareResponse.getHr());
                    Log.e("RechargeCardActivity : ", "octroi = "+getFareResponse.getOctroi());
                    Log.e("RechargeCardActivity : ", "it ="+getFareResponse.getIt());
                    Log.e("RechargeCardActivity : ", "tol = "+getFareResponse.getToll());
                    intrastate = getFareResponse.getIntrastate();
                    interstate = getFareResponse.getInterstate();
                    acc = getFareResponse.getAcc();
                    hr = getFareResponse.getHr();
                    octroi = getFareResponse.getOctroi();
                    it = getFareResponse.getIt();
                    toll = getFareResponse.getToll();
                }
                else
                {
                    Log.d("RechargeCardActivity :" ,"not success"+response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<GetFareResponse> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
       // binding.tilEmail.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s){
    }
    @Override
    public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
      //  CommonUtils.dismissLoadingDialog();
        if(response.isSuccessful()){
            for(int i=0;i<response.body().size();i++){
                if(binding.getData().getCardNo().equalsIgnoreCase(response.body().get(i).getCardNo())){
                    getSharedPreferences(PrefrenceKeyConstant.PREF_NAME, Context.MODE_PRIVATE).edit().putString("cardNo", Id).apply();
                   // startActivity(new Intent(RechargeCardActivity.this, PaymentOptions.class).putExtra("amount", response.body().get(i).getAmount()));
                     Log.d("getCadData"," sucessfull"+response.body().toString());

                    for(CardData cardData : response.body()) {

                        routename=cardData.getRouteName();
                        String[] parts = routename.split(" to ");

                        String fromStopName = parts[0]; // NAGAUR
                        String tillStopName = parts[1]; // LADNU
                        name.setText(cardData.getCardName());
                        concessionType.setText(cardData.getConcessionName());
                        depoName.setText(cardData.getDepotName());
                        fromStop.setText(fromStopName);
                        tillStop.setText(tillStopName);
                        busService.setText(cardData.getBusService());
                        amount.setText(cardData.getAmount());
                        rechargePeriod.setText(cardData.getRechargePeriod());
                        rechargeDate.setText(cardData.getRechargeDate());
                        rechargeExpirydate.setText(cardData.getExpiryDate());
                        ///rechargeExpirydate.setText("22/10/2024");
                        routeNo=cardData.getRouteNo();
                        fromstopCode=cardData.getFromStop();
                        tillStopCode= cardData.getTillStop();
                        cardId.setText(cardData.getCardNo());
                        registrationID=(cardData.getRegistrationID());
                        emaiId=(cardData.getEmailID());
                        getfare();

                        showLoadingDialog(this);


                        String concessionCode=concessionType.getText().toString();

                        if(concessionCode.equals("FEMALE MONTHLY PASS"))
                        {
                            concessionCodeValue="FMTP";
                        } else if (concessionCode.equals("MONTHLY PASS")) {
                            concessionCodeValue="MTP";
                        }
                        Log.e("RechargeCardActivity : ", "concessionCddeValue = "+ concessionCodeValue);
                        getconcessionRate();
                    }
                }
            }
        }

        else {
            noData.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);

         //   Log.d("getCadData","not successfull"+response.body().toString());
            CommonUtils.dismissLoadingDialog();
          //  CommonUtils.showSnackBar(binding.getRoot(),response.body());
        }
    }

    @Override
    public void onFailure(Call<List<CardData>> call, Throwable t) {
        CommonUtils.dismissLoadingDialog();
        CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

         selectedItem = parent.getItemAtPosition(position).toString();
        Log.d("RechargeCardActivity :" ,"selected Item  :"+selectedItem);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

