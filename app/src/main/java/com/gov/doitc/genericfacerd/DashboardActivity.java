package com.gov.doitc.genericfacerd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.google.android.material.snackbar.Snackbar;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView iv_pending,iv_success,iv_expired,iv_failed,iv_refresh,iv_logout;
    private SharedPreferences sharedPreferencesdepartment;
    String ssoLoginTokan,ssoid,UserId;
    TextView tv_pending,tv_success,tv_expired,tv_failed;
    String pendingStatusId,ExpiredStatusId,SuccessStatusId,FailedStatusId;
    String AadhaarRefNo,RequestDate,RequestId,SubauaName,RequestType,Purpose,CreatedDate;
    CardView cv_markin,cv_success,cv_expired,cv_failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        tv_pending = findViewById(R.id.tv_pending);
        tv_failed = findViewById(R.id.tv_failed);
        tv_expired = findViewById(R.id.tv_expired);
        tv_success = findViewById(R.id.tv_success);

        cv_success = findViewById(R.id.cv_success);
        cv_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,SuccessActivity.class);
                i.putExtra("SuccessStatusId",SuccessStatusId);
                startActivity(i);

            }
        });
        cv_failed = findViewById(R.id.cv_failed);
        cv_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,FailedActivity.class);
                i.putExtra("FailedStatusId",FailedStatusId);
                startActivity(i);

            }
        });
        cv_expired = findViewById(R.id.cv_expired);
        cv_expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,ExpiredActivity.class);
                i.putExtra("ExpiredStatusId",ExpiredStatusId);
                startActivity(i);

            }
        });


        //SHARED PREF
        sharedPreferencesdepartment = getApplicationContext().getSharedPreferences("GenericFaceAuthLogInTokan0010", MODE_PRIVATE);
        ssoLoginTokan = sharedPreferencesdepartment.getString("ssoLoginTokan", "null");
        ssoid = sharedPreferencesdepartment.getString("ssoid", "null");
        UserId = sharedPreferencesdepartment.getString("UserId", "null");

//        iv_success = findViewById(R.id.iv_success);
//        iv_success.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(DashboardActivity.this,SuccessActivity.class);
//                i.putExtra("SuccessStatusId",SuccessStatusId);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        iv_expired = findViewById(R.id.iv_exipred);
//        iv_expired.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(DashboardActivity.this,ExpiredActivity.class);
//                i.putExtra("ExpiredStatusId",ExpiredStatusId);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        iv_failed = findViewById(R.id.iv_failed);
//        iv_failed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(DashboardActivity.this,FailedActivity.class);
//                i.putExtra("FailedStatusId",FailedStatusId);
//                startActivity(i);
//                finish();
//            }
//        });

        iv_refresh = findViewById(R.id.iv_refresh);
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDashboardCount();
            }
        });
        iv_logout = findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog();
            }
        });

        iv_pending = findViewById(R.id.iv_pending);
        iv_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this,PendingActivty.class);
                i.putExtra("pendingStatusId",pendingStatusId);
                i.putExtra("pendingUserId",UserId);
                startActivity(i);
               // finish();
            }
        });



        //SsologinfunctionsDirect();

        getDashboardCount();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDashboardCount();
                //SsologinfunctionsDirect();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        cv_markin = findViewById(R.id.cv_markin);

    }



    public void getDashboardCount() {
        WebServiceHandler webServiceHandler =new WebServiceHandler(DashboardActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response)  {
                Log.e("DOB",response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            String IsSuccess = json.getString("IsSuccess");
                            String Message = json.getString("Message");
                            if(IsSuccess.equals("true")){
                                JSONObject jsonObject = json.getJSONObject("Data");

                                JSONArray jsonArray = jsonObject.getJSONArray("StatusList");
                                String pendingname = jsonArray.getJSONObject(4).getString("StatusName");
                                String pendingTotalCount = jsonArray.getJSONObject(4).getString("TotalCount");
                                pendingStatusId = jsonArray.getJSONObject(4).getString("StatusId");

                                if(pendingTotalCount.equals("1")){
                                    cv_markin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("User");
                                                AadhaarRefNo = jsonObject1.getString("AadhaarRefNo");
                                                Purpose = jsonObject1.getString("Purpose");
                                                RequestDate = jsonObject1.getString("RequestDate");
                                                RequestId = jsonObject1.getString("RequestId");

                                                RequestType = jsonObject1.getString("RequestType");
                                                SubauaName = jsonObject1.getString("SubauaName");

                                                Log.e("RequestId!!",RequestId);

                                                showDialog(AadhaarRefNo,Purpose,RequestDate,RequestId,RequestType,SubauaName);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });



//                                    tv_pending.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//
//                                            try {
//                                                JSONObject jsonObject1 = jsonObject.getJSONObject("User");
//                                                 AadhaarRefNo = jsonObject1.getString("AadhaarRefNo");
//                                                 Purpose = jsonObject1.getString("Purpose");
//                                                 RequestDate = jsonObject1.getString("RequestDate");
//                                                 RequestId = jsonObject1.getString("RequestId");
//
//                                                 RequestType = jsonObject1.getString("RequestType");
//                                                 SubauaName = jsonObject1.getString("SubauaName");
//
//                                                showDialog(AadhaarRefNo,Purpose,RequestDate,RequestId,RequestType,SubauaName);
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
//                                    });
                                }

                                String Expiredname = jsonArray.getJSONObject(2).getString("StatusName");
                                String ExpiredTotalCount = jsonArray.getJSONObject(2).getString("TotalCount");
                                ExpiredStatusId = jsonArray.getJSONObject(2).getString("StatusId");

                                String Successname = jsonArray.getJSONObject(0).getString("StatusName");
                                String SuccessTotalCount = jsonArray.getJSONObject(0).getString("TotalCount");
                                SuccessStatusId = jsonArray.getJSONObject(0).getString("StatusId");

                                String Failedname = jsonArray.getJSONObject(3).getString("StatusName");
                                String FailedTotalCount = jsonArray.getJSONObject(3).getString("TotalCount");
                                FailedStatusId = jsonArray.getJSONObject(3).getString("StatusId");

                                Log.e("FNAME",Failedname);

                                tv_pending.setText(pendingTotalCount);
                                tv_failed.setText(FailedTotalCount);
                                tv_expired.setText(ExpiredTotalCount);
                                tv_success.setText(SuccessTotalCount);

//                                for(int i = 0; i<jsonArray.length();i++){
//
//                                }


                            }
                            else {
                                SharedPreferences preferences =getSharedPreferences("GenericFaceAuthLogInTokan0010", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(DashboardActivity.this, SpleshNextActivity.class);
                                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
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

        webServiceHandler.getDashboardCount(ssoLoginTokan);
    }

    public void showDialog(String AadhaarRefNo,String Purpose,String RequestDate,String RequestId,String RequestType,String SubauaName){
        Dialog dialog = new Dialog (DashboardActivity.this );
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
                Log.e("RequestType",RequestType);
                if(checkBox.isChecked()){
                    dialog.cancel();

                   // genrateFacePID();
                    if(RequestType.equals("1")){
                        genrateFacePID();
                    }
                    else if(RequestType.equals("2")){
                       // Toast.makeText(DashboardActivity.this, "E-kyc Perform", Toast.LENGTH_SHORT).show();
                        genrateEkycFacePID();
                    }


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

    public void genrateEkycFacePID(){

        String randomnumber = getRandomNumberString();
        String pidOption = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<PidOptions ver=\"1.0\" env=\"P\">\n" +
                "   <Opts format=\"\" pidVer=\"2.0\" otp=\"\" wadh=\"HzHcM1lgshaAElEgZPz8LrzBeugY9KQ/NMuunxOxtSE=\" />\n" +
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
        WebServiceHandler webServiceHandler =new WebServiceHandler(DashboardActivity.this);
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
                              //  Toast.makeText(DashboardActivity.this, Txn, Toast.LENGTH_SHORT).show();
                                successFaceAuthDialog(Txn);

                            }
                            else {
                                failedFaceAuthDialog(Message);
                               // Toast.makeText(DashboardActivity.this, Message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            failedFaceAuthDialog(String.valueOf(e));
                        }

                    }
                });

            }

            @Override
            public void onFailer(String failure) throws JSONException {
                failedFaceAuthDialog(failure);
            }
        };

        webServiceHandler.sendPidDeta(PID,AadhaarRefNo,RequestId,ssoLoginTokan);
    }

    public void successFaceAuthDialog(String Txn) {
        Dialog customdialog = new Dialog (DashboardActivity.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.check);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setText("Aadhaar Face Authenication Success");
        bodymsg.setText("Aadhaar Tokan: " +Txn);
        btn.setText("ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashboardActivity.this,DashboardActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();

            }
        });

        customdialog.show();


//        AlertDialog alertDialog = new AlertDialog.Builder(DashboardActivity.this).create();
//        alertDialog.setTitle("Aadhaar FaceAuth Success");
//        alertDialog.setMessage("Aadhaar Tokan: " +Txn);
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//        alertDialog.setCancelable(false);
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(DashboardActivity.this,DashboardActivity.class);
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
        Dialog customdialog = new Dialog (DashboardActivity.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.cross);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setTextColor(ContextCompat.getColor(DashboardActivity.this,R.color.markout));
        titlemsg.setText("Aadhaar Face Authentication Failed");
        bodymsg.setTextColor(ContextCompat.getColor(DashboardActivity.this,R.color.markout));
        bodymsg.setText("Message: " +msg);
        btn.setBackgroundColor(ContextCompat.getColor(DashboardActivity.this,R.color.markout));
        btn.setTextColor(ContextCompat.getColor(DashboardActivity.this,R.color.white));
        btn.setText("Ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(DashboardActivity.this,DashboardActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();

            }
        });

        customdialog.show();
    }


    private void alertDialog() {
        Dialog customdialog = new Dialog (DashboardActivity.this );
        customdialog.setContentView (R.layout.logout_dialog);
        customdialog.setCancelable(false);
        Button cancel = customdialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.cancel();
            }
        });


        Button conferm = customdialog.findViewById(R.id.btn_conferm);
        conferm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                SharedPreferences preferences =getSharedPreferences("GenericFaceAuthLogInTokan0010", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(DashboardActivity.this, SpleshNextActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        customdialog.show();


//        new AlertDialog.Builder(this)
//                .setIcon(R.drawable.applogo)
//                .setTitle("Generic FaceAuth")
//                .setMessage("Are you sure you want to Logout?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        SharedPreferences preferences =getSharedPreferences("GenericFaceAuthLogInTokan0010", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.clear();
//                        editor.apply();
//                        Intent intent = new Intent(DashboardActivity.this, SpleshNextActivity.class);
//                        intent.putExtra("finish", true); // if you are checking for this in your other Activities
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                                Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//
//                    }
//
//                })
//                .setNegativeButton("No", null)
//                .show();

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}