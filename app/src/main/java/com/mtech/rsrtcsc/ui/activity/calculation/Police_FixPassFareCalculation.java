package com.mtech.rsrtcsc.ui.activity.calculation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;

import com.mtech.rsrtcsc.databinding.ActivityPoliceFixPassFareCalculationBinding;
import com.mtech.rsrtcsc.ui.activity.capture.Police_Upload_Document;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Police_FixPassFareCalculation extends BaseActivity<ActivityPoliceFixPassFareCalculationBinding> implements View.OnClickListener {

    public String globalvariables;
    private final Date date = Calendar.getInstance().getTime();
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected ActivityPoliceFixPassFareCalculationBinding getActivityBinding() {
        return ActivityPoliceFixPassFareCalculationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPassType("FIXED PASSES");
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setTransactionDate(format.format(date));
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setStartDate(format.format(date));
      //  RegisterationDataHelper.getInstance().getPoliceApplicationModel().setCreatedOn(format.format(date));
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setCreatedBy("0");
        binding.transDate.setText(RegisterationDataHelper.getInstance().getPoliceApplicationModel().getTransactionDate());
        binding.expDate.setText(RegisterationDataHelper.getInstance().getPoliceApplicationModel().getExpiryDate());

        if (RegisterationDataHelper.getInstance().getPoliceApplicationModel().getDepoid().isEmpty())
        {
            SharedPreferences sharedPreferences=getSharedPreferences("115.00",MODE_PRIVATE);
            String value = sharedPreferences.getString("115.00","115.00");
            binding.cardFees.setText(value);
            binding.amount.setText(value);
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setCardFees(value);
        }else{
            SharedPreferences sharedPreferences=getSharedPreferences("00.00",MODE_PRIVATE);
            String value = sharedPreferences.getString("00.00","00.00");
            binding.cardFees.setText(value);
            binding.amount.setText(value);
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setCardFees(value);
        }

        if(getIntent()!=null){
            if(getIntent().getStringExtra("CONCESSION_NAME")!=null) {
                globalvariables=getIntent().getStringExtra("CONCESSION_NAME");
                System.out.println("Selected Conssion name:" +globalvariables );
            }else {
                System.out.println("Consession name: is null");
            }
        }


    }

    @Override
    protected void initCtrl() {
        binding.btnSave.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            startActivity(new Intent(this, Police_Upload_Document.class).putExtra("CONCESSION_NAME", globalvariables));
        } else if (v.getId() == R.id.btn_back) {
            clearText();
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(Police_FixPassFareCalculation.this, MainActivity.class));
        }

    }

    private void clearText(){
        binding.expDate.setText(null);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

}