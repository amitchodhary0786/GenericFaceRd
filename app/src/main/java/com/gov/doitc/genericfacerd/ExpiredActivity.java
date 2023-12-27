package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gov.doitc.genericfacerd.adepter.SuccessViewAdepter;
import com.gov.doitc.genericfacerd.model.TransactionModel;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpiredActivity extends AppCompatActivity {

    SharedPreferences sharedPreferencesdepartment;
    String ssoLoginTokan,ExpiredStatusId;
    RecyclerView rv_success;
    ArrayList<TransactionModel> transactionArray = new ArrayList<>();
    ImageView iv_nodata,iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expired);

        rv_success = findViewById(R.id.rv_successtransaction);
        iv_nodata = findViewById(R.id.iv_nodata);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        //SHARED PREF
        sharedPreferencesdepartment = getApplicationContext().getSharedPreferences("GenericFaceAuthLogInTokan0010", MODE_PRIVATE);
        ssoLoginTokan = sharedPreferencesdepartment.getString("ssoLoginTokan", "null");

        ExpiredStatusId = getIntent().getStringExtra("ExpiredStatusId");

        getPendingData(ExpiredStatusId,ssoLoginTokan,ssoLoginTokan);
    }


    public void getPendingData(String SuccessStatusId,String SuccessStatusId1,String ssoLoginTokan){
        WebServiceHandler webServiceHandler =new WebServiceHandler(ExpiredActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response)  {
                Log.e("SUCCESSRESP",response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            String IsSuccess = json.getString("IsSuccess");
                            String Message = json.getString("Message");
                            if(IsSuccess.equals("true")){
                                JSONArray jsonArray = json.getJSONArray("Data");
                                if(jsonArray.length() == 0){
                                    iv_nodata.setVisibility(View.VISIBLE);
                                    rv_success.setVisibility(View.GONE);
                                }
                                else {
                                    iv_nodata.setVisibility(View.GONE);
                                    rv_success.setVisibility(View.VISIBLE);
                                }
                                for(int i = 0; i<jsonArray.length();i++){
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                    String AadhaarRefNo = jsonObject2.getString("AadhaarRefNo");
                                    String Purpose = jsonObject2.getString("Purpose");
                                    String RequestDate = jsonObject2.getString("RequestDate");
                                    String RequestId = jsonObject2.getString("RequestId");
                                    String RequestType = jsonObject2.getString("RequestType");
                                    String SubauaName = jsonObject2.getString("SubauaName");

                                    TransactionModel transactionModel = new TransactionModel();
                                    transactionModel.setAadhaarRefNo(AadhaarRefNo);
                                    transactionModel.setPurpose(Purpose);
                                    transactionModel.setRequestDate(RequestDate);
                                    transactionModel.setRequestId(RequestId);
                                    transactionModel.setRequestType(RequestType);
                                    transactionModel.setSubauaName(SubauaName);
                                    transactionArray.add(transactionModel);
                                    // Log.e("respss",District);

                                }

                                rv_success.setHasFixedSize(true);
                                StaggeredGridLayoutManager gridLayoutManager =
                                        new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                                rv_success.setLayoutManager(gridLayoutManager);
                                SuccessViewAdepter successViewAdepter = new SuccessViewAdepter(getApplicationContext(),transactionArray);
                                rv_success.setAdapter(successViewAdepter);
                                successViewAdepter.myClickListeners = new SuccessViewAdepter.MyClickListeners() {
                                    @Override
                                    public void onItemClick(int position) {
                                        String AadhaarRefNo = transactionArray.get(position).getAadhaarRefNo();
                                        String Purpose = transactionArray.get(position).getPurpose();
                                        String RequestDate = transactionArray.get(position).getRequestDate();
                                        String RequestId = transactionArray.get(position).getRequestId();
                                        String RequestType = transactionArray.get(position).getRequestType();
                                        String SubauaName = transactionArray.get(position).getSubauaName();
                                        showDialog(AadhaarRefNo,Purpose,RequestDate,RequestId,RequestType,SubauaName);
                                    }
                                };
                            }
                            else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };

        webServiceHandler.getPendingData(SuccessStatusId,SuccessStatusId,ssoLoginTokan);
    }

    public void showDialog(String AadhaarRefNo,String Purpose,String RequestDate,String RequestId,String RequestType,String SubauaName){
        Dialog dialog = new Dialog (ExpiredActivity.this );
        TextView tv_timestamp,tv_reqtype,tv_reqid,tv_purpose,tv_deptname,tv_aadhaarnum;

        dialog.setContentView (R.layout.success_dialog);
        tv_timestamp = dialog.findViewById(R.id.tv_timestamp);
        tv_reqtype = dialog.findViewById(R.id.tv_reqtype);
        tv_reqid = dialog.findViewById(R.id.tv_reqid);
        tv_purpose = dialog.findViewById(R.id.tv_purpose);
        tv_deptname = dialog.findViewById(R.id.tv_deptname);
        tv_aadhaarnum = dialog.findViewById(R.id.tv_aadhaarnum);

        tv_timestamp.setText(RequestDate);
        tv_reqtype.setText(RequestType);
        tv_reqid.setText(RequestId);
        tv_purpose.setText(Purpose);
        tv_deptname.setText(SubauaName);
        tv_aadhaarnum.setText(AadhaarRefNo);



        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        Button btn_verify = dialog.findViewById(R.id.btn_ok);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });

        dialog.show();
    }
}