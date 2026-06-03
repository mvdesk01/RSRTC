package com.mtech.rsrtcsc.ui.activity.cardstatuss;

import static com.mtech.rsrtcsc.ui.activity.capture.UploadDocumentActivity.resizeImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.CustomLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VirtualCard extends AppCompatActivity {

    protected ViewDataBinding binding;
    ImageView vcimg;
    TextView vcname;
    TextView vccontype;
    TextView vcidno;
    TextView valid, vcFromStop, vcToStop, vcFrom, vcTo, vcEmpId;
    LinearLayout layoutEmpId;
    ImageView img;
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private List<SpinnerDataModel> concession= new ArrayList();
    private List<SpinnerDataModel> cardstatus= new ArrayList();
    public String globalvariable;
    String byteimg;
    private Bitmap bitmapImage;
    private boolean isTimeout = false;
    private Call<List<SpinnerDataModel>> cardCall;
    CardStatusModel cardStatusModel;


//       String frStop = ((EditText) findViewById(R.id.tv_from_stop)).getText().toString();
//       String endStop = ((EditText) findViewById(R.id.tv_till_stop)).getText().toString();

    @SuppressLint({"WrongThread", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_card);

        vcimg=findViewById(R.id.vc_profile_img);
        vcname = findViewById(R.id.vc_Name);
        vccontype = findViewById(R.id.vc_ConType);
        vcidno = findViewById(R.id.vc_Id_No);
        valid = findViewById(R.id.vc_validDate);
        img = findViewById(R.id.ivHumberger);
        vcFromStop = findViewById(R.id.vcFromStop);
        vcToStop = findViewById(R.id.vcToStop);
        vcFrom = findViewById(R.id.vcFrom);
        vcTo = findViewById(R.id.vcTo);
        layoutEmpId = findViewById(R.id.ll_EmpId);
        vcEmpId = findViewById(R.id.vc_EmpID);
        showLoadingDialog(this);
        name();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VirtualCard.this, MainActivity.class));
            }
        });

        if(vcTo.getText().toString().length() < 5){
            vcTo.setPadding(0,0,34,0);
        }
    }

    public static void showLoadingDialog(Activity activity){
        if(CommonUtils.customProgressBar==null)
            CommonUtils.customProgressBar = CustomLoader.show(activity, true);

        try {
            CommonUtils.customProgressBar.setCancelable(false);
            CommonUtils.customProgressBar.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
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

    private void imagesize(){
        byte[] imageAsBytes = Base64.decode(byteimg.getBytes(), Base64.DEFAULT);
        bitmapImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        bitmapImage= resizeImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length),300,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
        vcimg.setImageBitmap(bitmapImage);
    }

/*    private void name() {

        showLoadingDialog(this);

        // Start 10 sec timeout timer
        new android.os.Handler().postDelayed(() -> {
            if (!isTimeout) {
                isTimeout = true;

                if (cardCall != null && !cardCall.isCanceled()) {
                    cardCall.cancel(); // cancel network call
                }

                dismissLoadingDialog();

                showRetrySnackBar();
            }
        }, 20000); // 10 sec

         cardStatusModel = new CardStatusModel("9669060675");
         try{
             apiInterfaceRSTC.GetVirtualCard(cardStatusModel).enqueue(new Callback<List<SpinnerDataModel>>() {
                 @Override
                 public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                     Toast.makeText(VirtualCard.this, "response successfull", Toast.LENGTH_SHORT).show();
                 }

                 @Override
                 public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                     Log.e("API_ERROR", "Error = " + t.getMessage(), t);
                 }
             });

         } catch (RuntimeException e) {
             throw new RuntimeException(e);
         } catch (Exception e){
             Log.d("API_DEBUG", "Error: "+e);
         }
        dismissLoadingDialog();

       *//* cardCall = apiInterfaceRSTC.GetVirtualCard(cardStatusModel);

        cardCall.enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {

                if(response.isSuccessful()){
                    Toast.makeText(VirtualCard.this, "response successfull", Toast.LENGTH_SHORT).show();
                }
//                if (isTimeout) return; // Ignore response after timeout
//
//                dismissLoadingDialog();
//
//                if (response.isSuccessful()) {
//                    List<SpinnerDataModel> list = response.body();
//
//                    if (list == null || list.isEmpty()) {
//                        showRetrySnackBar();
//                        return;
//                    }
//
//                    for (SpinnerDataModel model : list) {
//                        vcname.setText(model.getcName());
//                        vccontype.setText(model.getConcessionName());
//                        vcidno.setText(model.getCard_Auto_ID());
//                        valid.setText(model.getExpiryDate());
//                        byteimg = model.getApplicantPhoto();
//
//                        validat(model);
//                        imagesize();
//                        generateQrCode(model);
//                    }
//                } else {
//                    showRetrySnackBar();
//                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                Log.e("API_ERROR", "Error = " + t.getMessage(), t);

                if (isTimeout) return;

                dismissLoadingDialog();
                showRetrySnackBar();
            }
        });*//*
}*/


    private void name() {
        Log.d("API_DEBUG", "Calling GetVirtualCard API...");
        try{
            apiInterfaceRSTC.GetVirtualCard(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(VirtualCard.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {

                @Override
                public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                    dismissLoadingDialog();
                    if(response.isSuccessful()){
                        concession = response.body();
                        List<String> listpass=new ArrayList<>();
                        assert response.body() != null;
                        for(SpinnerDataModel model : response.body()){
                            vcname.setText(model.getcName());
                            vccontype.setText(model.getConcessionName().strip());
                            vcidno.setText(model.getCard_Auto_ID());
                            valid.setText(model.getExpiryDate());
                            byteimg = model.getApplicantPhoto();
                            Log.d("TAG", "fromStop"+model.getFromStop());
                            validat(model);
                            imagesize();
                            generateQrCode(model);
                            if(model.getConcessionName().length() > 10) {
                                vccontype.setPadding(25,0,0,0);
                            } else {
                                vccontype.setPadding(90,0,0,0);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

                }
            });

        } catch(Exception e){
            Log.d("API_DEBUG", "Error: "+e);
        }
    }

    private void showRetrySnackBar() {
        Snackbar.make(findViewById(android.R.id.content),
                        "Request timed out. Try again?",
                        Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> {
                    isTimeout = false;
                    name(); // retry API
                })
                .show();
    }


    private void generateQrCode(SpinnerDataModel content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
           if(Objects.equals(content.getConcessionName(), "STUDENT PASSENGER")){
               BitMatrix bitMatrix = writer.encode(    content.getcName()+
                               "\nID: "+content.getCard_Auto_ID()+
                               "\n"+content.getConcessionName()+
                               "\nExpiry date: "+content.getExpiryDate()+
                               " Destination: "+content.getFromStop()+ " to "+ content.getTillStop()
                       , BarcodeFormat.QR_CODE, 512, 512);
               int width = bitMatrix.getWidth();
               int height = bitMatrix.getHeight();
               Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
               for (int x = 0; x < width; x++) {
                   for (int y = 0; y < height; y++) {
                       bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                   }
               }
               ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
           }
           else if (Objects.equals(content.getConcessionName(), "RAJASTHAN POLICE")) {
               BitMatrix bitMatrix = writer.encode(    content.getcName()
                               +  "\nID: "+content.getCard_Auto_ID()
                               +  "\n"+content.getConcessionName()
                               +  "\nExpiry date: "+content.getExpiryDate()
                               +  "\nPost: "+content.getPolicePost()
                               +  "\nLocation: "+content.getPoliceLocation()
                               +  " EmpID: "+content.getPoliceEmployeeID(),
                       BarcodeFormat.QR_CODE, 512, 512);
               int width = bitMatrix.getWidth();
               int height = bitMatrix.getHeight();
               Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
               for (int x = 0; x < width; x++) {
                   for (int y = 0; y < height; y++) {
                       bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                   }
               }
               ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
           } else{
               String cardId = content.getCardAutoID();
               String  expriryDate = content.getExpiryDate();
               String concessionName = content.getConcessionName();
               String name = content.getcName();
               BitMatrix bitMatrix = writer.encode(    name+"\nId: "+cardId+"\n"+concessionName+"\nExpiry: "+expriryDate,
                               BarcodeFormat.QR_CODE, 512, 512);
               int width = bitMatrix.getWidth();
               int height = bitMatrix.getHeight();
               Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
               for (int x = 0; x < width; x++) {
                   for (int y = 0; y < height; y++) {
                       bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                   }
               }
               ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
           }

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void validat(SpinnerDataModel model){
        if(Objects.equals(model.getConcessionName(), "STUDENT PASSENGER")){
//            setContentView(((ViewDataBinding) binding).getRoot());
//            vcFromStop.setVisibility(View.VISIBLE);
//            vcToStop.setVisibility(View.VISIBLE);
//            vcFrom.setVisibility(View.VISIBLE);
//            vcTo.setVisibility(View.VISIBLE);
            layoutEmpId.setVisibility(View.GONE);


            vcFromStop.setText(model.getFromStop());
            vcToStop.setText(model.getTillStop());
            Log.d("Tag","fromStop"+model.getFromStop());
            Log.d("TAG","toStop"+model.getTillStop());
        }
        else if(Objects.equals(model.getConcessionName(), "RAJASTHAN POLICE")){

//            vcFromStop.setVisibility(View.VISIBLE);
//            vcToStop.setVisibility(View.VISIBLE);
//            vcFrom.setVisibility(View.VISIBLE);
//            vcTo.setVisibility(View.VISIBLE);
//            layoutEmpId.setVisibility(View.VISIBLE);

            vcFrom.setText("Post: ");
            vcTo.setText("Location: ");
            vcFromStop.setText(model.getPolicePost());
            vcToStop.setText(model.getPoliceLocation());
            vcEmpId.setText(model.getPoliceEmployeeID());
        }
        else{
            layoutEmpId.setVisibility(View.GONE);
            vcFromStop.setVisibility(View.GONE);
            vcToStop.setVisibility(View.GONE);
            vcFrom.setVisibility(View.GONE);
            vcTo.setVisibility(View.GONE);
        }
    }
}



/*
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validat(String type) {
        concode = type;
        switch (type){
            case "STUDENT PASSENGER":
                binding.llSelectStop.setVisibility(View.VISIBLE);
                binding.tvFromStop.getText().clear();
                binding.tvTillStop.getText().clear();
                onDateSet();
                binding.btnNext.setVisibility(View.GONE);
                break;
            case "VARISHTHA NAGRIK":
                getAgeDifference();
                break;
            case "VARISHTHA NAGRIK  (80 YRS AND ABOVE)":
                getAgeDifference80();
                break;

            default:
                binding.llSelectStop.setVisibility(View.GONE);
                binding.llPosition.setVisibility(View.GONE);
                break;
        }*/
