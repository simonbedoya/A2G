package com.monitoreosatelitalgps.a2g.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by sbv23 on 17/10/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private List<VehiculoMap> vehiculoMapList;
    private Context context;


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.txtPlate)
        TextView plate;
        @Bind(R.id.iconStatus)
        ImageView iconStatus;
        @Bind(R.id.cardVehicle)
        CardView cardView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position);
                }
            }
        }
    }

    public RecyclerAdapter(Context context,List<VehiculoMap> vehiculoMapList) {
        this.context = context;
        this.vehiculoMapList = vehiculoMapList;
    }

    public void setListVehicles(List<VehiculoMap> listVehicles){
        this.vehiculoMapList = listVehicles;
    }

    @Override
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_recycler, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerViewHolder holder, int position) {
        holder.plate.setText(vehiculoMapList.get(position).getPlk());
        VehiculoMap vehiculoMap = vehiculoMapList.get(position);
        if(vehiculoMap.getCodeCarStatus().endsWith("PAN")){
            holder.iconStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_report_problem));
        }else if(vehiculoMap.getCodeDeviceStatus().equals("NOR") || vehiculoMap.getCodeDeviceStatus().equals("ERR")){
            holder.iconStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_portable_wifi_off));
        }else if(vehiculoMap.getCodeDeviceStatus().equals("ACT") && vehiculoMap.getCodeCarStatus().equals("ING") && vehiculoMap.getTrk() == 0.0){
            holder.iconStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_remove_circle));
        }else if(vehiculoMap.getCodeDeviceStatus().equals("ACT") && vehiculoMap.getCodeCarStatus().equals("OFF")){
            holder.iconStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_gps_off));
        }else{
            holder.iconStatus.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_check_circle));
        }
        holder.cardView.setBackground(ContextCompat.getDrawable(context,R.drawable.background_list));

    }

    @Override
    public int getItemCount() {
        return vehiculoMapList.size();
    }


}
