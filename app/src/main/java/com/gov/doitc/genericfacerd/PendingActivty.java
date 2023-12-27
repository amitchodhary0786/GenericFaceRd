package com.gov.doitc.genericfacerd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gov.doitc.genericfacerd.adepter.DistrictViewAdepter;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class PendingActivty extends AppCompatActivity {
    Button btn_auth;
    ImageView iv_back;
    TextView tv_aadhaarnum,tv_datetime,tv_refid;
    String pendingStatusId,pendingUserId;
    CardView cv_pending;
    SwipeRefreshLayout swipeRefreshLayout;

    String AadhaarRefNo,RequestDate,RequestId,SubauaName,RequestType,Purpose,CreatedDate;
    SharedPreferences sharedPreferencesdepartment;
    String ssoLoginTokan;
    ImageView iv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_activty);

        //SHARED PREF
        sharedPreferencesdepartment = getApplicationContext().getSharedPreferences("GenericFaceAuthLogInTokan0010", MODE_PRIVATE);
        ssoLoginTokan = sharedPreferencesdepartment.getString("ssoLoginTokan", "null");

        Log.e("TOKEN_PENDING",ssoLoginTokan);

        pendingStatusId = getIntent().getStringExtra("pendingStatusId");
        pendingUserId = getIntent().getStringExtra("pendingUserId");
        getPendingData(pendingStatusId,pendingUserId,ssoLoginTokan);

        tv_aadhaarnum = findViewById(R.id.tv_aadhaarnum);
        tv_datetime = findViewById(R.id.tv_datetime);
        tv_refid = findViewById(R.id.tv_refid);

        iv_nodata = findViewById(R.id.iv_nodata);

        cv_pending = findViewById(R.id.cv_pending);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPendingData(pendingStatusId,pendingUserId,ssoLoginTokan);
                //SsologinfunctionsDirect();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PendingActivty.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        btn_auth = findViewById(R.id.btn_auth);
        btn_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });
    }


    public void getPendingData(String pendingStatusId1,String pendingUserId,String ssoLoginTokan){
        WebServiceHandler webServiceHandler =new WebServiceHandler(PendingActivty.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response)  {
                Log.e("PENRESP",response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            String IsSuccess = json.getString("IsSuccess");
                            String Message = json.getString("Message");
                            if(IsSuccess.equals("true")){
                                iv_nodata.setVisibility(View.GONE);
                                cv_pending.setVisibility(View.VISIBLE);
                                JSONObject jsonObject = json.getJSONObject("Data");
                                 AadhaarRefNo = jsonObject.getString("AadhaarRefNo");
                                 Purpose = jsonObject.getString("Purpose");
                                 RequestDate = jsonObject.getString("RequestDate");
                                 RequestId = jsonObject.getString("RequestId");
                                 RequestType = jsonObject.getString("RequestType");
                                 SubauaName = jsonObject.getString("SubauaName");



                                tv_aadhaarnum.setText(AadhaarRefNo);
                                tv_datetime.setText(RequestDate);
                                tv_refid.setText(RequestId);


                            }
                            else {
                                cv_pending.setVisibility(View.GONE);
                                iv_nodata.setVisibility(View.VISIBLE);
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

        webServiceHandler.getPendingData(pendingStatusId1,pendingUserId,ssoLoginTokan);
    }

    public void showDialog(){
        Dialog dialog = new Dialog (PendingActivty.this );
        TextView tv_timestamp,tv_reqtype,tv_reqid,tv_purpose,tv_deptname,tv_aadhaarnum;

        dialog.setContentView (R.layout.authentication_dialog);
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

        CheckBox checkBox = dialog.findViewById(R.id.checkbox);
        checkBox.setText("I hereby state that i have no objection in authenticating myself with AADHAAR based authentication system and consent to providing my AADHAAR Number, and OTP for AADHAAR based Authentication. I also give my explicit consent for accessing the mobile number and email address from AADHAAR System. \n" +
                "मैं एतद्द्वारा कहता हूं कि मुझे आधार आधारित प्रमाणीकरण प्रणाली के साथ खुद को प्रमाणित करने में कोई आपत्ति नहीं है और मैं आधार आधारित प्रमाणीकरण के लिए अपना आधार नंबर और ओटीपी प्रदान करने के लिए सहमत हूं। मैं आधार प्रणाली से मोबाइल नंबर और ईमेल पते तक पहुंचने के लिए भी अपनी स्पष्ट सहमति देता हूं।");
        Button btn_verify = dialog.findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    dialog.cancel();
                    genrateFacePID();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Check Aadhaar consern",Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }
    public  String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.valueOf(number);
    }
    public void genrateFacePID() {
        String randomnumber = getRandomNumberString();
        try {
            String pidOption = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<PidOptions ver=\"1.0\" env=\"P\">\n" +
                    "   <Opts format=\"\" pidVer=\"2.0\" otp=\"\" wadh=\"\" />\n" +
                    "   <CustOpts>\n" +
                    "      <Param name=\"txnId\" value=\""+randomnumber+"\"/>\n" +
                    "      <Param name=\"purpose\" value=\"auth\"/>\n" +
                    "      <Param name=\"language\" value=\"en\"/>\n" +
                    "   </CustOpts>\n" +
                    "</PidOptions>";
            Intent intent2 = new Intent();
            intent2.setAction("in.gov.uidai.rdservice.face.CAPTURE");
            intent2.putExtra("request", pidOption);
            startActivityForResult(intent2, 123);
        } catch (Exception e) {
            //    Log.e("Error", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == Activity.RESULT_OK){
            try{
                if(data!=null){
                    String result = data.getStringExtra("response");
                    //display pid data
                    //tv_viewpid.setText(result);
                    // Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
                    setOutputText(result);

                }
            }
            catch (Exception e){
                Log.e("Error", "Error while deserialize pid data", e);
            }
        }
    }
    private void setOutputText(final String pidXML) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("PID",pidXML);
                sendPidDeta(pidXML,AadhaarRefNo,RequestId,ssoLoginTokan);
            }
        });
    }

    public void sendPidDeta(String PID,String AadhaarRefNo,String RequestId,String ssoLoginTokan){
        WebServiceHandler webServiceHandler =new WebServiceHandler(PendingActivty.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                //Log.e("FACERESP",response);

                JSONObject jsonObject1 = new JSONObject(response);
                String IsSuccess = jsonObject1.getString("IsSuccess");
                String Message = jsonObject1.getString("Message");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // et_aadhaar.setText(AadhaarId);
                        try{


                            if(IsSuccess.equals("true")) {
                                String Txn = jsonObject1.getString("Txn");
                               // Toast.makeText(PendingActivty.this, Txn, Toast.LENGTH_SHORT).show();
                                successFaceAuthDialog(Txn);

                            }
                            else {
                                failedFaceAuthDialog(Message);
                              //  Toast.makeText(PendingActivty.this, Message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){

                        }

                    }
                });

            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };

        webServiceHandler.sendPidDeta(PID,AadhaarRefNo,RequestId,ssoLoginTokan);
    }


    public void successFaceAuthDialog(String Txn) {
        Dialog customdialog = new Dialog (PendingActivty.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setText("Aadhaar Face Authenication Success");
        bodymsg.setText("Aadhaar Tokan: " +Txn);
        btn.setText("ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PendingActivty.this,DashboardActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();

            }
        });

        customdialog.show();


//        AlertDialog alertDialog = new AlertDialog.Builder(PendingActivty.this).create();
//        alertDialog.setTitle("Aadhaar FaceAuth Success");
//        alertDialog.setMessage("Aadhaar Tokan: " +Txn);
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//        alertDialog.setCancelable(false);
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(PendingActivty.this,DashboardActivity.class);
//                startActivity(i);
//                finish();
//                alertDialog.dismiss();
//
//
//            }
//        });
//        alertDialog.show();
    }

    public void failedFaceAuthDialog(String msg){
        Dialog customdialog = new Dialog (PendingActivty.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.faceauthfailed);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setTextColor(ContextCompat.getColor(PendingActivty.this,R.color.markout));
        titlemsg.setText("Aadhaar Face Authentication Failed");
        bodymsg.setTextColor(ContextCompat.getColor(PendingActivty.this,R.color.markout));
        bodymsg.setText("Message: " +msg);
        btn.setBackgroundColor(ContextCompat.getColor(PendingActivty.this,R.color.markout));
        btn.setTextColor(ContextCompat.getColor(PendingActivty.this,R.color.white));
        btn.setText("Ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PendingActivty.this,DashboardActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();

            }
        });

        customdialog.show();
    }

}