package com.mtech.rsrtcsc.ui.activity.auth.forgot;

import static com.mtech.rsrtcsc.utils.CommonUtils.dismissLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.isOnline;
import static com.mtech.rsrtcsc.utils.CommonUtils.showLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityForgotPasswordBinding;
import com.mtech.rsrtcsc.model.request.ForgotModel;
import com.mtech.rsrtcsc.model.response.AuthModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.MultiTextWatcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding> implements View.OnClickListener, MultiTextWatcher.TextWatcherWithInstance, Callback<List<AuthModel>> {
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();


    @Override
    protected ActivityForgotPasswordBinding getActivityBinding() {
        return ActivityForgotPasswordBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.setData(new ForgotModel(""));
    }

    @Override
    protected void initCtrl() {
        binding.btnForgot.setOnClickListener(this);
        new MultiTextWatcher().registerEditText(binding.tieEmail).setCallback(this);
        binding.ivHumberger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.btnForgot:
            if(checkValidation()) {
                showLoadingDialog(this);
                apiInterface.forgotPassword(binding.getData()).enqueue(this);
            }
            break;

            case R.id.ivHumberger:startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));break;
        }*/

        if (v.getId() == R.id.btnForgot) {
            if (checkValidation()) {
                showLoadingDialog(this);
                apiInterface.forgotPassword(binding.getData()).enqueue(this);
            }
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
        }

    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getMobileNo().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter mobile no");
        } if(binding.getData().getMobileNo().length()!=10){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid mobile no");
        }

        else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        return ret;
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
    /*    switch (editText.getId()){
            case R.id.tieEmail: binding.tilEmail.setErrorEnabled(false); break;
        }*/

        if (editText.getId() == R.id.tieEmail) {
            binding.tilEmail.setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }

    @Override
    public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()) {
         if(response.body().size()>0){
             for(AuthModel model:response.body()){
                 Toast.makeText(ForgotPasswordActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();
                 finish();
                 break;
             }
         } else{
             binding.tilEmail.setErrorEnabled(true);
             binding.tilEmail.setError("Mobile Number is not yet register, try signup");
         }

        }
        else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
    }

    @Override
    public void onFailure(Call<List<AuthModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());

    }
}