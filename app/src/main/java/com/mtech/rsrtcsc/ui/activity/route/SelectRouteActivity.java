package com.mtech.rsrtcsc.ui.activity.route;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySelectRouteBinding;
import com.mtech.rsrtcsc.model.request.RouteModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.ui.adapter.RouteAdapter;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectRouteActivity extends BaseActivity<ActivitySelectRouteBinding> implements Callback<List<RouteModel>> {

    private RSRTCInterface apiInterface = new RSRTCConnection().createServiceRoute();
    private String globalvariables;

    @Override
    protected ActivitySelectRouteBinding getActivityBinding() {
        return ActivitySelectRouteBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        CommonUtils.showLoadingDialog(this);
        apiInterface.getRouteDetail(new com.mtech.rsrtcsc.model.request.RouteModel(RegisterationDataHelper.getInstance().getApplicationData().getBusType(),
                        RegisterationDataHelper.getInstance().getApplicationData().getFromStopVal(),
                        RegisterationDataHelper.getInstance().getApplicationData().getTillStopVal()))
                       .enqueue(this);
        if (getIntent() != null) {
            if (getIntent().getStringExtra("CONCESSION_NAME") != null) {
                globalvariables = getIntent().getStringExtra("CONCESSION_NAME");
                System.out.println("Selected Conssion name:" + globalvariables);
            } else {
                System.out.println("Consession name: is null");
            }
        }

    }

    @Override
    protected void initCtrl() {
//          binding.ivHumberger.setOnClickListener((View.OnClickListener) this);
    }


    @Override
    public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
        CommonUtils.dismissLoadingDialog();
        if (response.isSuccessful()) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(SelectRouteActivity.this));
            binding.recyclerView.setAdapter(new RouteAdapter(SelectRouteActivity.this, response.body(), globalvariables));
        } else {
            CommonUtils.showSnackBar(binding.getRoot(), getString(R.string.internal_server_error));
        }
    }

    @Override
    public void onFailure(Call<List<RouteModel>> call, Throwable t) {
        CommonUtils.dismissLoadingDialog();
        CommonUtils.showSnackBar(binding.getRoot(), t.getMessage());
    }

    public void onClick(View v) {
        if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(SelectRouteActivity.this, MainActivity.class));
        }
    }
}