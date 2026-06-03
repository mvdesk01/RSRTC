package com.mtech.rsrtcsc.ui.activity.cardstatuss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.capture.UploadDocumentActivity;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rpf_VirtualCard extends AppCompatActivity {

    TextView Id,EmpId,Name,Post,Location,ConType,Valid;
    ImageView img,vcimg;
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private List<SpinnerDataModel> concession= new ArrayList();
    public String globalvariable;
    String byteimg;
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpf_virtual_card);

        Id = findViewById(R.id.rpf_Id_No);
        EmpId = findViewById(R.id.rpf_emp_id);
        Name = findViewById(R.id.rpf_Name);
        Post = findViewById(R.id.rpf_post);
        Location = findViewById(R.id.rpf_location);
        ConType = findViewById(R.id.rpf_ConType);
        Valid  = findViewById(R.id.rpf_validDate);
        img = findViewById(R.id.ivHumberger);
        vcimg=findViewById(R.id.vc_profile_img);

        name();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Rpf_VirtualCard.this, MainActivity.class));

            }
        });


    }

    private void imagesize(){
        byte[] imageAsBytes = Base64.decode(byteimg.getBytes(), Base64.DEFAULT);
        bitmapImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        bitmapImage= UploadDocumentActivity.resizeImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length),300,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
        vcimg.setImageBitmap(bitmapImage);
    }


    private void name() {
        apiInterfaceRSTC.GetVirtualCard(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(Rpf_VirtualCard.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        String con_type = model.getConcessionName();
                        Name.setText(model.getcName());
                        ConType.setText(con_type);
                        Id.setText(model.getCard_Auto_ID());
                        Valid.setText(model.getExpiryDate());
                        EmpId.setText(model.getPoliceEmployeeID());
                        Post.setText(model.getPolicePost());
                        Location.setText(model.getPoliceLocation());
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
}