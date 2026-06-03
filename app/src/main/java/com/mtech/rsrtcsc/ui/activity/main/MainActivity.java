package com.mtech.rsrtcsc.ui.activity.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.mtech.rsrtcsc.ContactUs;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityMainBinding;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.auth.login.LoginActivity;
import com.mtech.rsrtcsc.ui.activity.capture.UploadDocumentActivity;
import com.mtech.rsrtcsc.ui.activity.cardstatuss.CardStatusView;
import com.mtech.rsrtcsc.ui.activity.cardstatuss.VirtualCard;
import com.mtech.rsrtcsc.ui.activity.drawer.Recharge.RechargeCardActivity;
import com.mtech.rsrtcsc.ui.activity.drawer.report.ReportActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    public String globalvariable;
    private String globalvariable1;
    private List<SpinnerDataModel> concession= new ArrayList();
    private static final int PICK_IMAGE_REQUEST = 100;
    private final String TAG = "Main Activity";
    private Uri mImageUri;
    String byteimg;
    private Bitmap bitmapImage;


    @Override
    protected ActivityMainBinding getActivityBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.clDrawer.tvusername.setText(PrefrenceHelper.getPrefrenceStringValue(MainActivity.this, PrefrenceKeyConstant.FULL_NAME).toUpperCase(Locale.ROOT));
        ConcessionType();

    }

    @Override
    protected void initCtrl() {
        binding.ivHumberger.setOnClickListener(this);
        binding.clDrawer.tvprofile.setOnClickListener(this);
        binding.clDrawer.tvsmartcard.setOnClickListener(this);
        binding.clDrawer.tvpolicesmartcard.setOnClickListener(this);
        binding.clDrawer.tvvirtualcard.setOnClickListener(this);
        binding.clDrawer.tvRechargeCard.setOnClickListener(this);
        binding.clDrawer.tvReport.setOnClickListener(this);
        binding.clDrawer.tvStatus.setOnClickListener(this);
        binding.clDrawer.tvLogout.setOnClickListener(this);
//        binding.clDrawer.tvprofileImage.setOnClickListener(this);
        binding.clDrawer.tvcontact.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvcontact) {
            startActivity(new Intent(MainActivity.this, ContactUs.class));
        } else if (v.getId() == R.id.ivHumberger) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tvprofile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        } else if (v.getId() == R.id.tvsmartcard) {
            startNewActivity(HomeScreen.class);
        } else if (v.getId() == R.id.tvpolicesmartcard) {
            startActivity(new Intent(MainActivity.this, Police_Home.class));
        } else if (v.getId() == R.id.tvvirtualcard) {
            if (globalvariable == null) {
                Toast.makeText(MainActivity.this, "You have not applied for SMART CARD! (Apply Smart Card)", Toast.LENGTH_LONG).show();
            } else {
                if (globalvariable.equals("RPF") || globalvariable.equals("DPF")) {
                    // startNewActivity(Rpf_VirtualCard.class);
                    startNewActivity(VirtualCard.class);
                } else {
                    startNewActivity(VirtualCard.class);
                }
            }
        } else if (v.getId() == R.id.tvRechargeCard) {
            startNewActivity(RechargeCardActivity.class);
        } else if (v.getId() == R.id.tvReport) {
            if (globalvariable == null) {
                Toast.makeText(MainActivity.this, "You have not applied for SMART CARD! (Apply Smart Card)", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            }
        } else if (v.getId() == R.id.tvStatus) {
            if (globalvariable == null) {
                Toast.makeText(MainActivity.this, "You have not applied for SMART CARD! (Apply Smart Card)", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(MainActivity.this, CardStatusView.class));
            }
        }else if (v.getId() == R.id.tvRecharge) {
            if (globalvariable == null) {
                Toast.makeText(MainActivity.this, "You have not applied for SMART CARD! (Apply Smart Card)", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(MainActivity.this, RechargeCardActivity.class));
            }
        }
        else if (v.getId() == R.id.tvLogout) {
            onBackPressed();
        }

    }

    private void imagesize(){
        byte[] imageAsBytes = Base64.decode(byteimg.getBytes(), Base64.DEFAULT);
        bitmapImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        bitmapImage= UploadDocumentActivity.resizeImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length),300,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
        binding.clDrawer.tvprofileImage.setImageBitmap(bitmapImage);
    }

    private void name() {
        apiInterfaceRSTC.GetVirtualCard(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(MainActivity.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        byteimg = model.getApplicantPhoto();
                            imagesize();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

            }
        });
    }


    private void ConcessionType() {
        apiInterfaceRSTC.GetCardStatusAPP(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(MainActivity.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        globalvariable = model.getConcessionCode();
                        if(!(globalvariable == null)){
                            name();
                        } else{
                            Toast.makeText(MainActivity.this, "Your have not applied for SMART CARD! (Apply Smard Card)", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void startNewActivity(Class className) {
        startActivity(new Intent(this,className));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isPermissionDenied=false;
        for(int i=0;i<grantResults.length;i++)
        {
            if(!(grantResults[i]==PackageManager.PERMISSION_GRANTED))
            {isPermissionDenied=true;
                break;
            }
        }
        if(!isPermissionDenied)
        {
            switch (requestCode){


            }
        }else{
            CommonUtils.showSnackBar(binding.getRoot(),"Please allow permission");
        }
    }


    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


}