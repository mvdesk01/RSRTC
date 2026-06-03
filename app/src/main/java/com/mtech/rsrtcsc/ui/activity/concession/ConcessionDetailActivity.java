package com.mtech.rsrtcsc.ui.activity.concession;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityConcessionDetailBinding;
import com.mtech.rsrtcsc.model.request.ConcessionName;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.model.request.StopModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.calculation.FixPassFareCalculation;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.ui.activity.route.SelectRouteActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConcessionDetailActivity extends BaseActivity<ActivityConcessionDetailBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RSRTCInterface apiInterface = new RSRTCConnection().createServiceRoute();
    private RSRTCInterface apiInterfaceRSTC = new RSRTCConnection().createService();
    private String concessStdPassenger;
    private String globalvariable;
    private Context context;
    private String name;
    private String concode;
    private List<SpinnerDataModel> concessionCodeList = new ArrayList<>();
    private List<String> concessionCodeList1 = new ArrayList<>();
    private ArrayList<String> list1 = new ArrayList<>();
    private List<SpinnerDataModel> concessionCodeListFInal = new ArrayList<SpinnerDataModel>();
    ArrayList<String> concessionCode = new ArrayList<String>();
    ArrayList<String> concessionName = new ArrayList<String>();
    String code;
    String pass;
    String SpinnerName;
    private List<SpinnerDataModel> concodename= new ArrayList();
    String rangecode;
    private List<SpinnerDataModel> passPeriod= new ArrayList<>();


    @Override
    protected ActivityConcessionDetailBinding getActivityBinding() {
        return ActivityConcessionDetailBinding.inflate(getLayoutInflater());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void init() {
        CommonUtils.setSpinner(binding.spinnerBusType, R.array.bus_type);
        CommonUtils.setSpinner(binding.spinnerBusType2, R.array.bus_type);
        GetAllConcessionMaster();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void initCtrl() {
        binding.ivHumberger.setOnClickListener(this);
        binding.conCodeSpinner.setOnItemSelectedListener(this);
        binding.spinnerBusType.setOnItemSelectedListener(this);
        binding.spinnerBusType2.setOnItemSelectedListener(this);
        binding.searchRoute.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.tvFromStop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postStopNameApi(binding.tvFromStop.getText().toString().trim(),"from");
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        binding.tvTillStop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postStopNameApi(binding.tvTillStop.getText().toString().trim(),"till");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void postStopNameApi(String stop,String type) {
        apiInterface.stopName(new StopModel(stop)).enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    for(SpinnerDataModel model: response.body()){
                        List<SpinnerDataModel> responseList = new ArrayList<SpinnerDataModel>();
                        List<String> responseListString = new ArrayList<String>();
                        responseList.addAll((List<SpinnerDataModel>)response.body());
                        for(int i =0; i< responseList.size();i++ ){
                            SpinnerDataModel spinnerDataModel = responseList.get(i);
                            responseListString.add(spinnerDataModel.getBusStopName());
                        }
                        if(type.equalsIgnoreCase("from")) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConcessionDetailActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, responseListString);
                            binding.tvFromStop.setAdapter(adapter);
                            binding.tvFromStop.showDropDown();
                            CommonUtils.dismissLoadingDialog();
                            binding.tvFromStop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i(TAG,"Position was" +position);
                                    String pos = parent.getItemAtPosition(position).toString();
                                    String[] separated = pos.split("\\(");
                                    RegisterationDataHelper.getInstance().getApplicationData().setFromStop(pos);
                                    RegisterationDataHelper.getInstance().getApplicationData().setFromStopVal(separated[0]);
                                }
                            });
                            break;
                        }
                        else{
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConcessionDetailActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, responseListString);
                            binding.tvTillStop.setAdapter(adapter);
                            binding.tvTillStop.showDropDown();
                            CommonUtils.dismissLoadingDialog();
                            binding.tvTillStop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Log.i(TAG,"Position was" +position);
                                    String pos = parent.getItemAtPosition(position).toString();
                                    String[] separated = pos.split("\\(");
                                    RegisterationDataHelper.getInstance().getApplicationData().setTillStop(pos);
                                    RegisterationDataHelper.getInstance().getApplicationData().setTillStopVal(separated [0]);

                                }
                            });
                            break;

                        }
                    }
                }else{
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

    private void GetAllConcessionMaster() {
        apiInterfaceRSTC.GetAllConcessionMaster().enqueue(new Callback<List<SpinnerDataModel>>() {
            @Override
            public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                if(response.isSuccessful()){
                    concodename=response.body();
                    List<String> code = new ArrayList<String>();
                    List<String> name = new ArrayList<String>();
                    for(int i=0;i<response.body().size();i++)
                    {
                        name.add(response.body().get(i).getConcessionName());
                        code.add(response.body().get(i).getConcessionCode());
                    }

                    for(int i =0; i<name.size();i++) {
                        concessionCodeListFInal.add(new SpinnerDataModel(name.get(i), code.get(i)));
                    }
                    name.add(0,"Select Concession");
                    CommonUtils.setSpinner(binding.conCodeSpinner,name);
                    // CommonUtils.setSpinner(binding.conCodeSpinner,code);
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


    public  void getSpinnerpass(){
        binding.conCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerName = binding.conCodeSpinner.getSelectedItem().toString();
                if (position != 0) {
                    code=concessionCodeListFInal.get(position-1).getConcessionCode();
                    RegisterationDataHelper.getInstance().getApplicationData().setConcessionCode(code);
                }
                apiInterfaceRSTC.GetSpecificConcessionTypeMaster(new ConcessionName(code)).enqueue(new Callback<List<SpinnerDataModel>>() {
                    @Override
                    public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                        CommonUtils.dismissLoadingDialog();
                        if(response.isSuccessful()){
                            passPeriod = response.body();
                            List<String> listpass=new ArrayList<>();
                            List<String> listcon=new ArrayList<>();
                            for(SpinnerDataModel model: response.body()){
                                listpass.add(model.getRangePeriod());
                                listcon.add(model.getConcessionTypeName());
                                CommonUtils.setSpinner(binding.conTypeSpinner,listcon);
                                CommonUtils.setSpinner(binding.pasPeriod,listpass);
                                RegisterationDataHelper.getInstance().getApplicationData().setConcessionType(model.getConcessionTypeId());
                                rangecode=model.getRangePeriod();
                            }

                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.YEAR, Integer.parseInt(rangecode));
                            String str = format.format(cal.getTime());
                            RegisterationDataHelper.getInstance().getApplicationData().setPassPeriod(""+listpass);
                            RegisterationDataHelper.getInstance().getApplicationData().setPassValidity(""+listpass);
                            RegisterationDataHelper.getInstance().getApplicationData().setExpiryDate(str);

                            if(SpinnerName.equals("STUDENT PASSENGER") || SpinnerName.equals("VARISHTHA NAGRIK") || SpinnerName.equals("VARISHTHA NAGRIK  (80 YRS AND ABOVE)")){
                                validat(SpinnerName);
                            }


                        }else{
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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        if (parent.getId() == R.id.con_code_spinner) {
            getSpinnerpass();
        } else if (parent.getId() == R.id.pas_period) {
            binding.pasPeriod.setEnabled(false);
        } else if (parent.getId() == R.id.spinner_bus_type) {
            binding.spinnerBusType.setEnabled(false);
            RegisterationDataHelper.getInstance().getApplicationData().setBusType("EXP");
        } else if (parent.getId() == R.id.spinner_bus_type2) {
            checkBusType(parent.getItemAtPosition(position).toString());
        }


    }

    private void checkBusType(String type) {
        switch (type){
            case "EXPRESS": RegisterationDataHelper.getInstance().getApplicationData().setBusType("EXP"); break;
            case "ORDINARY": RegisterationDataHelper.getInstance().getApplicationData().setBusType("ORD"); break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDateSet() {
        binding.datePick.setText(getExpDate());
        RegisterationDataHelper.getInstance().getApplicationData().setExpiryDate(getExpDate());
    }

    private String getExpDate(){
        String finalDate;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (month>4){

            calendar.add(Calendar.YEAR,1);

            String YEAR = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(calendar.getTime());

            finalDate = "31/05/"+YEAR;

        }else {

            String YEAR = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(calendar.getTime());

            finalDate = "31/05/"+YEAR;

        }

        return finalDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validat(String type) {
        concode = type;
        switch (type){
            case "STUDENT PASSENGER":
                binding.llSelectStop.setVisibility(View.VISIBLE);
//                binding.tvFromStop.getText().clear();
//                binding.tvTillStop.getText().clear();

//                EditText fromEdt = findViewById(R.id.tv_from_stop);
//                EditText toEdt = findViewById(R.id.tv_till_stop);
//                String fromStop = fromEdt.getText().toString();
//                String toStop = toEdt.getText().toString();
//
//                String frStop = binding.tvFromStop.getText().toString();
//                String tStop = binding.tvTillStop.getText().toString();
//
//                SpinnerDataModel.setFromStop(fromStop);
//                SpinnerDataModel.setTillStop(toStop);

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
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (binding.conCodeSpinner.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.conCodeSpinner, "Please select concession name");
            } else if (SpinnerName.equals("VARISHTHA NAGRIK")) {
                if (getAgeDifference() < Double.parseDouble(PrefrenceKeyConstant.AGE)) {
                    Toast.makeText(this, "VARISHTHA NAGRIK age should be 60 or more than 60", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, FixPassFareCalculation.class).putExtra("CONCESSION_NAME", code));
                }
            } else if (SpinnerName.equals("VARISHTHA NAGRIK  (80 YRS AND ABOVE)")) {
                if (getAgeDifference80() < Double.parseDouble(PrefrenceKeyConstant.vAGE)) {
                    Toast.makeText(this, "VARISHTHA NAGRIK  (80 YRS AND ABOVE) age should be 80 or more than 80", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, FixPassFareCalculation.class).putExtra("CONCESSION_NAME", code));
                }
            } else {
                startActivity(new Intent(this, FixPassFareCalculation.class).putExtra("CONCESSION_NAME", code));
            }
        } else if (v.getId() == R.id.search_Route) {
            if (concode.equals("STUDENT PASSENGER")) {
                if (binding.tvFromStop.getText().toString().isEmpty() || binding.tvTillStop.getText().toString().isEmpty()) {
                    CommonUtils.showSnackBar(binding.getRoot(), "Please Enter Stops!");
                } else {
                    EditText fromEdt = findViewById(R.id.tv_from_stop);
                    EditText toEdt = findViewById(R.id.tv_till_stop);
                    String fromStop = fromEdt.getText().toString();
                    String toStop = toEdt.getText().toString();

                    RegisterationDataHelper.getInstance().getApplicationData().setFromStop(fromStop);
                    RegisterationDataHelper.getInstance().getApplicationData().setTillStop(toStop);

                    startActivity(new Intent(this, SelectRouteActivity.class).putExtra("CONCESSION_NAME", code));
                }
            }
        } else if (v.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(ConcessionDetailActivity.this, MainActivity.class));
        }

    }

    public static class TimeAgo {
        public long ConvertTimeToDays(String dataDate) {
            String convTime = null;
            String suffix = "ago";
            long days = 0;

            try {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date pastTime = simpleDateFormat.parse(dataDate);

                Date nowTime = new Date();

                assert pastTime != null;
                long dateDiff = nowTime.getTime() - pastTime.getTime();
                days = TimeUnit.MILLISECONDS.toDays(dateDiff);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return days;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getAgeDifference(){
        TimeAgo timeAgo = new TimeAgo();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(RegisterationDataHelper.getInstance().getApplicationData().getDob());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String TIME = simpleDateFormat.format(date);

        int difference = (int) (timeAgo.ConvertTimeToDays(TIME)/360);
        return difference;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getAgeDifference80(){
        TimeAgo timeAgo = new TimeAgo();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(RegisterationDataHelper.getInstance().getApplicationData().getDob());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String TIME = simpleDateFormat.format(date);

        int difference = (int) (timeAgo.ConvertTimeToDays(TIME)/360);
        return difference;
    }

    public void onBackPressed(){
        super.onBackPressed();
    }
}