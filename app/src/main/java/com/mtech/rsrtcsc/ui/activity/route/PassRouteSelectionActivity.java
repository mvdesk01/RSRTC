package com.mtech.rsrtcsc.ui.activity.route;


import android.content.Intent;
import android.view.View;

import com.mtech.rsrtcsc.ui.activity.concession.ConcessionDetailActivity;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityPassRouteSelectionBinding;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

public class PassRouteSelectionActivity extends BaseActivity<ActivityPassRouteSelectionBinding> implements View.OnClickListener {

    @Override
    protected ActivityPassRouteSelectionBinding getActivityBinding() {
        return ActivityPassRouteSelectionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        RegisterationDataHelper.getInstance().getApplicationData().setPassType("FIXED PASSES");
    }

    @Override
    protected void initCtrl() {
        binding.fixedPass.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PassRouteSelectionActivity.this, ConcessionDetailActivity.class);
        startActivity(intent);
        finish();
    }
}