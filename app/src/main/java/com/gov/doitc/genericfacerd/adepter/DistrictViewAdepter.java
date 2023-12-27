package com.gov.doitc.genericfacerd.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gov.doitc.genericfacerd.R;
import com.gov.doitc.genericfacerd.model.Districtpojo;

import java.util.ArrayList;

public class DistrictViewAdepter extends RecyclerView.Adapter<DistrictViewAdepter.ViewHolder> {
    private ArrayList<Districtpojo> mData;
    private LayoutInflater mInflater;
    public MyClickListeners myClickListeners;




    // data is passed into the constructor
    public DistrictViewAdepter(Context context, ArrayList<Districtpojo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.district_master, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Districtpojo animal = mData.get(position);
      //  Log.e("dis",animal.getDistrictname());
        holder.name.setText(animal.getDistrictname());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        LinearLayout ll_nextscreen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_name);
            //  propertyvalue = itemView.findViewById(R.id.tv_propertyvalue);
            ll_nextscreen = itemView.findViewById(R.id.ll_nextscreen);
            ll_nextscreen.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            myClickListeners.onItemClick(getAdapterPosition());
        }
    }
    public interface MyClickListeners {

        void onItemClick(int position);
    }
}
