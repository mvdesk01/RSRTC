package com.mtech.rsrtcsc.ui.activity.cardstatuss;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardStatusView extends AppCompatActivity{
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    TextView Appno;
    TextView namee,depot,Concession;
    TextView reg , pay,cardf,tran,rsrtc,cards, cardi,remark,ExpiryDate,contact,rechargedate, rechargeamount,tostop,fromstop,EmpID,Post,Location;
    LinearLayout remarks,Tobusstop, Frombusstop,ConAdd,v_EmpId,v_post,v_location,v_trans;
    private String globalvariable;
    ImageView img;
    private String appid;
    private List<SpinnerDataModel> cardstatus= new ArrayList();
    private List<SpinnerDataModel> concession= new ArrayList();
    DatabaseHelper dbHelper;
    String rem;
    Button Back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_status_view);
        dbHelper = new DatabaseHelper(this);

        Back = findViewById(R.id.back);
        Appno = findViewById(R.id.Appno);
        namee = findViewById(R.id.name);
        reg = findViewById(R.id.Registration);
        pay = findViewById(R.id.payment);
        cardf = findViewById(R.id.cardfees);
        tran = findViewById(R.id.transactionNo);
        rsrtc = findViewById(R.id.rsrtcStatus);
        cards = findViewById(R.id.CardStatus);
        cardi = findViewById(R.id.CardId);
        remark = findViewById(R.id.Remarks);
        remarks = findViewById(R.id.gone_remark);
        ExpiryDate = findViewById(R.id.Exp);
        img =findViewById(R.id.ivHumberger);
        contact=findViewById(R.id.Contact);
//        rechargedate=findViewById(R.id.RDate);
//        rechargeamount=findViewById(R.id.RAmount);
        fromstop = findViewById(R.id.FromStop);
        Frombusstop = findViewById(R.id.frombusstop);
        tostop = findViewById(R.id.ToStop);
        Tobusstop= findViewById(R.id.tobusstop);
        depot = findViewById(R.id.depot);
        Concession = findViewById(R.id.concession);
        ConAdd=findViewById(R.id.conadd);
        EmpID = findViewById(R.id.EmpID);
        Post = findViewById(R.id.Post);
        Location = findViewById(R.id.Location);
        v_EmpId = findViewById(R.id.gone_empid);
        v_post = findViewById(R.id.gone_post);
        v_location = findViewById(R.id.gone_Location);
        v_trans = findViewById(R.id.gone_trans);
        ConcessionType();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardStatusView.this, MainActivity.class));
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CardStatusView.this, MainActivity.class));
            }
        });


    }


    public static boolean loginCheck(Cursor cursor, String emailCheck) {
        while (cursor.moveToNext()){
            if (cursor.getString(0).equals(emailCheck)) {
                return true;
            }
        }
        return false;
    }



    private void ConcessionType(){
        apiInterfaceRSTC.GetCardStatusAPP(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(CardStatusView.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    cardstatus = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        Appno.setText(model.getApplicantID());
                        namee.setText(model.getcName());
                        reg.setText(model.getReg_Date());
                        pay.setText(model.getPaymentStatus());
                        cardf.setText(model.getCardFees());
                        cardi.setText(model.getCard_Auto_ID());
                        cards.setText(model.getCard_Status());
                        String transaction = model.getTransactionId();
                        tran.setText(transaction);
                        rsrtc.setText(model.getApplication_Status());
                        contact.setText(model.getContactAddr());
//                        rechargedate.setText(model.getRechargeDate());
//                        rechargeamount.setText(model.getRechargeAmt());
                        fromstop.setText(model.getFromStop());
                        tostop.setText(model.getTillStop());
                        depot.setText(model.getDepot());
                        ExpiryDate.setText(model.getExpiryDate());
                        Concession.setText(model.getConcession_Name());
                        EmpID.setText(model.getPoliceEmployeeID());
                        Post.setText(model.getPolicePost());
                        Location.setText(model.getPoliceLocation());
                        String con = model.getConcessionCode();
                        if(transaction.equals("")){
                        }else {
                            v_trans.setVisibility(View.VISIBLE);
                        }

                        if(con.equals("SP")){
                            Frombusstop.setVisibility(View.VISIBLE);
                            Tobusstop.setVisibility(View.VISIBLE);
                            ConAdd.setVisibility(View.INVISIBLE);
                        }
                        rem = model.getApplication_Status();
                        if(rem.equals("Reject")){
                            remarks.setVisibility(View.VISIBLE);
                            remark.setText(model.getRemarks());
                        }
                        if(con.equals("RPF") || con.equals("DPF")){
                            cardf.setText("Not Applicable");
                            v_EmpId.setVisibility(View.VISIBLE);
                            v_post.setVisibility(View.VISIBLE);
                            v_location.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
            }
        });
    }

    public void onClick(View view) {
    }
}