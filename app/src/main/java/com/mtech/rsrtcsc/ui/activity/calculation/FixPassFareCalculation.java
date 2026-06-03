package com.mtech.rsrtcsc.ui.activity.calculation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.ui.activity.capture.UploadDocumentActivity;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityFixPassFareCalculationBinding;

import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FixPassFareCalculation extends BaseActivity<ActivityFixPassFareCalculationBinding> implements View.OnClickListener {

    public String globalvariables;
    private final Date date = Calendar.getInstance().getTime();
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected ActivityFixPassFareCalculationBinding getActivityBinding() {
        return ActivityFixPassFareCalculationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        RegisterationDataHelper.getInstance().getApplicationData().setPassType("FIXED PASSES");
        RegisterationDataHelper.getInstance().getApplicationData().setTransactionDate(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setStartDate(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setCreatedOn(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setCreatedBy("0");
        binding.transDate.setText(RegisterationDataHelper.getInstance().getApplicationData().getTransactionDate());
        binding.expDate.setText(RegisterationDataHelper.getInstance().getApplicationData().getExpiryDate());

        if (RegisterationDataHelper.getInstance().getApplicationData().getDepoid().isEmpty())
        {
            SharedPreferences sharedPreferences=getSharedPreferences("115.00",MODE_PRIVATE);
            String value = sharedPreferences.getString("115.00","115.00");
            binding.cardFees.setText(value);
            binding.amount.setText(value);
            RegisterationDataHelper.getInstance().getApplicationData().setCardFees(value);
        }else{
            SharedPreferences sharedPreferences=getSharedPreferences("40.00",MODE_PRIVATE);
            String value = sharedPreferences.getString("40.00","40.00");
            binding.cardFees.setText("40.00");
            binding.amount.setText("40.00");
            RegisterationDataHelper.getInstance().getApplicationData().setCardFees(value);
        }

        if(getIntent()!=null){
            if(getIntent().getStringExtra("CONCESSION_NAME")!=null) {
                globalvariables=getIntent().getStringExtra("CONCESSION_NAME");
                System.out.println("Selected Conssion name:" +globalvariables );
            } else {
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
            startActivity(new Intent(this, UploadDocumentActivity.class).putExtra("CONCESSION_NAME", globalvariables));
        } else if (v.getId() == R.id.btn_back) {
            clearText();
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(FixPassFareCalculation.this, MainActivity.class));
        }

    }

    private void clearText(){
        binding.expDate.setText(null);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

}