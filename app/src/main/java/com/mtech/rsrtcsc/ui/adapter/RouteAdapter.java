package com.mtech.rsrtcsc.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mtech.rsrtcsc.R;
import com.mtech.rsrtcsc.databinding.AdapterRouteBinding;
import com.mtech.rsrtcsc.model.request.RouteModel;
import com.mtech.rsrtcsc.repository.cache.PrefrenceKeyConstant;
import com.mtech.rsrtcsc.ui.activity.calculation.FixPassFareCalculation;
import com.mtech.rsrtcsc.ui.activity.route.SelectRouteActivity;
import com.mtech.rsrtcsc.utils.RegisterationDataHelper;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private Context context;
    public String globalvariables;
    public  String km2= "50.00";
    private List<RouteModel> list = new ArrayList<>();

    /*public RouteAdapter(Context context, List<RouteModel> list, String gv) {
        this.context = context;
        this.list = list;
        this.globalvariables = gv;
    }*/

    public RouteAdapter(SelectRouteActivity context, List<com.mtech.rsrtcsc.model.request.RouteModel> body, String globalvariables) {
        this.context = context;
        this.list = body;
        this.globalvariables = globalvariables;
    }


    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterRouteBinding.inflate(LayoutInflater.from(context),parent,false));

    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        holder.binding.setData(list.get(position));
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;


    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterRouteBinding binding;

        public ViewHolder(@NonNull AdapterRouteBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.cvMain.setOnClickListener(this);


        }



        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cvMain) {
                Bundle b = new Bundle();
                RegisterationDataHelper.getInstance().getApplicationData().setRouteNo(list.get(getAdapterPosition()).getRouteNo());
                RegisterationDataHelper.getInstance().getApplicationData().setRouteName(list.get(getAdapterPosition()).getRouteName());
                RegisterationDataHelper.getInstance().getApplicationData().setKm(list.get(getAdapterPosition()).getKm());
                RegisterationDataHelper.getInstance().getApplicationData().setDepot(list.get(getAdapterPosition()).getDepotCd());

                if (Double.parseDouble(RegisterationDataHelper.getInstance().getApplicationData().getKm()) <= Double.parseDouble(PrefrenceKeyConstant.KM)) {
                    b.putSerializable("rootData", list.get(getAdapterPosition()));
                    Intent i = new Intent(context, FixPassFareCalculation.class);
                    i.putExtra("CONCESSION_NAME", globalvariables);
                    i.putExtras(b);
                    v.getContext().startActivity(i);
                } else {
                    Toast.makeText(context.getApplicationContext(),
                            "Your selected 'Distance is greater than 75.' Please select another depot ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
