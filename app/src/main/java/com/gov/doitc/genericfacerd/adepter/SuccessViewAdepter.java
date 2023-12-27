package com.gov.doitc.genericfacerd.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gov.doitc.genericfacerd.R;
import com.gov.doitc.genericfacerd.model.TransactionModel;

import java.util.ArrayList;

public class SuccessViewAdepter extends RecyclerView.Adapter<SuccessViewAdepter.ViewHolder> {
    private ArrayList<TransactionModel> mData;
    private LayoutInflater mInflater;
    public MyClickListeners myClickListeners;

    // data is passed into the constructor
    public SuccessViewAdepter(Context context, ArrayList<TransactionModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @NonNull
    @Override
    public SuccessViewAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.success_layout, parent, false);
        return new SuccessViewAdepter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuccessViewAdepter.ViewHolder holder, int position) {
        TransactionModel animal = mData.get(position);
        //  Log.e("dis",animal.getDistrictname());
        holder.tv_aadhaarnum.setText(animal.getAadhaarRefNo());
        holder.tv_datetime.setText(animal.getRequestDate());
        holder.tv_deptname.setText(animal.getSubauaName());
        holder.tv_reqtype.setText(animal.getRequestType());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_aadhaarnum,tv_datetime,tv_deptname,tv_reqtype;
        CardView cv_success;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_aadhaarnum = itemView.findViewById(R.id.tv_aadhaarnum);
            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tv_deptname = itemView.findViewById(R.id.tv_deptname);
            tv_reqtype = itemView.findViewById(R.id.tv_reqtype);
            cv_success = itemView.findViewById(R.id.cv_success);
            cv_success.setOnClickListener(this);
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
