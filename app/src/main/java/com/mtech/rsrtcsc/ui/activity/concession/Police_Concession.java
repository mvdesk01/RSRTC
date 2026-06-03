package com.mtech.rsrtcsc.ui.activity.concession;

import static java.util.Calendar.DAY_OF_MONTH;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.RequiresApi;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.base.BaseActivity;
import com.mtech.rsrtcsc.databinding.ActivityPoliceConcessionBinding;
import com.mtech.rsrtcsc.model.request.ConcessionName;
import com.mtech.rsrtcsc.model.request.PoliceConcessionModel;
import com.mtech.rsrtcsc.model.request.SpinnerDataModel;
import com.mtech.rsrtcsc.repository.remote.RSRTCConnection;
import com.mtech.rsrtcsc.repository.remote.RSRTCInterface;
import com.mtech.rsrtcsc.ui.activity.calculation.Police_FixPassFareCalculation;
import com.mtech.rsrtcsc.ui.activity.main.MainActivity;
import com.mtech.rsrtcsc.utils.CommonUtils;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Police_Concession extends BaseActivity<ActivityPoliceConcessionBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener {


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
    String Con_Name;
    String s_YEAR;
    String strr = null;


    @Override
    protected ActivityPoliceConcessionBinding getActivityBinding() {
        return ActivityPoliceConcessionBinding.inflate(getLayoutInflater());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void init() {
        Intent intent = getIntent();
        Con_Name = intent.getStringExtra("message");
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

    }


    private void GetAllConcessionMaster() {
        apiInterfaceRSTC.GetPoliceConcession(new PoliceConcessionModel(Con_Name)).enqueue(new Callback<List<SpinnerDataModel>>() {
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
//                    name.add(1,"RAJASTHAN POLICE");
//                    name.add(2,"DELHI POLICE");
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
                    RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionCode(code);
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
                                RegisterationDataHelper.getInstance().getPoliceApplicationModel().setConcessionType(model.getConcessionTypeId());
                                rangecode=model.getRangePeriod();
                            }
                            SimpleDateFormat formattedDate=new SimpleDateFormat("dd/MM/yyyy");
                            Calendar cal = Calendar.getInstance();
                            int year = cal.get(Calendar.YEAR);
                            int month = cal.get(Calendar.MONTH);
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            cal.add(Calendar.YEAR, Integer.parseInt(rangecode));
                                try {
                                    strr = formattedDate.format(formattedDate.parse(day+"/"+(month+7)+"/"+year));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPassPeriod(""+listpass);
                            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setPassValidity(""+listpass);
                            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setExpiryDate(strr);


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
            RegisterationDataHelper.getInstance().getPoliceApplicationModel().setBusType("EXP");
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onDateSet() {
        binding.datePick.setText(getExpDate());
        RegisterationDataHelper.getInstance().getPoliceApplicationModel().setExpiryDate(getExpDate());
    }

    private String getExpDate(){
        String finalDate;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(DAY_OF_MONTH);


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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            if (binding.conCodeSpinner.getSelectedItemPosition() == 0) {
                CommonUtils.showSnackBar(binding.conCodeSpinner, "Please select concession name");
            } else {
                startActivity(new Intent(this, Police_FixPassFareCalculation.class).putExtra("CONCESSION_NAME", code));
            }
        } else if (v.getId() == R.id.btn_back) {
            onBackPressed();
        } else if (v.getId() == R.id.ivHumberger) {
            startActivity(new Intent(Police_Concession.this, MainActivity.class));
        }

    }


    public void onBackPressed(){
        super.onBackPressed();
    }
}