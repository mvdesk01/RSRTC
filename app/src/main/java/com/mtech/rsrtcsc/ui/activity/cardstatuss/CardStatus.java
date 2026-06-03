package com.mtech.rsrtcsc.ui.activity.cardstatuss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.repository.local.ConnectionHelper;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CardStatus extends AppCompatActivity {

    Button btn_show;
    EditText field_edit;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_status);

        btn_show = findViewById(R.id.btnshow);
        field_edit = findViewById(R.id.tieEmail);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name();
                startActivity(new Intent(CardStatus.this, CardStatusView.class));
            }
        });
    }

    private void name() {
        try {
            ConnectionHelper helper = new ConnectionHelper();
            Connection connect = helper.connectionClass();
            if (connect != null) {
                String query = "Select mm.ApplicantID,mm.FName + ' ' + mm.MName + ' ' + mm.LName as C_NAME,convert(varchar, mm.CreatedOn, 103) as Reg_Date, \n" +
                        "mm.MobileNo,mm.PaymentStatus,mm.TransactionId,Case When DStatus='D' and mm.Application_Status='Reject' then 'Duplicate Reject. Reapply Duplicate' else mm.Application_Status end as Application_Status,  \n" +
                        "case when mm.DEPOTID = 0 then 'Deliver to Home' else md.Depot_Name end as Depot,  \n" +
                        "Case When mm.Application_Status='Approve' then Case when MCD.StatusId=2 and DispatchNo is null then 'Printed. Ready to dispatched' when MCD.StatusId=2 and DispatchNo is not null then 'Dispatched' else 'In-Process' end else '' end as Card_Status,\n" +
                        "Case When mm.Application_Status='Approve' then MCD.Card_Auto_ID else '' end as Card_Auto_ID,MM.cardfees,mr.ReasonName+', '+rr.REMARK as Remark \n" +
                        "from MMemberNewRegistration mm Left Join MtechCardDetails MCD On MCD.Registration_Id=MM.Registration_No  \n" +
                        "left join Master_Depot md on mm.DEPOTID = md.Depot_Num  \n" +
                        "Left Join Master_Consession mc on ltrim(rtrim(mm.ConcessionCode))= ltrim(rtrim(mc.Concession_Code))  \n" +
                        "Left Join REJECT_REASON RR On RR.REQUEST_ID=mm.ApplicantID Left join Master_Reason mr on mr.ReasonID=RR.Reason_ID  \n" +
                        "where mc.busTypeCd = 'EXP' and (mm.MobileNo ='"+RegisterationDataHelper.getInstance().getApplicationData().getMobileNo()+"' or MM.PoliceEmployeeID='"+field_edit+"' or MM.ApplicantID='"+RegisterationDataHelper.getInstance().getApplicationData().getApplicantID()+"') and Application_Status in('Pending','Reject')\n";
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    String id = rs.getString("MobileNo");
                    field_edit.setText(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
