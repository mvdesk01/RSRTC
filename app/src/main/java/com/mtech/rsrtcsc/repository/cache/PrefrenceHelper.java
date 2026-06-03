package com.mtech.rsrtcsc.repository.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.mtech.rsrtcsc.model.request.RechargeRequest;
import com.mtech.rsrtcsc.model.response.AuthModel;

public class PrefrenceHelper {

    public static void saveUserData(Context context, AuthModel model){
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.EMAIL_ID,model.getEmailId()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.PHONE_NO,model.getMobileNo()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.FULL_NAME,model.getUserName()).apply();
       // writePrefrencesValue(context).putString(PrefrenceKeyConstant.IMAGE,model.getImage()).apply();

    }
/*
*/

    public static void saveRechargeData(Context context, RechargeRequest model){
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.REGISTRATIONID,model.getRegistrationID()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.BASEFARE,model.getBaseFare()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ACAMOUNT,model.getAcAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.TOLLAMOUNT,model.getTollAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.OCTAMOUNT,model.getOctAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.HRAMOUNT,model.getHrAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ITAMOUNT,model.getItAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.TOTALAMOUNT,model.getTotalAmount()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.EXPIRYDATE,model.getExpireDate()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.CARDAUTOID,model.getCardAutoID()).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.PERIODRANGE,model.getPeriodRange()).apply();




    }



    public static String getPrefrenceStringValue(Context context, String KEY){
        return  context.getSharedPreferences(PrefrenceKeyConstant.PREF_NAME, Context.MODE_PRIVATE).getString(KEY, "");
    }

    public static int getPrefrenceIntValue(Context context, String KEY){
        return  context.getSharedPreferences(PrefrenceKeyConstant.PREF_NAME, Context.MODE_PRIVATE).getInt(KEY, 0);
    }

    public static SharedPreferences.Editor writePrefrencesValue(Context context){
        return context.getSharedPreferences(PrefrenceKeyConstant.PREF_NAME, Context.MODE_PRIVATE).edit();
    }


    public static void saveConcessionDetail(Context context, String MM, String ED, String SELECTED_1, String SELECTED_2, String SELECTED_3) {
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.MM,MM).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.ED,ED).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_1,SELECTED_1).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_2,SELECTED_2).apply();
        writePrefrencesValue(context).putString(PrefrenceKeyConstant.SELECTED_3,SELECTED_3).apply();

    }
}
