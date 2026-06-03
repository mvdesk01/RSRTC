package com.mtech.rsrtcsc.ui.activity.drawer.report;

import android.content.Intent;
import android.view.View;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityReportBinding;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends BaseActivity<ActivityReportBinding> implements View.OnClickListener {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private List<SpinnerDataModel> concession= new ArrayList();

    @Override
    protected ActivityReportBinding getActivityBinding() {
        return ActivityReportBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
                name();

    }

    @Override
    protected void initCtrl() {
        binding.ivHumberger.setOnClickListener(this);
        binding.back.setOnClickListener(this);

    }

    private void name() {

        apiInterfaceRSTC.GetCardStatusAPP(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(ReportActivity.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        binding.app.setText(model.getApplicantID());
                        binding.transid.setText(model.getTransactionId());
                        binding.amount.setText(model.getCardFees());
                        binding.date.setText(model.getReg_Date());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

            }
        });
    }


    public void onClick(View v) {
        if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(ReportActivity.this, MainActivity.class));
        } else if (v.getId() == R.id.back) {
            startActivity(new Intent(ReportActivity.this, MainActivity.class));
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}