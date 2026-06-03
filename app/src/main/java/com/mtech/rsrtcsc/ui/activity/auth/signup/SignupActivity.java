
package com.mtech.rsrtcsc.ui.activity.auth.signup;

import static com.mtech.rsrtcsc.utils.CommonUtils.dismissLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.isOnline;
import static com.mtech.rsrtcsc.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySignupBinding;
import com.mtech.rsrtcsc.model.request.GetOtpModel;
import com.mtech.rsrtcsc.model.request.SignupModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.response.AuthModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;
import com.mtech.rsrtcsc.utils.MultiTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity<ActivitySignupBinding> implements View.OnClickListener, OnCompleteListener<AuthResult>, Callback<AuthModel>, MultiTextWatcher.TextWatcherWithInstance {

    private FirebaseAuth auth;
    private PhoneAuthProvider.ForceResendingToken forceToken;
    private String otp;
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();
    private List<SpinnerDataModel> concession= new ArrayList();
    public String globalvariable;
    public GetOtpModel getOtpModel;


    @Override
    protected ActivitySignupBinding getActivityBinding() {
        return ActivitySignupBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        auth = FirebaseAuth.getInstance();
        binding.setData(new SignupModel("","","","",""));
    }

    @Override
    protected void initCtrl() {
        binding.tvLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnOTP.setOnClickListener(this);

        new MultiTextWatcher().registerEditText(binding.tieName)
                .registerEditText(binding.tieEmail)
                .registerEditText(binding.tiePassword)
                .registerEditText(binding.tieMobileNo)
                .registerEditText(binding.tieOTP).setCallback(this);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback=  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            if(phoneAuthCredential!=null){
                binding.tieOTP.setText(phoneAuthCredential.getSmsCode());
//                signInWithCredential(phoneAuthCredential);
            }else{
                dismissLoadingDialog();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            dismissLoadingDialog();
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError(e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull final String code, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(code, forceResendingToken);
            otp=code;
            forceToken=forceResendingToken;
            dismissLoadingDialog();
            Toast.makeText(SignupActivity.this, "OTP send successfully!", Toast.LENGTH_SHORT).show();
        }
    };

    private void signUpWithOtpVerify() {
        if(isOnline(SignupActivity.this)) {
          apiInterface.signup(binding.getData()).enqueue(new Callback<AuthModel>() {
              @Override
              public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                  if(response.isSuccessful()){
                      Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                      if ("User registered successfully".equalsIgnoreCase(response.body().getResult().trim())) {
                          finish();  // ← Pops the screen
                      }
                  } else{
                      dismissLoadingDialog();
                      showSnackBar(binding.getRoot(), "Error!! try again later");
                  }
              }

              @Override
              public void onFailure(Call<AuthModel> call, Throwable t) {
                  dismissLoadingDialog();
                  showSnackBar(binding.getRoot(), t.getMessage());
              }
          });
        }
        else showSnackBar(binding.getRoot(),"Please turn on internet");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            if (checkValidation()) {
                if(binding.getData().getOtp().trim().length()==0){
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter otp");
            return;
        }
        else if(binding.getData().getOtp().trim().length()!=4){
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter four digit otp");
            return;
        }
                signUpWithOtpVerify();
            }
        } else if (v.getId() == R.id.btnOTP) {

            if(checkValidation()){
                if(isOnline(SignupActivity.this))
                {
                    String email = binding.tieEmail.getText().toString().trim();
                    String mobile = binding.tieMobileNo.getText().toString().trim();

                    getOtpModel = new GetOtpModel(email, mobile);

                    apiInterface.GetOTP(getOtpModel).enqueue(new Callback<AuthModel>() {
                        @Override
                        public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(SignupActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                dismissLoadingDialog();
                                showSnackBar(binding.getRoot(), "Error!! try again later");
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthModel> call, Throwable t) {
                            dismissLoadingDialog();
                            showSnackBar(binding.getRoot(), t.getMessage());
                        }
                    });
                }
                /*{
                    apiInterface.signup(binding.getData()).enqueue(new Callback<AuthModel>() {
                        @Override
                        public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
//                                if(Objects.equals(response.body().getResult(), "OTP sent Successfully")){
//                                    otpVerifyModel.setEmail(binding.tieEmail.getText().toString().trim());
//                                }
                            } else {
                                dismissLoadingDialog();
                                showSnackBar(binding.getRoot(), "Error!! try again later");
//                                binding.tilEmail.setErrorEnabled(true);
//                                binding.tilEmail.setError("Error!! try again later");
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthModel> call, Throwable t) {
                            dismissLoadingDialog();
                            showSnackBar(binding.getRoot(), t.getMessage());
                        }
                    });
                }*/
                else showSnackBar(binding.getRoot(),"Please turn on internet");
            }
        } else if (v.getId() == R.id.tvLogin) {
            finish();
        }
    }


    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getUserName().trim().length()==0){
            ret=false;
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Please enter name");
        }
        else if(binding.getData().getEmailId().trim().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter email id");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailId()).matches()){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid email id");
        }
        else if(binding.getData().getPassword().trim().length()==0){
            ret=false;
            binding.tilPassword.setErrorEnabled(true);
            binding.tilPassword.setError("Please enter password");
        }
        else if(binding.getData().getMobileNo().trim().length()==0){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile number");
        }
        else if(binding.getData().getMobileNo().trim().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile number");
        }
        else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }


  /*      else if(binding.getData().getOtp().trim().length()==0){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter otp");
        }
        else if(binding.getData().getOtp().trim().length()!=4){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter four digit otp");
        }*/


        /*else if(otp==null){
            ret=false;
            showSnackBar(binding.getRoot(),"Please click on send otp button");
        }*/


        return ret;
    }

    private void sendOTP() {
        auth.setLanguageCode("en");
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+binding.tieMobileNo.getText().toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callback);

        if(forceToken!=null) options.setForceResendingToken(forceToken);
        PhoneAuthProvider.verifyPhoneNumber(options.build());
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            if(isOnline(SignupActivity.this)) apiInterface.signup(binding.getData()).enqueue(this);
            else showSnackBar(binding.getRoot(),"Please turn on internet");
        }else{
            dismissLoadingDialog();
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError(task.getException().getMessage());
        }
    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        if(response.isSuccessful()){
            if(isOnline(SignupActivity.this)){
                if(response.body().getResult().equals("Your Registration SuccessFull.")) {
                    Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }
                else{
                    // dismissLoadingDialog();
                    binding.tilMobileNo.setErrorEnabled(true);
                    binding.tilMobileNo.setError(response.body().getResult());

                }
            }
            else showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        if (editText.getId() == R.id.tieName) {
            binding.tilName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieEmail) {
            binding.tilEmail.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tiePassword) {
            binding.tilPassword.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieMobileNo) {
            binding.tilMobileNo.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieOTP) {
            binding.tilOTP.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }
}










/*

package com.mtech.rsrtcsc.ui.activity.auth.signup;

import static com.mtech.rsrtcsc.utils.CommonUtils.dismissLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.isOnline;
import static com.mtech.rsrtcsc.utils.CommonUtils.showLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySignupBinding;
import com.mtech.rsrtcsc.model.request.LoginModel;
import com.mtech.rsrtcsc.model.request.SignupModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.response.AuthModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;
import com.mtech.rsrtcsc.utils.MultiTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity<ActivitySignupBinding> implements View.OnClickListener, OnCompleteListener<AuthResult>, Callback<AuthModel>, MultiTextWatcher.TextWatcherWithInstance {

    private FirebaseAuth auth;
    private PhoneAuthProvider.ForceResendingToken forceToken;
    private String otp;
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();
    private List<SpinnerDataModel> concession= new ArrayList();
    public String globalvariable;


    @Override
    protected ActivitySignupBinding getActivityBinding() {
        return ActivitySignupBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        auth = FirebaseAuth.getInstance();
        binding.setData(new SignupModel("","","","",""));
    }

    @Override
    protected void initCtrl() {
        binding.tvLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnOTP.setOnClickListener(this);

        new MultiTextWatcher().registerEditText(binding.tieName)
                .registerEditText(binding.tieEmail)
                .registerEditText(binding.tiePassword)
                .registerEditText(binding.tieMobileNo)
                .registerEditText(binding.tieOTP).setCallback(this);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback=  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            if(phoneAuthCredential!=null){
                binding.tieOTP.setText(phoneAuthCredential.getSmsCode());
                signInWithCredential(phoneAuthCredential);
            }else{
                dismissLoadingDialog();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            dismissLoadingDialog();
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError(e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull final String code, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(code, forceResendingToken);
            otp=code;
            forceToken=forceResendingToken;
            dismissLoadingDialog();
            Toast.makeText(SignupActivity.this, "OTP send successfully!", Toast.LENGTH_SHORT).show();
        }
    };

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            if (checkValidation()) {
                // showLoadingDialog(SignupActivity.this);
                signInWithCredential(PhoneAuthProvider.getCredential(otp, binding.getData().getOtp()));
            }
        } else if (v.getId() == R.id.btnOTP) {
            if (binding.getData().getMobileNo().trim().length() == 0) {
                showSnackBar(binding.getRoot(), "Please enter mobile number");
            } else if (binding.getData().getMobileNo().trim().length() != 10) {
                showSnackBar(binding.getRoot(), "Please enter valid mobile number");
            } else {
                showLoadingDialog(this);
                apiInterface.login(new LoginModel(binding.getData().getMobileNo(), "")).enqueue(new Callback<List<AuthModel>>() {
                    @Override
                    public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
                        if (response.isSuccessful()) {
                            boolean ret = true;
                            if (response.body().size() > 0) {
                                for (int i = 0; i < response.body().size(); i++) {
                                    if (binding.getData().getMobileNo().equals(response.body().get(i).getMobileNo())) {
                                        ret = false;
                                        break;
                                    }
                                }
                            }
                            if (ret) {
                                // Google play integrity check code
                                long projectNumber = 881924936947L;
                                try {
                                    // Create the IntegrityManager instance
                                    IntegrityManager integrityManager = IntegrityManagerFactory.create(SignupActivity.this);

                                    // Build the IntegrityTokenRequest
                                    IntegrityTokenRequest integrityTokenRequest = IntegrityTokenRequest.builder()
                                            .setCloudProjectNumber(projectNumber) // Make sure this is correct
                                            .build();

                                    // Request the integrity token
                                    integrityManager.requestIntegrityToken(integrityTokenRequest)
                                            .addOnSuccessListener(integrityTokenResponse -> {
                                                // Handle successful token request
                                                String integrityToken = integrityTokenResponse.token();
                                                // Send this token to your backend for validation
                                                sendOTP();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle the error
                                                Log.e("IntegrityCheck", "Error fetching integrity token: " + e.getMessage());
                                            });

                                } catch (Exception e) {
                                    // Catch any unexpected exceptions
                                    Log.e("IntegrityCheck", "Exception building IntegrityTokenRequest: " + e.getMessage());
                                }


                                sendOTP();

                            } else {
                                dismissLoadingDialog();
                                binding.tilMobileNo.setErrorEnabled(true);
                                binding.tilMobileNo.setError("This Mobile No is already registered with our system, please try with any other");
                            }
                        } else {
                            dismissLoadingDialog();
                            showSnackBar(binding.getRoot(), getString(R.string.internal_server_error));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AuthModel>> call, Throwable t) {
                        dismissLoadingDialog();
                        showSnackBar(binding.getRoot(), t.getMessage());
                    }
                });
            }
        } else if (v.getId() == R.id.tvLogin) {
            finish();
        }
    }


    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getUserName().trim().length()==0){
            ret=false;
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Please enter name");
        }
        else if(binding.getData().getEmailId().trim().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter email id");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailId()).matches()){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid email id");
        }
        else if(binding.getData().getPassword().trim().length()==0){
            ret=false;
            binding.tilPassword.setErrorEnabled(true);
            binding.tilPassword.setError("Please enter password");
        }
        else if(binding.getData().getMobileNo().trim().length()==0){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile no");
        }
        else if(binding.getData().getMobileNo().trim().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile no");
        }
        else if(binding.getData().getOtp().trim().length()==0){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter otp");
        }
        else if(binding.getData().getOtp().trim().length()!=6){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter six digit otp");
        }else if(otp==null){
            ret=false;
            showSnackBar(binding.getRoot(),"Please click on send otp button");
        }else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        return ret;
    }

    private void sendOTP() {
        auth.setLanguageCode("en");
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+binding.tieMobileNo.getText().toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callback);

        if(forceToken!=null) options.setForceResendingToken(forceToken);
        PhoneAuthProvider.verifyPhoneNumber(options.build());
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            if(isOnline(SignupActivity.this)) apiInterface.signup(binding.getData()).enqueue(this);
            else showSnackBar(binding.getRoot(),"Please turn on internet");
        }else{
            dismissLoadingDialog();
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError(task.getException().getMessage());
        }
    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        if(response.isSuccessful()){
            if(isOnline(SignupActivity.this)){
                if(response.body().getResult().equals("Your Registration SuccessFull.")) {
                    Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }
                else{
                    // dismissLoadingDialog();
                    binding.tilMobileNo.setErrorEnabled(true);
                    binding.tilMobileNo.setError(response.body().getResult());

                }
            }
            else showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        if (editText.getId() == R.id.tieName) {
            binding.tilName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieEmail) {
            binding.tilEmail.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tiePassword) {
            binding.tilPassword.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieMobileNo) {
            binding.tilMobileNo.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieOTP) {
            binding.tilOTP.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }
}
*/
















/*

package com.mtech.rsrtcsc.ui.activity.auth.signup;

//*
// * SignupActivity that uses e‑mail OTP (SMTP) instead of Firebase phone‑auth.
// *
// * Add these dependencies to your module build.gradle:
// * implementation 'com.sun.mail:android-mail:1.6.7'
// * implementation 'com.sun.mail:android-activation:1.6.7'
// *
// * Replace the SMTP_* constants with your own credentials.



import static com.mtech.rsrtcsc.utils.CommonUtils.dismissLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.isOnline;
import static com.mtech.rsrtcsc.utils.CommonUtils.showLoadingDialog;
import static com.mtech.rsrtcsc.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivitySignupBinding;
import com.mtech.rsrtcsc.model.request.LoginModel;
import com.mtech.rsrtcsc.model.request.SignupModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.response.AuthModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;
import com.mtech.rsrtcsc.utils.MultiTextWatcher;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity<ActivitySignupBinding> implements
        View.OnClickListener, Callback<AuthModel>, MultiTextWatcher.TextWatcherWithInstance {

    // === SMTP SETTINGS – FILL WITH YOUR OWN CREDENTIALS ===
    private static final String SMTP_HOST = "smtp.gmail.com"; // or your mail server
    private static final String SMTP_PORT = "587";
    private static  String SMTP_USERNAME = "mtechattendence@gmail.com"; // sender e‑mail
    private static final String SMTP_PASSWORD = "hjle ldkz vymh xgmg";          // app‑specific password / token
    private static final String EMAIL_SUBJECT = "Your RSRTC Sign-up verification code";

    // === OTP ===
    private static final long   OTP_EXPIRY_MILLIS = TimeUnit.MINUTES.toMillis(10);
    private String generatedOtp;
    private long   otpGeneratedAt;

    // === API ===
    private final RSRTCInterface apiInterface      = new RSRTCConnection().createService();
    private final RSRTCInterface apiInterfaceRSTC  = new RSRTCConnection().createService();
    private final RSRTCInterface smsApiInterface   = new RSRTCConnection().createSMSService(); // kept for future use

    private final List<SpinnerDataModel> concession = new ArrayList<>();

    private final ExecutorService mailExecutor = Executors.newSingleThreadExecutor();
    private final Handler          mainHandler  = new Handler(Looper.getMainLooper());

    @Override
    protected ActivitySignupBinding getActivityBinding() {
        return ActivitySignupBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.setData(new SignupModel("", "", "", "", ""));
    }

    @Override
    protected void initCtrl() {
        binding.tvLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnOTP.setOnClickListener(this);

        new MultiTextWatcher().registerEditText(binding.tieName)
                .registerEditText(binding.tieEmail)
                .registerEditText(binding.tiePassword)
                .registerEditText(binding.tieMobileNo)
                .registerEditText(binding.tieOTP)
                .setCallback(this);
    }

    // ===================== BUTTON HANDLERS ===================== //

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRegister) {
            if (checkValidation()) {
                verifyAndRegister();
            }
        } else if (id == R.id.btnOTP) {
            if (validateEmailForOtp()) {
                checkExistingUserAndSendOtp();
            }
        } else if (id == R.id.tvLogin) {
            finish();
        }
    }

    private boolean validateEmailForOtp() {
        String email = binding.getData().getEmailId().trim();
        if (TextUtils.isEmpty(email)) {
            showSnackBar(binding.getRoot(), "Please enter email id");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showSnackBar(binding.getRoot(), "Please enter valid email id");
            return false;
        }
        return true;
    }

    private void checkExistingUserAndSendOtp() {
        showLoadingDialog(this);
        String enteredMobile = binding.getData().getMobileNo();
        String enteredEmail = binding.getData().getEmailId();

        apiInterface.login(new LoginModel(enteredMobile, "")).enqueue(new Callback<List<AuthModel>>() {
            @Override
            public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
                if (response.isSuccessful()) {
                    System.out.println("login response body: " + response.body());
                    boolean isMobileNew = true;
                    boolean isEmailNew = true;

                    if (response.body() != null && !response.body().isEmpty()) {
                        for (AuthModel model : response.body()) {
                            System.out.println("login response body mobile no: " + model.getMobileNo());
                            System.out.println("login response body email id: " + model.getEmailId());
                            if (enteredMobile.equals(model.getMobileNo())) {
                                System.out.println("login response body mobile no: " + model.getMobileNo());
                                isMobileNew = false;
                            }
                            if (enteredEmail.equals(model.getEmailId())) {
                                System.out.println("login response body email id: " + model.getEmailId());
                                isEmailNew = false;
                            }
                        }
                    }

                    if (!isMobileNew) {
                        dismissLoadingDialog();
                        binding.tilMobileNo.setErrorEnabled(true);
                        binding.tilMobileNo.setError("This mobile number is already registered.");
                    } else if (!isEmailNew) {
                        dismissLoadingDialog();
                        binding.tilEmail.setErrorEnabled(true);
                        binding.tilEmail.setError("This email address is already registered.");
                    } else {
                        sendOtpEmail(); // ✅ both mobile & email are unique
                    }

                } else {
                    dismissLoadingDialog();
                    showSnackBar(binding.getRoot(), getString(R.string.internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<List<AuthModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(), t.getMessage());
            }
        });
    }


*/
/*    private void checkExistingUserAndSendOtp() {
        showLoadingDialog(this);
        apiInterface.login(new LoginModel(binding.getData().getMobileNo(), "")).enqueue(new Callback<List<AuthModel>>() {
            @Override
            public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
                if (response.isSuccessful()) {
                    System.out.println("login response body: "+response.body());
                    boolean isNewUser = true;
                    if (response.body() != null && !response.body().isEmpty()) {
                        for (AuthModel model : response.body()) {
                            if (binding.getData().getMobileNo().equals(model.getMobileNo())) {
                                isNewUser = false;
                                break;
                            }
                        }
                    }
                    if (isNewUser) {
                        sendOtpEmail();
                    } else {
                        dismissLoadingDialog();
                        binding.tilMobileNo.setErrorEnabled(true);
                        binding.tilMobileNo.setError("This Mobile No is already registered with our system, please try with any other");
                    }
                } else {
                    dismissLoadingDialog();
                    showSnackBar(binding.getRoot(), getString(R.string.internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<List<AuthModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(), t.getMessage());
            }
        });
    }*//*


    // ===================== OTP LOGIC ===================== //

    private void sendOtpEmail() {
        generatedOtp   = generateSixDigitOtp();
        otpGeneratedAt = System.currentTimeMillis();

        final String emailBody = "Your one‑time verification code is " + generatedOtp +
                "\nThis code will expire in 5 minutes.";

        mailExecutor.execute(() -> {
            try {
                MailSender.sendMail(SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD,
                        EMAIL_SUBJECT, emailBody, binding.getData().getEmailId());
                mainHandler.post(() -> {
                    dismissLoadingDialog();
                    Toast.makeText(SignupActivity.this, "OTP sent successfully!", Toast.LENGTH_SHORT).show();
                });
            } catch (final Exception ex) {
                mainHandler.post(() -> {
                    dismissLoadingDialog();
                    showSnackBar(binding.getRoot(), "Failed to send OTP: " + ex.getMessage());
                });
            }
        });
    }

    private String generateSixDigitOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

    // ===================== REGISTRATION ===================== //

    private void verifyAndRegister() {
        String enteredOtp = binding.getData().getOtp().trim();
        if (generatedOtp == null) {
            showSnackBar(binding.getRoot(), "Please click on send otp button");
            return;
        }
        if (!generatedOtp.equals(enteredOtp)) {
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Incorrect OTP");
            return;
        }
        if (System.currentTimeMillis() - otpGeneratedAt > OTP_EXPIRY_MILLIS) {
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("OTP expired, please request a new one");
            return;
        }

        if (!isOnline(this)) {
            showSnackBar(binding.getRoot(), "Please turn on internet");
            return;
        }

        // All good – call signup API
        showLoadingDialog(this);
        apiInterface.signup(binding.getData()).enqueue(this);
    }

    // ===================== VALIDATION ===================== //

    private boolean checkValidation() {
        boolean ret = true;
        if (binding.getData().getUserName().trim().isEmpty()) {
            ret = false;
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Please enter name");
        } else if (binding.getData().getEmailId().trim().isEmpty()) {
            ret = false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter email id");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailId()).matches()) {
            ret = false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid email id");
        } else if (binding.getData().getPassword().trim().isEmpty()) {
            ret = false;
            binding.tilPassword.setErrorEnabled(true);
            binding.tilPassword.setError("Please enter password");
        } else if (binding.getData().getMobileNo().trim().isEmpty()) {
            ret = false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile no");
        } else if (binding.getData().getMobileNo().trim().length() != 10) {
            ret = false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile no");
        } else if (binding.getData().getOtp().trim().isEmpty()) {
            ret = false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter otp");
        } else if (binding.getData().getOtp().trim().length() != 6) {
            ret = false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter six digit otp");
        }
        return ret;
    }

    // ===================== API CALLBACKS ===================== //

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        dismissLoadingDialog();
        if (response.isSuccessful() && response.body() != null) {
            if ("Your Registration SuccessFull.".equals(response.body().getResult())) {
                Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            } else {
                binding.tilMobileNo.setErrorEnabled(true);
                binding.tilMobileNo.setError(response.body().getResult());
            }
        } else {
            showSnackBar(binding.getRoot(), getString(R.string.internal_server_error));
        }
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(), t.getMessage());
    }

    // ===================== TEXT WATCHER ===================== //

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        if (editText.getId() == R.id.tieName) {
            binding.tilName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieEmail) {
            binding.tilEmail.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tiePassword) {
            binding.tilPassword.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieMobileNo) {
            binding.tilMobileNo.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieOTP) {
            binding.tilOTP.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) { }

    // ===================== MailSender ===================== //
*/
/**
     * Lightweight SMTP helper using JavaMail*//*



    private static class MailSender {
        static void sendMail(String host, String port, String user, String pass,
                             String subject, String body, String recipient) throws Exception {

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        }
    }
}

*/
