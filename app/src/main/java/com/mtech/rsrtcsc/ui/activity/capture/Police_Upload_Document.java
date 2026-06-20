package com.mtech.rsrtcsc.ui.activity.capture;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityPoliceUploadDocumentBinding;
import com.mtech.rsrtcsc.model.request.PoliceApplicationModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.request.SpinnerRequestModel;
import com.mtech.rsrtcsc.model.response.RegistrationModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.ImageUtil;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Police_Upload_Document extends BaseActivity<ActivityPoliceUploadDocumentBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener, Callback<List<RegistrationModel>> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();

    private final int PROOF_ID_CODE=1,CONCESSION_CODE=2,CONCESSION_CODE1=7,CONCESSION_CODE2=8,ADDRESS_PROOF_CODE=3,PROOF_ID_CODE_PICK=4,CONCESSION_CODE_PICK=5,CONCESSION_CODE_PICK1=9,CONCESSION_CODE_PICK2=10,ADDRESS_PROOF_CODE_PICK=6;
    private Bitmap bitmapImage;
    private String path;
    private String appid;
    private String outMsg;
    private String billdestMsg;
    private List<SpinnerDataModel> mainList= new ArrayList<>();
    private List<SpinnerDataModel> concessionList= new ArrayList<>();
    private String globalvariable;
    private String payload = PrefrenceKeyConstant.BDSKUATY;
    private  String requestmsg;
    //private String payloadRest = "|NA|NA|NA|INR|NA|R|"+BDSKUATY.toLowerCase(Locale.ROOT)+"|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|"+BILL_DESK_DUMP_URL;
    private String payloadRest = "|NA|NA|NA|INR|NA|R|"+ PrefrenceKeyConstant.BDSKUATY.toLowerCase(Locale.ROOT)+"|NA|NA|F|NA|";
    // private String paynow = "|NA|NA|NA|NA|NA|"+BILL_DESK_DUMP_URL;
    private String paynow = "|"+ RegisterationDataHelper.getInstance().getPoliceApplicationModel().getMobileNo()+"|MOB|NA|NA|NA|"+ PrefrenceKeyConstant.BILL_DESK_DUMP_URL;
    private String FEES= PrefrenceKeyConstant.pay;
    List<String> concessionListClone=new ArrayList<>();
    List<String> concessionListgetno=new ArrayList<>();
    private String img;
    private String msg;


    @Override
    protected ActivityPoliceUploadDocumentBinding getActivityBinding() {
        return ActivityPoliceUploadDocumentBinding.inflate(getLayoutInflater());
    }
    @Override
    protected void init() {
        if(getIntent()!=null){
            if(getIntent().getStringExtra("CONCESSION_NAME")!=null) {
                globalvariable=getIntent().getStringExtra("CONCESSION_NAME");
                System.out.println("Selected Conssion name:" +globalvariable );
            }else {
                System.out.println("Consession name: is null");
            }
        }
        getdocument();
    }

    @Override
    protected void initCtrl() {
        binding.spinnerPhotoId.setOnItemSelectedListener(this);
        binding.spinnerConcession.setOnItemSelectedListener(this);
        binding.spinnerSalarySlip.setOnItemSelectedListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
        binding.capture.setOnClickListener(this);
        binding.browse.setOnClickListener(this);
        binding.capture1.setOnClickListener(this);
        binding.browse1.setOnClickListener(this);
        binding.captureSalarySlip.setOnClickListener(this);
        binding.browseSalarySlip.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);
    }

    private void getdocument(){
        CommonUtils.showLoadingDialog(this);
        apiInterface.getDocumentType(new SpinnerRequestModel(globalvariable)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concessionList=response.body();
                    concessionListClone.add(0,"Select Concession Proof");
                    concessionListgetno.add(0,"Select Concession Proof");
                    for(int i=0;i<response.body().size();i++){
                        concessionListClone.add(response.body().get(i).getDocumentName());
                        concessionListgetno.add(response.body().get(i).getSrNo());

                    }

                    apiInterface.getConcessionDoc(new SpinnerRequestModel("")).enqueue(new Callback<List<SpinnerDataModel>>() {
                        @Override
                        public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                            CommonUtils.dismissLoadingDialog();
                            if(response.isSuccessful()) {
                                mainList=response.body();
                                List<String> addressList=new ArrayList<>();
                                List<String> proofList=new ArrayList<>();
                                addressList.add(0,"Salary Slip");
                                proofList.add(0,"Select Proof ID");
                                for(int i=0;i<response.body().size();i++){
                                    //addressList.add(response.body().get(i).getDocumentName());
                                    proofList.add(response.body().get(i).getDocumentName());
                                }
                                CommonUtils.setSpinner(binding.spinnerConcession, Collections.singletonList(concessionListClone.get(1)));
                                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionApplicableDocumentProofID(concessionListgetno.get(1));
                                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setAddressProofID("0");
                                CommonUtils.setSpinner(binding.spinnerPhotoId,proofList);
                                CommonUtils.setSpinner(binding.spinnerSalarySlip,addressList);
                            }
                            else CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error1));
                        }

                        @Override
                        public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
                        }
                    });
                }else {
                    CommonUtils.dismissLoadingDialog();
                    CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error2));
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });

    }


    private boolean checkPermission(int requestCode) {

        boolean cameraPermission =
                ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermission) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    requestCode);

            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PROOF_ID_CODE:
                if(resultCode==0){
                    this.recreate();
                }else {
                    loadImage((Bitmap) data.getExtras().get("data"));
                    binding.ivOne.setVisibility(View.VISIBLE);
                    Toast.makeText(Police_Upload_Document.this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case PROOF_ID_CODE_PICK:
                try {
                    if(resultCode == 0){
                        this.recreate();
//                        startNewActivity(UploadDocumentActivity.class);
                    }else{
                        Uri uri = data.getData();
                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        loadImage(ImageUtil.getBitmapFromUri(this, uri));
                        binding.ivOne.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e)
                { e.printStackTrace();
                }

                break;

            case CONCESSION_CODE:
                if(resultCode==0){
                    this.recreate();
                }else {
                    loadImage1((Bitmap) data.getExtras().get("data"));
                    binding.ivTwo.setVisibility(View.VISIBLE);
                    Toast.makeText(Police_Upload_Document.this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case CONCESSION_CODE_PICK:
                try {
                    if(resultCode == 0){
                        this.recreate();
                    }else{
                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        loadImage1(ImageUtil.getBitmapFromUri(this, uri));
                        binding.ivTwo.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e)
                { e.printStackTrace();
                }
                break;


            case ADDRESS_PROOF_CODE:
                if(resultCode==0){
                    this.recreate();
                }else {
                    loadImage5((Bitmap) data.getExtras().get("data"));
                    binding.imgSalarySlip.setVisibility(View.VISIBLE);
                    Toast.makeText(Police_Upload_Document.this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case ADDRESS_PROOF_CODE_PICK:
                try {
                    if(resultCode == 0){
                        this.recreate();
                    }else{
                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        loadImage5(ImageUtil.getBitmapFromUri(this, uri));
                        binding.imgSalarySlip.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e)
                { e.printStackTrace();
                }break;
        }
    }

    private boolean isGifFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);

        if (mimeType != null && mimeType.equalsIgnoreCase("image/gif")) {
            return true;
        }

        String path = uri.toString().toLowerCase();
        return path.endsWith(".gif");
    }

    public static Bitmap resizeImage(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void loadImage(Bitmap bitmap) {
        bitmapImage=bitmap;
        if(ImageUtil.checkImageSize(bitmap))
        {
            Glide.with(this).load(bitmap).into(binding.ivOne);
            bitmapImage= resizeImage(bitmap,150,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            bitmapImage.recycle();
            convertByteToHexadecimalPhotoId(byteArray);
//            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPhotoIDProofData(ImageUtil.convertBaseString(bitmapImage));
        } else{
            Toast.makeText(Police_Upload_Document.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show();
        }

        // showLoadingDialog(this);
    }

    private void loadImage1(Bitmap bitmap) {
        bitmapImage=bitmap;
        if(ImageUtil.checkImageSize(bitmap)){  Glide.with(this).load(bitmap).into(binding.ivTwo);
            bitmapImage= resizeImage(bitmap,150,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            bitmapImage.recycle();
            convertByteToHexadecimalConOne(byteArray);
            // RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionApplicableDocumentProofFileData(ImageUtil.convertBaseString(bitmap));
        }else{ Toast.makeText(Police_Upload_Document.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show(); }

    }

    private void loadImage5(Bitmap bitmap) {
        bitmapImage=bitmap;
        if(ImageUtil.checkImageSize(bitmap)){  Glide.with(this).load(bitmap).into(binding.imgSalarySlip);
            bitmapImage= resizeImage(bitmap,150,true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            bitmapImage.recycle();
            convertByteToHexadecimalAddProof(byteArray);
            // RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConAppDoc3HexData(ImageUtil.convertBaseString(bitmap));
        }else{ Toast.makeText(Police_Upload_Document.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show(); }

    }

    public  void convertByteToHexadecimalPhotoId(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPhotoIDProofData(hex);
        System.out.print(hex);
    }

    public  void convertByteToHexadecimalConOne(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionApplicableDocumentProofFileData(hex);
        System.out.print(hex);
    }


    public  void convertByteToHexadecimalAddProof(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setAddressProofFileData(hex);
        System.out.print(hex);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_photo_id) {
            if (position > 0) {
                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPhotoIDProofID(mainList.get(position - 1).getSrNo());
            }
        }
// Uncomment the line below if you need to handle the spinner_salarySlip case
/*
else if (parent.getId() == R.id.spinner_salarySlip) {
    if (position > 0) {
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setAddressProofID(mainList.get(position - 1).getSrNo());
    }
}
*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            if (binding.spinnerPhotoId.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.getRoot(), "Please Select Proof ID!");
            } else {
                if (checkPermission(PROOF_ID_CODE)) {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PROOF_ID_CODE);
                }
            }
            binding.ivOne.setImageBitmap(null);
        } else if (v.getId() == R.id.browse) {
            if (binding.spinnerPhotoId.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.getRoot(), "Please Select Proof ID!");
            } else {
                binding.ivOne.setImageBitmap(null);
                if (checkPermission(PROOF_ID_CODE_PICK)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PROOF_ID_CODE_PICK);
                }
            }
        } else if (v.getId() == R.id.capture_salarySlip) {
            if (checkPermission(ADDRESS_PROOF_CODE)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), ADDRESS_PROOF_CODE);
            }
            binding.imgSalarySlip.setImageBitmap(null);
        } else if (v.getId() == R.id.browse_salarySlip) {
            binding.imgSalarySlip.setImageBitmap(null);
            if (checkPermission(ADDRESS_PROOF_CODE_PICK)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADDRESS_PROOF_CODE_PICK);
            }
        } else if (v.getId() == R.id.capture1) {
            if (checkPermission(CONCESSION_CODE)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CONCESSION_CODE);
            }
            binding.ivTwo.setImageBitmap(null);
        } else if (v.getId() == R.id.browse1) {
            binding.ivTwo.setImageBitmap(null);
            if (checkPermission(CONCESSION_CODE_PICK)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CONCESSION_CODE_PICK);
            }
        } else if (v.getId() == R.id.btn_back) {
            super.onBackPressed();
        } else if (v.getId() == R.id.btn_clear) {
            binding.spinnerPhotoId.setSelection(0);
            binding.spinnerSalarySlip.setSelection(0);
            binding.spinnerConcession.setSelection(0);
            binding.tiePhotoNo1.setText("");
            binding.tiePhotoNo2.setText("");
            binding.tieSalarySlip.setText("");
            binding.btnRegister.setText("Register");
            binding.ivOne.setVisibility(View.GONE);
            binding.ivTwo.setVisibility(View.GONE);
            binding.imgSalarySlip.setVisibility(View.GONE);
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionApplicableDocumentProofFileData("");
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setAddressProofFileData("");
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPhotoIDProofData("");
        } else if (v.getId() == R.id.btn_register) {
            if (binding.btnRegister.getText().toString().equals("Register")) {
                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPhotoIdno(binding.tiePhotoNo1.getText().toString());
                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConAppDocProofNo(binding.tiePhotoNo2.getText().toString());
                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setAddressProofNo(binding.tieSalarySlip.getText().toString());

                if (!RegisterationDataHelper.getInstance().getPoliceApplicationModel().getPhotoIdno().equals("") &&
                        !RegisterationDataHelper.getInstance().getPoliceApplicationModel().getConAppDocProofNo().equals("") &&
                        !RegisterationDataHelper.getInstance().getPoliceApplicationModel().getAddressProofNo().equals("") &&
                        !RegisterationDataHelper.getInstance().getPoliceApplicationModel().getAddressProofFileData().equals("") &&
                        !RegisterationDataHelper.getInstance().getPoliceApplicationModel().getConcessionApplicableDocumentProofFileData().equals("") &&
                        !RegisterationDataHelper.getInstance().getPoliceApplicationModel().getPhotoIDProofData().equals("")) {

                    CommonUtils.showLoadingDialog(this);
                    apiInterface.SavePoliceRegistration(RegisterationDataHelper.getInstance().getPoliceApplicationModel()).enqueue(this);
                } else {
                    CommonUtils.showSnackBar(binding.getRoot(), "Please Select and Fill All Details!");
                }
            } else {
                // billDeskRequest();
            }
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(Police_Upload_Document.this, MainActivity.class));
        }

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


    @Override
    public void onResponse(Call<List<RegistrationModel>> call, Response<List<RegistrationModel>> response) {
        CommonUtils.dismissLoadingDialog();
        if(response.isSuccessful()){
            for(int i=0;i<response.body().size();i++){
                outMsg=response.body().get(i).getOutMsg();
                appid=response.body().get(i).getAppId();
                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setApplicantID(appid);
                showDialog(outMsg,appid);
                break;
            }

        }else{
            CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
        }
    }

    public void onBackPressed(){
    }

    private void startNewActivity(Class className) {
        startActivity(new Intent(this,className));
    }

    @Override
    public void onFailure(Call<List<RegistrationModel>> call, Throwable t) {
        CommonUtils.dismissLoadingDialog();
        CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
    }

    private void showDialog(String message,String appid){
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setCancelable(false)
                .setMessage("Your Application id is "+appid)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        binding.btnRegister.setText("Done");
                        RegisterationDataHelper.getInstance().setPoliceApplicationModel
                                (new PoliceApplicationModel("","1",appid,"","","","","","",
                                        PrefrenceHelper.getPrefrenceStringValue(Police_Upload_Document.this, PrefrenceKeyConstant.PHONE_NO),
                                        PrefrenceHelper.getPrefrenceStringValue(Police_Upload_Document.this, PrefrenceKeyConstant.EMAIL_ID),
                                        "","","","","","","","",
                                        "","","","","","","",
                                        "","","","","","","","","","",
                                        "","0","0","0","","","0","0","0","0","0","0",
                                        "","","","","I","","","","","",
                                        "","","",""));
                        Intent intent = new Intent(Police_Upload_Document.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })
                .show();
    }
}