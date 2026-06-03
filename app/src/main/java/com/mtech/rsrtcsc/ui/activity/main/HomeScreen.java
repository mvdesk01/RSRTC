package com.mtech.rsrtcsc.ui.activity.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityHomeScreenBinding;
import com.mtech.rsrtcsc.model.request.AadharModel;
import com.mtech.rsrtcsc.model.request.ApplicationModel;
import com.mtech.rsrtcsc.model.request.CardStatusModel;
import com.mtech.rsrtcsc.model.request.DepotAddress;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.request.Districtname;
import com.mtech.rsrtcsc.model.request.TehsilName;
import com.mtech.rsrtcsc.repository.cache.PrefrenceHelper;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.capture.ImageCaptureActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.MultiTextWatcher;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends BaseActivity<ActivityHomeScreenBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, MultiTextWatcher.TextWatcherWithInstance {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private List<SpinnerDataModel> concession= new ArrayList();
    private String globalvariable;
    private String globaladhar;
    private Calendar calendar = Calendar.getInstance();
    private String postalfees = "115";
    private List<SpinnerDataModel> depotadd= new ArrayList();
    private List<SpinnerDataModel> depoidList= new ArrayList();
    private List<SpinnerDataModel> tehsilList= new ArrayList();
    private String appid;
    List<String> listdepotadd = new ArrayList<String>();
    private  String depotaddressname;
    private String depot;
    private String depotname;
    private String tehsil;
    private String tehsils;
    private List<SpinnerDataModel> districtN= new ArrayList();
    List<String> listdistrict = new ArrayList<String>();



    @Override
    protected ActivityHomeScreenBinding getActivityBinding() {
        return ActivityHomeScreenBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        depotApi();
        DistrictNAme("");
        binding.tilMobileNo.setEnabled(false);
        binding.tilEmailId.setEnabled(false);
        binding.tieTextState.setEnabled(false);


    }


    @Override
    protected void initCtrl() {
        binding.ivHumberger.setOnClickListener(this);
        binding.tieDOB.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.clear.setOnClickListener(this);
        binding.back.setOnClickListener(this);
        binding.titleSpinner.setOnItemSelectedListener(this);
        binding.genderSpinner.setOnItemSelectedListener(this);
        binding.tieSpinnerPostalCode.setOnItemSelectedListener(this);
        binding.tvFromStop.setOnItemSelectedListener(this);
        binding.tvFromTehsil.setOnItemSelectedListener(this);

        binding.tilHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCollectPass("1");
                binding.tieTextState.setText("RAJASTHAN");
            }
        });
        binding.tilPotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCollectPass("2");
            }
        });

        new MultiTextWatcher().registerEditText(binding.tieAadharNumber)
                .registerEditText(binding.tieFirstName)
                .registerEditText(binding.tieMiddleName)
                .registerEditText(binding.tieLastName)
                .registerEditText(binding.tieFatherName)
                .registerEditText(binding.tieDOB)
                .registerEditText(binding.tieMobileNo)
                .registerEditText(binding.tiePhoneNo)
                .registerEditText(binding.tieAddress)
                .registerEditText(binding.tieTextPostalCode)
                .setCallback(this);




    }

    private void DistrictNAme(String str) {
        apiInterface.GetDistrict(new Districtname(str)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    districtN = response.body();
                    List<String> responseListString = new ArrayList<String>();

                    for(int i=0;i<response.body().size();i++)
                    {
                        responseListString.add(response.body().get(i).getDistrictName());
                        listdistrict.add(response.body().get(i).getDistrictCode());
                    }

                    responseListString.add(0,"Select District");
                    CommonUtils.setSpinner(binding.tvFromStop,responseListString);


                }else{
                    CommonUtils.dismissLoadingDialog();
                    CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (binding.getData().getTitle().equalsIgnoreCase("") || binding.getData().getTitle().equalsIgnoreCase("Title")) {
            ret = false;
            CommonUtils.showSnackBar(binding.getRoot(), "Please select title");
            binding.titleSpinner.requestFocus();

        } else if (binding.getData().getAadharNo().length() <= 11) {
            ret = false;
            binding.tilAadharNumber.setErrorEnabled(true);
            CommonUtils.showSnackBar(binding.getRoot(), "Please enter Valid Aadhaar number");
            binding.tilAadharNumber.requestFocus();
        }

        else if(binding.getData().getFirst_name().trim().equalsIgnoreCase("")){
            ret=false;
            binding.tilFirstName.setErrorEnabled(true);
            CommonUtils.showSnackBar(binding.getRoot(),"Please enter first name");
            binding.tilFirstName.requestFocus();
        }

        else if(binding.getData().getFathername().trim().equalsIgnoreCase("")){
            ret= false;
            binding.tilFatherName.setErrorEnabled(true);
            CommonUtils.showSnackBar(binding.getRoot(),"Please enter father name");
            binding.tilFatherName.requestFocus();
        }

        else if(binding.getData().getGender().equalsIgnoreCase("") || binding.getData().getGender().equalsIgnoreCase("Gender")){
            ret=false;
            CommonUtils.showSnackBar(binding.getRoot(),"Please select gender");
            binding.genderSpinner.requestFocus();
        }
        else
            if(binding.getData().getDob().equalsIgnoreCase("")){
            ret=false;
            binding.tilDOB.setErrorEnabled(true);
             CommonUtils.showSnackBar(binding.getRoot(),"Please select DOB");
            binding.tilDOB.requestFocus();
        }
        else if(binding.getData().getMobileNo().equalsIgnoreCase("") || binding.getData().getMobileNo().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            CommonUtils.showSnackBar(binding.getRoot(),"Please enter valid mobile number");
            binding.tilMobileNo.requestFocus();
        }
         else if(binding.getData().getEmailID().equalsIgnoreCase("") || !Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailID()).matches()){
            ret=false;
            binding.tilEmailId.setErrorEnabled(true);
            CommonUtils.showSnackBar(binding.getRoot(),"Please enter valid email id");
            binding.tilEmailId.requestFocus();
        }

        else if(binding.getData().getAddress().equalsIgnoreCase("")){
            ret=false;
            binding.tilAddress.setErrorEnabled(true);
                CommonUtils.showSnackBar(binding.getRoot(),"Please enter address");
            binding.tilAddress.requestFocus();
        }
            else if(binding.tilHome.isChecked()){

                if (binding.getData().getVillage().equalsIgnoreCase("")) {
                    ret = false;
                    binding.tilVillage.setErrorEnabled(true);
                    CommonUtils.showSnackBar(binding.getRoot(),"Please enter village/city/town");
                    binding.tilVillage.requestFocus();

                }else if(binding.getData().getVia().equalsIgnoreCase("")) {
                    ret = false;
                    binding.tilVia.setErrorEnabled(true);
                    CommonUtils.showSnackBar(binding.getRoot(), "Please enter via");
                    binding.tilVia.requestFocus();

                }else if(binding.getData().getDistrict().equalsIgnoreCase("") || binding.getData().getDistrict().equalsIgnoreCase("District")) {
                    ret = false;
                    CommonUtils.showSnackBar(binding.getRoot(), "Please select district");
                    binding.tvFromStop.requestFocus();

                }else if(binding.getData().getTehsil().equalsIgnoreCase("") || binding.getData().getTehsil().equalsIgnoreCase("Tehsil")) {
                    ret = false;
                    CommonUtils.showSnackBar(binding.getRoot(), "Please select tehsil");
                    binding.tvFromTehsil.requestFocus();

                }else if(binding.getData().getRemarks().length()<=5) {
                    ret = false;
                    binding.tilPostalCode.setErrorEnabled(true);
                    CommonUtils.showSnackBar(binding.getRoot(),"Please enter valid postal code");
                    binding.tilPostalCode.requestFocus();
                }

            }else {
                if(binding.getData().getDepoid().equalsIgnoreCase("") || binding.tieSpinnerPostalCode.getSelectedItem().toString().trim().equals("Select Depot")){
                    ret=false;
                    CommonUtils.showSnackBar(binding.getRoot(),"Please select Collect Pass Form");
                    binding.tieSpinnerPostalCode.requestFocus();
                } else {
                    binding.getData();
                }
            }

        return ret;
    }

    private void checkCollectPass(String type) {

        switch (type){

            case "1":
                binding.llPostalcodeview.setVisibility(View.VISIBLE);
                binding.PostalAdd.setVisibility(View.INVISIBLE);
                binding.llNearestdepot.setVisibility(View.GONE);
                SharedPreferences sharedPreferences = getSharedPreferences("75",MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putString("115", postalfees);
                editor.apply();
                break;

            case "2":
                binding.llNearestdepot.setVisibility(View.VISIBLE);
                binding.PostalAdd.setVisibility(View.VISIBLE);
                binding.llPostalcodeview.setVisibility(View.GONE);
                break;
        }
    }


    public void datePicker() {
        new DatePickerDialog(this,this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)).show();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.title_spinner) {
            if (position > 0) {
                binding.getData().setTitle(parent.getItemAtPosition(position).toString());
            }
        } else if (parent.getId() == R.id.gender_spinner) {
            binding.getData().setGender(genderApiKey(parent.getItemAtPosition(position).toString()));
        } else if (parent.getId() == R.id.tie_Spinner_PostalCode) {
            depotcontact();
        } else if (parent.getId() == R.id.tv_from_stop) {
            Gettehsil();
        } else if (parent.getId() == R.id.tv_from_tehsil) {
            if (position > 0) {
                binding.getData().setTehsil(parent.getItemAtPosition(position).toString());
            }
        }

    }

    private void depotcontact(){
        binding.tieSpinnerPostalCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depotaddressname = binding.tieSpinnerPostalCode.getSelectedItem().toString();
                if(position>0) binding.getData().setDepoid(depoidList.get(position-1).getDepotNum());
                if(position != 0){
                    depot = depoidList.get(position-1).getDepotNum();
                   // RegisterationDataHelper.getInstance().getApplicationData().setDepoid(depot);

                }
                apiInterface.GetDepotAddress(new DepotAddress(depot)).enqueue(new Callback<List<SpinnerDataModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                        if(response.isSuccessful()){
                            depotadd=response.body();
                            List<String> listpass=new ArrayList<>();
                            for(SpinnerDataModel model: response.body()){
                                listpass.add(model.getDepotCode());
                                depotname = String.valueOf(listpass);
                                binding.PostalAdd.setText(depotname);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void Gettehsil(){
        binding.tvFromStop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> listpass=new ArrayList<>();
                tehsil = binding.tvFromStop.getSelectedItem().toString();
                if(position>0) binding.getData().setDistrict(districtN.get(position-1).getDistrictName());
                if(position != 0){
                    tehsils = districtN.get(position-1).getDistrictCode();
                    // RegisterationDataHelper.getInstance().getApplicationData().setDepoid(depot);

                }
                apiInterface.GetTehsil(new TehsilName(tehsils)).enqueue(new Callback<List<SpinnerDataModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                        if(response.isSuccessful()){
                            tehsilList=response.body();

                            for(SpinnerDataModel model: response.body()){
                                listpass.add(model.getTehsilName());
                                depotname = String.valueOf(listpass);
                            }
                            listpass.add(0,"Please Select Tehsil");
                            CommonUtils.setSpinner(binding.tvFromTehsil,listpass);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        CommonUtils.showSnackBar(binding.getRoot(),"Error");
    }


    private String genderApiKey(String gender) {
        String key="";
        switch (gender.toLowerCase(Locale.ROOT)){
            case "male" : key="M"; break;
            case "female" : key="F"; break;
            case "gender" : key="Gender"; break;
            default: key="O"; break;
        }
        return key;
    }

    private void depotApi() {
        CommonUtils.showLoadingDialog(this);
        apiInterface.depotApi().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    depoidList=response.body();
                    List<String> list = new ArrayList<String>();
                    for(int i=0;i<response.body().size();i++)
                    {
                        list.add(response.body().get(i).getDepotName());
                        listdepotadd.add(response.body().get(i).getDepotNum());
                    }
                    list.add(0,"Select Depot");
                    CommonUtils.setSpinner(binding.tieSpinnerPostalCode,list);
                    proofApi(list);
                }else{
                    CommonUtils.dismissLoadingDialog();
                    CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
                }
            }
            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    private void mobileverification() {
        apiInterfaceRSTC.GetMobileNoCheck(new CardStatusModel(PrefrenceHelper.getPrefrenceStringValue(HomeScreen.this, PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        globalvariable = model.getMobileNo();
                        if(globalvariable.equals("Y")){
                            binding.tilMobileNo.setErrorEnabled(true);
                            binding.tilMobileNo.setError("This Mobile No is already registered with our system , please try with another mobile no.");
                            binding.tilMobileNo.requestFocus();
                        }else
                        {
                            RegisterationDataHelper.getInstance().setApplicationData(binding.getData());
                            Intent intent = new Intent(HomeScreen.this, ImageCaptureActivity.class);
                            intent.putExtra("message", "NormalUser");
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {

            }
        });
    }

    private boolean AadharCard() {
        boolean ret = true;
        apiInterfaceRSTC.GetAadharCheck(new AadharModel(binding.getData().getAadharNo())).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concession = response.body();
                    List<String> listpass=new ArrayList<>();
                    for(SpinnerDataModel model : response.body()){
                        globaladhar = model.getAadharNo();
                        if(globaladhar.equals("Y")){
                            binding.tilAadharNumber.setErrorEnabled(true);
                            binding.tilAadharNumber.setError("This Aadhar No is already registered with our system , please try with any other");
                            binding.tilAadharNumber.requestFocus();
                        }else
                        {
                            mobileverification();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
            }
        });
        return ret;
    }
    private void proofApi(List<String> depotList) {
        apiInterface.getProofApi().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                CommonUtils.dismissLoadingDialog();
                if(response.isSuccessful()){
                  //  proofList=response.body();
                    List<String> list = new ArrayList<String>();

                    for(int i=0;i<response.body().size();i++)
                    {
                        list.add(response.body().get(i).getProofName());
                    }
                    list.add(0,"Select Proof");

                    CommonUtils.setSpinner(binding.titleSpinner, R.array.title);
                    CommonUtils.setSpinner(binding.genderSpinner, R.array.gender1);


                    binding.setData(new ApplicationModel("","1", UUID.randomUUID().toString().replaceAll
                            ("-", "").toUpperCase(),"","","","","","",
                            PrefrenceHelper.getPrefrenceStringValue(HomeScreen.this, PrefrenceKeyConstant.PHONE_NO),
                            PrefrenceHelper.getPrefrenceStringValue(HomeScreen.this, PrefrenceKeyConstant.EMAIL_ID),
                            "","","","","","","","",
                            "","","","","","","",
                            "","","","","","","","","","",
                            "","","0","0","0","","","0","0","0","0",
                            "0","0","","","","","I","","","","",
                            "","","","","","","","","",
                            "","","","","",""));

                }
                else CommonUtils.showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
            }



            @Override
            public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                CommonUtils.dismissLoadingDialog();
                CommonUtils.showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(HomeScreen.this, MainActivity.class));
        } else if (v.getId() == R.id.back) {
            startActivity(new Intent(HomeScreen.this, MainActivity.class));
        } else if (v.getId() == R.id.tieDOB) {
            datePicker();
        } else if (v.getId() == R.id.next) {
            if (checkValidation()) {
                AadharCard();
            }
        } else if (v.getId() == R.id.clear) {
            clearText();
        }

    }


    private void clearText() {
        binding.tieappid.getText().clear();
        binding.tieAadharNumber.getText().clear();
        binding.tieFirstName.getText().clear();
        binding.tieMiddleName.getText().clear();
        binding.tieLastName.getText().clear();
        binding.tieFatherName.getText().clear();
//        binding.tieMobileNo.getText().clear();
//        binding.tieEmailId.getText().clear();
        binding.tieAddress.getText().clear();
        binding.tieTextPostalCode.getText().clear();
        binding.tiePhoneNo.getText().clear();
        binding.tieDOB.getText().clear();
        binding.radioGroup.clearCheck();
        binding.genderSpinner.setSelection(0);
        binding.titleSpinner.setSelection(0);
        binding.tieSpinnerPostalCode.setSelection(0);
        binding.PostalAdd.setText(null);
        binding.tieTextVillage.getText().clear();
        binding.tieTextVia.getText().clear();
        binding.tieTextHouse.getText().clear();
        binding.tvFromTehsil.setSelection(0);
        binding.tvFromStop.setSelection(0);
    }

    private void startNewActivity(Class className) {
        startActivity(new Intent(this,className));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat formattedDate=new SimpleDateFormat("dd/MM/yyyy");
        try {
            Calendar selectedDate=Calendar.getInstance();
            selectedDate.set(year,month,dayOfMonth);

            if(calendar.get(Calendar.YEAR)-selectedDate.get(Calendar.YEAR)<=5){
                binding.tilDOB.setErrorEnabled(true);
                binding.tilDOB.setError("Please select valid dob");
            }
            else{
                binding.tilDOB.setErrorEnabled(false);
//                binding.getData().setDob(formattedDate.format(formattedDate.parse(dayOfMonth+"/"+month+"/"+year)));
                binding.tieDOB.setText(formattedDate.format(formattedDate.parse(dayOfMonth+"/"+(month +1)+"/"+year)));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        if (editText.getId() == R.id.tieAadharNumber) {
            binding.tilAadharNumber.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieFirstName) {
            binding.tilFirstName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieMiddleName) {
            binding.tilMiddleName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieLastName) {
            binding.tilLastName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieFatherName) {
            binding.tilFatherName.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieDOB) {
            binding.tilDOB.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieMobileNo) {
            binding.tilMobileNo.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tiePhoneNo) {
            binding.tilPhoneNo.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieEmailId) {
            binding.tilEmailId.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tieAddress) {
            binding.tilAddress.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_text_Village) {
            binding.tilVillage.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_text_Via) {
            binding.tilVia.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_text_House) {
            binding.tilHouse.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_text_State) {
            binding.tilState.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_text_PostalCode) {
            binding.tilPostalCode.setErrorEnabled(false);
        } else if (editText.getId() == R.id.tie_Spinner_PostalCode) {
            binding.tilPostalCode.setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }}