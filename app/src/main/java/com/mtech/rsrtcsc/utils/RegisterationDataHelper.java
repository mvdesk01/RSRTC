package com.mtech.rsrtcsc.utils;

import com.mtech.rsrtcsc.model.request.ApplicationModel;
import com.mtech.rsrtcsc.model.request.PoliceApplicationModel;

public class RegisterationDataHelper {

    private ApplicationModel model;
    private PoliceApplicationModel policeApplicationModel;
    public static RegisterationDataHelper registerationDataHelper;
    private RegisterationDataHelper(){

    }

    public  static RegisterationDataHelper getInstance(){
        if(registerationDataHelper==null) registerationDataHelper=new RegisterationDataHelper();
        return registerationDataHelper;
    }

    public void setApplicationData(ApplicationModel model){
        this.model=model;
    }

    public void setPoliceApplicationModel(PoliceApplicationModel policeApplicationModel){
        this.policeApplicationModel=policeApplicationModel;
    }

    public ApplicationModel getApplicationData(){
         return model;
    }

    public PoliceApplicationModel getPoliceApplicationModel(){
        return policeApplicationModel;
    }
}
