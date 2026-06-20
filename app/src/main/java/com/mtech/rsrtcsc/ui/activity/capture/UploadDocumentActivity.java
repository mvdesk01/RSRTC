package com.mtech.rsrtcsc.ui.activity.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.billdesk.sdk.PaymentOptions;
import com.bumptech.glide.Glide;
import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityUploadDocumentsBinding;
import com.mtech.rsrtcsc.model.request.ApplicationModel;
import com.mtech.rsrtcsc.model.request.BillDeskRequestModel;
import com.mtech.rsrtcsc.model.request.BillDeskRequestPayment;
import com.mtech.rsrtcsc.model.request.BilldeskRequestPayloadModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.request.SpinnerRequestModel;
import com.mtech.rsrtcsc.model.response.BillDeskModel;
import com.mtech.rsrtcsc.model.response.RegistrationModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.billdesk.BilldeskCallBack;
import com.mtech.rsrtcsc.ui.activity.billdesk.MyWebViewClient;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.ImageUtil;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocumentActivity extends BaseActivity<ActivityUploadDocumentsBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener, Callback<List<RegistrationModel>> {

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
    private String  payloadRest = "|NA|NA|NA|INR|NA|R|"+ PrefrenceKeyConstant.BDSKUATY.toLowerCase(Locale.ROOT)+"|NA|NA|F|NA|";
    // private String paynow = "|NA|NA|NA|NA|NA|"+BILL_DESK_DUMP_URL;
    private String paynow = "|"+RegisterationDataHelper.getInstance().getApplicationData().getMobileNo()+"|MOB|NA|NA|NA|"+ PrefrenceKeyConstant.BILL_DESK_DUMP_URL;
    private String FEES= PrefrenceKeyConstant.pay;
    List<String> concessionListClone=new ArrayList<>();
    List<String> concessionListgetno=new ArrayList<>();
    private String img;
    private String msg;

    private Uri photoURI;
    private Uri currentImageUri;
    private String currentPhotoPath;

    @Override
    protected ActivityUploadDocumentsBinding getActivityBinding() {
        return ActivityUploadDocumentsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        if(getIntent()!=null){
            if(getIntent().getStringExtra("CONCESSION_NAME")!=null) {
                globalvariable = getIntent().getStringExtra("CONCESSION_NAME");
            }
            else
            {
                System.out.println("Consession name: is null");
            }
        }
        getdocument();

    }

    private void getdocument(){
        CommonUtils.showLoadingDialog(this);
//        Log.d("TAG LL", "getdocument: "+globalvariable);
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
                        if(concessionListClone.size()==3){
                            binding.concessionapplication1.setVisibility(View.VISIBLE);
                            CommonUtils.setSpinner(binding.spinnerConcession, Collections.singletonList(concessionListClone.get(1)));
                            CommonUtils.setSpinner(binding.spinnerConcession1, Collections.singletonList(concessionListClone.get(2)));
                            RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofID(concessionListgetno.get(1));
                            RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc2ID(concessionListgetno.get(2));
                        }else if(concessionListClone.size()>3){
                            binding.concessionapplication1.setVisibility(View.VISIBLE);
                            binding.concessionapplication2.setVisibility(View.VISIBLE);
                            CommonUtils.setSpinner(binding.spinnerConcession, Collections.singletonList(concessionListClone.get(1)));
                            CommonUtils.setSpinner(binding.spinnerConcession1, Collections.singletonList(concessionListClone.get(2)));
                            CommonUtils.setSpinner(binding.spinnerConcession2, Collections.singletonList(concessionListClone.get(3)));
                            RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofID(concessionListgetno.get(1));
                            RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc2ID(concessionListgetno.get(2));
                            RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc3ID(concessionListgetno.get(3));
                        }
                    }

                    apiInterface.getConcessionDoc(new SpinnerRequestModel("")).enqueue(new Callback<List<SpinnerDataModel>>() {
                        @Override
                        public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                            CommonUtils.dismissLoadingDialog();
                            if(response.isSuccessful()) {
                                mainList=response.body();
                                List<String> addressList=new ArrayList<>();
                                List<String> proofList=new ArrayList<>();
                                addressList.add(0,"Select Address Proof");
                                proofList.add(0,"Select Proof ID");
                                for(int i=0;i<response.body().size();i++){
                                    addressList.add(response.body().get(i).getDocumentName());
                                    proofList.add(response.body().get(i).getDocumentName());
                                }
                                CommonUtils.setSpinner(binding.spinnerConcession, Collections.singletonList(concessionListClone.get(1)));
                                RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofID(concessionListgetno.get(1));

                                CommonUtils.setSpinner(binding.spinnerPhotoId,proofList);
                                CommonUtils.setSpinner(binding.spinnerAddressProof,addressList);
                            }
                            else CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error1));
                        }

                        @Override
                        public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                            CommonUtils.dismissLoadingDialog();
                            CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
                        }
                    });
                }
                else
                {
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

    @Override
    protected void initCtrl() {
        binding.spinnerPhotoId.setOnItemSelectedListener(this);
        binding.spinnerConcession.setOnItemSelectedListener(this);
        binding.spinnerConcession1.setOnItemSelectedListener(this);
        binding.spinnerConcession2.setOnItemSelectedListener(this);
        binding.spinnerAddressProof.setOnItemSelectedListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
        binding.capture.setOnClickListener(this);
        binding.browse.setOnClickListener(this);
        binding.capture1.setOnClickListener(this);
        binding.browse1.setOnClickListener(this);
        binding.capture2.setOnClickListener(this);
        binding.browse2.setOnClickListener(this);
        binding.capture4.setOnClickListener(this);
        binding.browse4.setOnClickListener(this);
        binding.capture5.setOnClickListener(this);
        binding.browse5.setOnClickListener(this);
        binding.ivHumberger.setOnClickListener(this);
//        WebView webView = findViewById(R.id.webview);
//        webView.setWebViewClient(new MyWebViewClient(UploadDocumentActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            this.recreate();
            return;
        }

        try {
            switch (requestCode) {
                case PROOF_ID_CODE_PICK:
                    if (data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                        loadImage(bitmap);
                        binding.ivOne.setVisibility(View.VISIBLE);
                    }
                    break;

                case CONCESSION_CODE_PICK:
                    if (data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                        loadImage1(bitmap);
                        binding.ivTwo.setVisibility(View.VISIBLE);
                    }
                    break;

                case CONCESSION_CODE_PICK1:
                    if (data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                        loadImage4(bitmap);
                        binding.ivFour.setVisibility(View.VISIBLE);
                    }
                    break;

                case CONCESSION_CODE_PICK2:
                    if (data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                        loadImage5(bitmap);
                        binding.ivFive.setVisibility(View.VISIBLE);
                    }
                    break;

                case ADDRESS_PROOF_CODE_PICK:
                    if (data != null && data.getData() != null) {

                        Uri uri = data.getData();

                        if (isGifFile(uri)) {
                            Toast.makeText(this,
                                    "GIF files are not allowed. Please select JPG, JPEG or PNG image.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                        loadImage2(bitmap);
                        binding.ivThree.setVisibility(View.VISIBLE);
                    }
                    break;



                case PROOF_ID_CODE:
                    if (currentPhotoPath != null) {
                        Bitmap bitmap = ImageUtil.getBitmapFromPath(currentPhotoPath, 1024, 1024);
                        bitmap = rotateIfRequired(bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        loadImage(bitmap);
                        binding.ivOne.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case PROOF_ID_CODE_PICK:
//                    if (data != null) {
//                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
////                        bitmap = rotateIfRequired(bitmap, data.getData());
//                        loadImage(bitmap);
//                        binding.ivOne.setVisibility(View.VISIBLE);
//                    }
//                    break;

                case CONCESSION_CODE:
                    if (currentPhotoPath != null) {
                        Bitmap bitmap = ImageUtil.getBitmapFromPath(currentPhotoPath, 1024, 1024);
                        bitmap = rotateIfRequired(bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        loadImage1(bitmap);
                        binding.ivTwo.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case CONCESSION_CODE_PICK:
//                    if (data != null) {
//                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
////                        bitmap = rotateIfRequired(bitmap, data.getData());
//                        loadImage1(bitmap);
//                        binding.ivTwo.setVisibility(View.VISIBLE);
//                    }
//                    break;

                case CONCESSION_CODE1:
                    if (currentPhotoPath != null) {
                        Bitmap bitmap = ImageUtil.getBitmapFromPath(currentPhotoPath, 1024, 1024);
                        bitmap = rotateIfRequired(bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        loadImage4(bitmap);
                        binding.ivFour.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case CONCESSION_CODE_PICK1:
//                    if (data != null) {
//                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
////                        bitmap = rotateIfRequired(bitmap, data.getData());
//                        loadImage4(bitmap);
//                        binding.ivFour.setVisibility(View.VISIBLE);
//                    }
//                    break;

                case CONCESSION_CODE2:
                    if (currentPhotoPath != null) {
                        Bitmap bitmap = ImageUtil.getBitmapFromPath(currentPhotoPath, 1024, 1024);
                        bitmap = rotateIfRequired(bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        loadImage5(bitmap);
                        binding.ivFive.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case CONCESSION_CODE_PICK2:
//                    if (data != null) {
//                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
////                        bitmap = rotateIfRequired(bitmap, data.getData());
//                        loadImage5(bitmap);
//                        binding.ivFive.setVisibility(View.VISIBLE);
//                    }
//                    break;

                case ADDRESS_PROOF_CODE:
                    if (currentPhotoPath != null) {
                        Bitmap bitmap = ImageUtil.getBitmapFromPath(currentPhotoPath, 1024, 1024);
                        bitmap = rotateIfRequired(bitmap, Uri.fromFile(new File(currentPhotoPath)));
                        loadImage2(bitmap);
                        binding.ivThree.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Photo Upload successfully", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case ADDRESS_PROOF_CODE_PICK:
//                    if (data != null) {
//                        Bitmap bitmap = ImageUtil.getBitmapFromUri(this, data.getData());
////                        bitmap = rotateIfRequired(bitmap, data.getData());
//                        loadImage2(bitmap);
//                        binding.ivThree.setVisibility(View.VISIBLE);
//                    }
//                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Image loading failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.capture) {
            if (binding.spinnerPhotoId.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.getRoot(), "Please Select Proof ID!");
            } else {
                if (checkPermission(PROOF_ID_CODE)) {
                    currentImageUri = createImageFileAndGetUri(PROOF_ID_CODE); // Custom method
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                    startActivityForResult(intent, PROOF_ID_CODE);
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
        } else if (v.getId() == R.id.capture2) {
            if (binding.spinnerAddressProof.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.getRoot(), "Please Select Address Proof");
            } else {
                if (checkPermission(ADDRESS_PROOF_CODE)) {
                    currentImageUri = createImageFileAndGetUri(ADDRESS_PROOF_CODE); // Custom method
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                    startActivityForResult(intent, ADDRESS_PROOF_CODE);
                }

               /* if (checkPermission(ADDRESS_PROOF_CODE)) {
                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), ADDRESS_PROOF_CODE);
                }*/
            }
            binding.ivThree.setImageBitmap(null);
        } else if (v.getId() == R.id.browse2) {
            if (binding.spinnerAddressProof.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.getRoot(), "Please Select Address Proof ID!");
            }
            binding.ivThree.setImageBitmap(null);
            if (checkPermission(ADDRESS_PROOF_CODE_PICK)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADDRESS_PROOF_CODE_PICK);
            }
        } else if (v.getId() == R.id.capture1) {
            if (checkPermission(CONCESSION_CODE)) {
                currentImageUri = createImageFileAndGetUri(CONCESSION_CODE); // Custom method
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                startActivityForResult(intent, CONCESSION_CODE);
            }

           /* if (checkPermission(CONCESSION_CODE)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CONCESSION_CODE);
            }*/
            binding.ivTwo.setImageBitmap(null);
        } else if (v.getId() == R.id.browse1) {
            binding.ivTwo.setImageBitmap(null);

            if (checkPermission(CONCESSION_CODE_PICK)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CONCESSION_CODE_PICK);
            }
        } else if (v.getId() == R.id.capture4) {
            if (checkPermission(CONCESSION_CODE1)) {
                currentImageUri = createImageFileAndGetUri(CONCESSION_CODE1); // Custom method
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                startActivityForResult(intent, CONCESSION_CODE1);
            }
            /*if (checkPermission(CONCESSION_CODE1)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CONCESSION_CODE1);
            }*/
            binding.ivFour.setImageBitmap(null);
        } else if (v.getId() == R.id.browse4) {
            binding.ivFour.setImageBitmap(null);
            if (checkPermission(CONCESSION_CODE_PICK1)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CONCESSION_CODE_PICK1);
            }
        } else if (v.getId() == R.id.capture5) {
            if (checkPermission(CONCESSION_CODE2)) {
                currentImageUri = createImageFileAndGetUri(CONCESSION_CODE2); // Custom method
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                startActivityForResult(intent, CONCESSION_CODE2);
            }
          /*  if (checkPermission(CONCESSION_CODE2)) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CONCESSION_CODE2);
            }*/
            binding.ivFive.setImageBitmap(null);
        } else if (v.getId() == R.id.browse5) {
            binding.ivFive.setImageBitmap(null);
            if (checkPermission(CONCESSION_CODE_PICK2)) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CONCESSION_CODE_PICK2);
            }
        } else if (v.getId() == R.id.btn_back) {
            super.onBackPressed();
        } else if (v.getId() == R.id.btn_clear) {
            binding.spinnerPhotoId.setSelection(0);
            binding.spinnerAddressProof.setSelection(0);
            binding.spinnerConcession.setSelection(0);
            binding.spinnerConcession1.setSelection(0);
            binding.spinnerConcession2.setSelection(0);
            binding.tiePhotoNo1.setText("");
            binding.tiePhotoNo2.setText("");
            binding.tiePhotoNo3.setText("");
            binding.tiePhotoNo4.setText("");
            binding.tiePhotoNo5.setText("");
            binding.btnRegister.setText("Register");
            binding.ivOne.setVisibility(View.GONE);
            binding.ivTwo.setVisibility(View.GONE);
            binding.ivThree.setVisibility(View.GONE);
            binding.ivFour.setVisibility(View.GONE);
            binding.ivFive.setVisibility(View.GONE);
            RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofFileData("");
            RegisterationDataHelper.getInstance().getApplicationData().setAddressProofFileData("");
            RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofData("");
            RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc2HexData("");
            RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc3HexData("");
        } else if (v.getId() == R.id.btn_register) {
            if (binding.btnRegister.getText().toString().equals("Register")) {
                RegisterationDataHelper.getInstance().getApplicationData().setPhotoIdno(binding.tiePhotoNo1.getText().toString());
                RegisterationDataHelper.getInstance().getApplicationData().setConAppDocProofNo(binding.tiePhotoNo2.getText().toString());
                RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc2No(binding.tiePhotoNo3.getText().toString());
                RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc3No(binding.tiePhotoNo4.getText().toString());
                RegisterationDataHelper.getInstance().getApplicationData().setAddressProofNo(binding.tiePhotoNo5.getText().toString());

                if (concessionListClone.size() > 3) {
                    if (!RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc2HexData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc3HexData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc2No().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc3No().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIdno().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDocProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConcessionApplicableDocumentProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIDProofData().equals("")) {

                        CommonUtils.showLoadingDialog(this);
                        apiInterface.saveRegistration(RegisterationDataHelper.getInstance().getApplicationData()).enqueue(this);
                    } else {
                        CommonUtils.showSnackBar(binding.getRoot(), "Please Select and Fill All Details!");
                    }
                } else if (concessionListClone.size() == 3) {
                    if (!RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc2HexData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDoc2No().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIdno().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDocProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConcessionApplicableDocumentProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIDProofData().equals("")) {

                        CommonUtils.showLoadingDialog(this);
                        apiInterface.saveRegistration(RegisterationDataHelper.getInstance().getApplicationData()).enqueue(this);
                    } else {
                        CommonUtils.showSnackBar(binding.getRoot(), "Please Select and Fill All Details!");
                    }
                } else if (concessionListClone.size() == 2) {
                    if (!RegisterationDataHelper.getInstance().getApplicationData().getPhotoIdno().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConAppDocProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofNo().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getConcessionApplicableDocumentProofFileData().equals("") &&
                            !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIDProofData().equals("")) {

                        CommonUtils.showLoadingDialog(this);
                        apiInterface.saveRegistration(RegisterationDataHelper.getInstance().getApplicationData()).enqueue(this);
                    } else {
                        CommonUtils.showSnackBar(binding.getRoot(), "Please Select and Fill All Details!");
                    }
                }
            }
            else
            {
                billDeskRequest();
            }
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(UploadDocumentActivity.this, MainActivity.class));
        }
    }

    private void generatePayloadBillDesk() {
        CommonUtils.showLoadingDialog(this);
        @SuppressLint("DefaultLocale") String str = String.format("%014d", Integer.parseInt(appid));
        //payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + PrefrenceKeyConstant.pay + payloadRest +str +paynow;
        payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + RegisterationDataHelper.getInstance().getApplicationData().getCardFees() + payloadRest +str +paynow;
        apiInterface.getCheckSum(new BilldeskRequestPayloadModel(payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()) {
                    for(int i=0;i<response.body().size();i++){
                        payload = payload +  "|" + response.body().get(i).getMsg().toUpperCase(Locale.ROOT);
                        requestmsg = payload +  "|" + response.body().get(i).getMsg().toUpperCase(Locale.ROOT);
                        billdestMsg = response.body().get(i).getMsg().toUpperCase(Locale.ROOT);
                        msg = response.body().get(i).getMsg();
                        Log.e("response",response.body().get(i).getMsg().toUpperCase(Locale.ROOT));
                        break;
                    }
                } else
                {
                    CommonUtils.dismissLoadingDialog();
                    CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error3));
                }
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void showDialog(String message,String appid){
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setCancelable(false)
                .setMessage("Your Application id is "+appid)
                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        binding.btnRegister.setText("Pay Now");
                        RegisterationDataHelper.getInstance().setApplicationData
                                (new ApplicationModel("","1",appid,"","","","","","",
                                        PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.PHONE_NO),
                                        PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.EMAIL_ID),
                                        "","","","","","","","",
                                        "","","","","","","",
                                        "","","","","","","","","","",
                                        "","","0","0","0","","","0","0","0","0","0",
                                        "0","","","","","I","","","","",
                                        "","","","","","","","","",
                                        "","","","","",""));
                        billdeskrequestformsg();
                        billDeskRequest();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void billdeskrequestformsg() {
        try {

            Log.d("llbilldesk CardNo", appid);
            Log.d("llbilldesk  UserId", RegisterationDataHelper.getInstance().getApplicationData().getMobileNo());
            Log.d("llbilldesk   Msg", payload);
            Log.d("llbilldesk  RequestType", PrefrenceKeyConstant.REQUEST_TYPE_BILLDESK);
        }
        catch (Exception e){

        }
        apiInterfaceRSTC.BillDeskRequest(new BillDeskRequestPayment(appid,RegisterationDataHelper.getInstance().getApplicationData().getMobileNo(),payload,PrefrenceKeyConstant.REQUEST_TYPE_BILLDESK)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().size();i++){
                        String str = response.body().get(i).getMsg();
                        System.out.println("Insert"+str);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {

            }
        });

    }

    private void billDeskRequest() {
        CommonUtils.showLoadingDialog(this);
        Log.d("TAG billDeskRequest Msg", payload);
        apiInterface.billDeskRequest(new BillDeskRequestModel(payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                CommonUtils.dismissLoadingDialog();
                Intent sdkIntent = new Intent(UploadDocumentActivity.this, PaymentOptions.class);
                sdkIntent.putExtra("msg", payload);
                sdkIntent.putExtra("user-email", PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.EMAIL_ID));
                sdkIntent.putExtra("user-mobile",PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.PHONE_NO));
                sdkIntent.putExtra("callback",new BilldeskCallBack());
                startActivity(sdkIntent);
            }
            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    @Override
    public void onResponse(Call<List<RegistrationModel>> call, Response<List<RegistrationModel>> response) {
        CommonUtils.dismissLoadingDialog();
        if(response.isSuccessful()){
            for(int i=0;i<response.body().size();i++){
                outMsg=response.body().get(i).getOutMsg();
                appid=response.body().get(i).getAppId();
                RegisterationDataHelper.getInstance().getApplicationData().setApplicantID(appid);
                generatePayloadBillDesk();
                showDialog(outMsg,appid);

                break;
            }

        } else{
            CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
        }
    }

    @Override
    public void onFailure(Call<List<RegistrationModel>> call, Throwable t) {
        CommonUtils.dismissLoadingDialog();
        CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
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

    private Bitmap rotateIfRequired(Bitmap bitmap, Uri selectedImage) throws IOException {
        InputStream input = getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }
    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
    private Uri createImageFileAndGetUri(int requestCode) {
        try {
            String imageFileName = "IMG_" + requestCode + "_" + System.currentTimeMillis();
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            currentPhotoPath = imageFile.getAbsolutePath();
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
    private Bitmap compressBitmap(Bitmap original) {
        // Step 1: Resize dimensions
        int maxWidth = 1024;
        int maxHeight = 1024;

        int width = original.getWidth();
        int height = original.getHeight();

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(original, finalWidth, finalHeight, true);

        // Step 2: Compress resized bitmap to under 2MB
        int quality = 100;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        while (stream.toByteArray().length > 2 * 1024 * 1024 && quality > 10) {
            stream.reset();
            quality -= 5;
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        }

        byte[] compressedData = stream.toByteArray();
        return BitmapFactory.decodeByteArray(compressedData, 0, compressedData.length);
    }
    private void loadImage(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.ivOne);
        } else {
            Toast.makeText(this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmapImage = resizeImage(bitmap, 150, true); // Still keeping resize
        byte[] compressedImage = ImageUtil.compressBitmapToUnder2MB(bitmapImage);
        bitmapImage.recycle();
        convertByteToHexadecimalPhotoId(compressedImage);
        // showLoadingDialog(this);
    }
    private void loadImage1(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.ivTwo);
        } else {
            Toast.makeText(this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmapImage = resizeImage(bitmap, 150, true); // Still keeping resize
        byte[] compressedImage = ImageUtil.compressBitmapToUnder2MB(bitmapImage);
        bitmapImage.recycle();

        convertByteToHexadecimalConOne(compressedImage);
    }
    private void loadImage2(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.ivThree);
        } else {
            Toast.makeText(this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmapImage = resizeImage(bitmap, 150, true); // Still keeping resize
        byte[] compressedImage = ImageUtil.compressBitmapToUnder2MB(bitmapImage);
        bitmapImage.recycle();

        convertByteToHexadecimalAddProof(compressedImage);

    /*    bitmapImage=bitmap;
        if(ImageUtil.checkImageSize(bitmap)){
            Glide.with(this).load(bitmap).into(binding.ivThree);
            // RegisterationDataHelper.getInstance().getApplicationData().setAddressProofFileData(ImageUtil.convertBaseString(bitmap));
        }else{ Toast.makeText(UploadDocumentActivity.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show(); }
        bitmapImage= resizeImage(bitmap,150,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 30, stream);
        byte[] byteArray = stream.toByteArray();
        bitmapImage.recycle();
        convertByteToHexadecimalAddProof(byteArray);*/
    }
    private void loadImage4(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.ivFour);
        } else {
            Toast.makeText(this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmapImage = resizeImage(bitmap, 150, true); // Still keeping resize
        byte[] compressedImage = ImageUtil.compressBitmapToUnder2MB(bitmapImage);
        bitmapImage.recycle();

        convertByteToHexadecimalConTwo(compressedImage);
    }
    private void loadImage5(Bitmap bitmap) {
        Bitmap compressedBitmap = compressBitmap(bitmap);
        bitmapImage = compressedBitmap;

        if (ImageUtil.checkImageSize(compressedBitmap)) {
            Glide.with(this).load(compressedBitmap).into(binding.ivFive);
        } else {
            Toast.makeText(this, "Please upload photo up to 2 MB", Toast.LENGTH_SHORT).show();
            return;
        }

        bitmapImage = resizeImage(bitmap, 150, true); // Still keeping resize
        byte[] compressedImage = ImageUtil.compressBitmapToUnder2MB(bitmapImage);
        bitmapImage.recycle();

        convertByteToHexadecimalConThree(compressedImage);
    }
    public  void convertByteToHexadecimalPhotoId(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofData(hex);
        System.out.print(hex);
    }
    public  void convertByteToHexadecimalConOne(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofFileData(hex);
        System.out.print(hex);
    }
    public  void convertByteToHexadecimalConTwo(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc2HexData(hex);
        System.out.print(hex);
    }
    public  void convertByteToHexadecimalConThree(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getApplicationData().setConAppDoc3HexData(hex);
        System.out.print(hex);
    }
    public  void convertByteToHexadecimalAddProof(byte[] byteArray) {
        String hex = "";
        for (byte i : byteArray) {
            hex += String.format("%02X", i);
        }
        RegisterationDataHelper.getInstance().getApplicationData().setAddressProofFileData(hex);
        System.out.print(hex);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_photo_id) {
            if (position > 0) {
                RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofID(mainList.get(position - 1).getSrNo());
            }
        } else if (parent.getId() == R.id.spinner_addressProof) {
            if (position > 0) {
                RegisterationDataHelper.getInstance().getApplicationData().setAddressProofID(mainList.get(position - 1).getSrNo());
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
