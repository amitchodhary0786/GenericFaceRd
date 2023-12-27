package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.gov.doitc.genericfacerd.adepter.DistrictViewAdepter;
import com.gov.doitc.genericfacerd.model.DistrictModel;
import com.gov.doitc.genericfacerd.model.Districtpojo;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeneficiaryRegistrationActivity extends AppCompatActivity  {
    ImageView iv_close;
    Spinner spdistrict;
    String[] courses = { "C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS" };
    Button fatch,btn_registerbeneficiary,btn_genotp,btn_authotp;
    ArrayList<DistrictModel> districtlist  = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();

    private ArrayList<Districtpojo> arraydistrict = new ArrayList<>();
    DistrictViewAdepter districtadepter;
    TextView district,tehsil;
    String districtid;

    EditText et_ssoid;
    EditText et_name,et_aadhaar,et_mobile,et_aadhaarotp;
    TextView tv_district;
    ImageView iv_back;
    String SSOID;
    LinearLayout ll_infolayout,ll_otp;
    CardView cv_details,cv_sso;

    CheckBox checkBox;
    String straadhaar,Txn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_registration);

        ll_otp = findViewById(R.id.ll_otp);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setText("I hereby state that i have no objection in authenticating myself with AADHAAR based authentication system and consent to providing my AADHAAR Number, and OTP for AADHAAR based Authentication. I also give my explicit consent for accessing the mobile number and email address from AADHAAR System. \n" +
                "मैं एतद्द्वारा कहता हूं कि मुझे आधार आधारित प्रमाणीकरण प्रणाली के साथ खुद को प्रमाणित करने में कोई आपत्ति नहीं है और मैं आधार आधारित प्रमाणीकरण के लिए अपना आधार नंबर और ओटीपी प्रदान करने के लिए सहमत हूं। मैं आधार प्रणाली से मोबाइल नंबर और ईमेल पते तक पहुंचने के लिए भी अपनी स्पष्ट सहमति देता हूं।");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeneficiaryRegistrationActivity.this,LogInActivity.class);
                startActivity(i);
                finish();
            }
        });

        //getDistrictlist();
        //SsologinfunctionsDirect();
        cv_sso = findViewById(R.id.cv_sso);

        district = findViewById(R.id.tv_district);
        district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        et_ssoid = findViewById(R.id.et_ssoid);
        fatch = findViewById(R.id.btn_fetch);
        fatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ssoid = et_ssoid.getText().toString().trim();

                if(ssoid.equals("")){
                    Toast.makeText(BeneficiaryRegistrationActivity.this, "This field not blank", Toast.LENGTH_SHORT).show();
                }
                else {

                    getSSOUSerDetails(ssoid);

                }


            }
        });

        et_name = findViewById(R.id.et_name);
        et_aadhaar = findViewById(R.id.et_aadhaar);
        et_mobile = findViewById(R.id.et_mobile);
        tv_district = findViewById(R.id.tv_district);

        btn_registerbeneficiary = findViewById(R.id.btn_registerbeneficiary);
        btn_registerbeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strname = et_name.getText().toString().trim();
                String straadhaar = et_aadhaar.getText().toString().trim();
                String strmobile = et_mobile.getText().toString().trim();
                String strdistrict = tv_district.getText().toString().trim();

                if(strname.equals("")){
                    Toast.makeText(BeneficiaryRegistrationActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }
                else if(straadhaar.equals("")){
                    Toast.makeText(BeneficiaryRegistrationActivity.this, "Please enter aadhaar number", Toast.LENGTH_SHORT).show();
                }
                else if(strmobile.equals("")){
                    Toast.makeText(BeneficiaryRegistrationActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerBeneficiary(strname,straadhaar,strmobile,SSOID);
                }
            }
        });


        ll_infolayout = findViewById(R.id.ll_infolayout);
        cv_details = findViewById(R.id.cv_details);

        btn_genotp = findViewById(R.id.btn_genotp);
        btn_genotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                straadhaar = et_aadhaar.getText().toString().trim();
                if(straadhaar.equals("")){
                    Toast.makeText(BeneficiaryRegistrationActivity.this, "Please update aadhaar number in SSO profile", Toast.LENGTH_SHORT).show();
                }
                else if(!checkBox.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please Check Aadhaar consern",Toast.LENGTH_LONG).show();
                }
                else {
                    genOTP(straadhaar);
                }

            }
        });

        et_aadhaarotp = findViewById(R.id.et_aadhaarotp);
        btn_authotp = findViewById(R.id.btn_authotp);
        btn_authotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strotp = et_aadhaarotp.getText().toString().trim();
                if(strotp.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter OTP", Toast.LENGTH_LONG).show();
                }
                else if(strotp.length()<=5 || strotp.length()>=7){
                    Toast.makeText(getApplicationContext(),"Invalid OTP", Toast.LENGTH_LONG).show();
                }
                else {
                    authOTP(straadhaar,Txn,strotp);
                }
            }
        });

    }


    public void genOTP(String aadhaar){
        WebServiceHandler webServiceHandler =new WebServiceHandler(BeneficiaryRegistrationActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("GENOTP",response);
                JSONObject jsonObject1 = new JSONObject(response);
                String IsSuccess = jsonObject1.getString("IsSuccess");
                String Message = jsonObject1.getString("Message");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(IsSuccess.equals("true")) {
                                    Txn = jsonObject1.getString("Txn");
                                    successOTPDialog();
                                }
                                else {
                                    Toast.makeText(BeneficiaryRegistrationActivity.this, Message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e){

                            }

//                            ll_otp.setVisibility(View.VISIBLE);
                            // et_aadhaar.setText(AadhaarId);
                        }
                    });


            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };
        webServiceHandler.getOTP(aadhaar);
    }

    public void authOTP(String aadhaar,String txn,String otp){
        WebServiceHandler webServiceHandler =new WebServiceHandler(BeneficiaryRegistrationActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("AUTHOTP",response);
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
                                    successAadhaarAuthDialog();
                                }
                                else {
                                    Toast.makeText(BeneficiaryRegistrationActivity.this, Message, Toast.LENGTH_SHORT).show();
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
        webServiceHandler.authOTP(aadhaar,txn,otp);
    }
    public void successOTPDialog(){

        Dialog customdialog = new Dialog (BeneficiaryRegistrationActivity.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.check);
        titlemsg.setText("Aadhaar OTP");
        bodymsg.setText("Aadhaar OTP Generated Successfully");
        btn.setText("ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_otp.setVisibility(View.VISIBLE);
                customdialog.dismiss();
                btn_genotp.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor2));
                btn_genotp.setEnabled(false);

            }
        });

        customdialog.show();

//        AlertDialog alertDialog = new AlertDialog.Builder(BeneficiaryRegistrationActivity.this).create();
//        alertDialog.setTitle("Aadhaar OTP");
//        alertDialog.setMessage("Aadhaar OTP Generate Success");
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//        alertDialog.setCancelable(false);
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                ll_otp.setVisibility(View.VISIBLE);
//                alertDialog.dismiss();
//                btn_genotp.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor2));
//                btn_genotp.setEnabled(false);
//
//                // btn_registerkiosk.isEnabled();
//
//            }
//        });
//
//        alertDialog.show();
    }


    public void successAadhaarAuthDialog(){
        Dialog customdialog = new Dialog (BeneficiaryRegistrationActivity.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.check);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setText("OTP Authentication");
        bodymsg.setText("Aadhaar OTP authentication successful");
        btn.setText("ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customdialog.dismiss();
                et_aadhaarotp.setEnabled(false);
                btn_registerbeneficiary.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor1));

                btn_registerbeneficiary.setEnabled(true);

                btn_authotp.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor2));
                btn_authotp.setEnabled(false);



            }
        });

        customdialog.show();

//        AlertDialog alertDialog = new AlertDialog.Builder(BeneficiaryRegistrationActivity.this).create();
//        alertDialog.setTitle("OTP Authentication");
//        alertDialog.setMessage("Aadhaar OTP authentication Success");
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//        alertDialog.setCancelable(false);
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialog.dismiss();
//                et_aadhaarotp.setEnabled(false);
//                btn_registerbeneficiary.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor1));
//
//                btn_registerbeneficiary.setEnabled(true);
//
//                btn_authotp.setBackgroundColor(ContextCompat.getColor(BeneficiaryRegistrationActivity.this,R.color.buttoncolor2));
//                btn_authotp.setEnabled(false);
//
//
//
//                // btn_registerkiosk.isEnabled();
//
//            }
//        });
//
//        alertDialog.show();
    }

    public void registerBeneficiary(String name,String aadhaar,String mobile,String sSSOID){
        WebServiceHandler webServiceHandler =new WebServiceHandler(BeneficiaryRegistrationActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("BENRREgister",response);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject1 = new JSONObject(response);
                                String IsSuccess = jsonObject1.getString("IsSuccess");
                                String Message = jsonObject1.getString("Message");
                                if(IsSuccess.equals("true")) {
                                    registerSuccessDialog();
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                                    String SSOID = jsonObject2.getString("SSOID");
                                    String Name = jsonObject2.getString("Name");
                                    String MobileNo = jsonObject2.getString("MOBILE_NO");
                                    String AadhaarRefNo = jsonObject2.getString("AADHAAR_REF_NO");


                                }

                                else {
                                    registeralreadyDialog(Message);
                                   // Toast.makeText(BeneficiaryRegistrationActivity.this, Message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }

                        }

                    });








            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };
        webServiceHandler.registerBeneficiary(name,aadhaar,mobile,sSSOID);
    }

    public void getSSOUSerDetails(String ssoid){
        WebServiceHandler webServiceHandler =new WebServiceHandler(BeneficiaryRegistrationActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("ssoRES",response);
                JSONObject jsonObject1 = new JSONObject(response);
                String IsSuccess = jsonObject1.getString("IsSuccess");

                if(IsSuccess.equals("true")){

                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                    String UserType = jsonObject2.getString("UserType");
                    SSOID = jsonObject2.getString("SSOID");
                    String FirstName = jsonObject2.getString("FirstName");
                    String LastName = jsonObject2.getString("LastName");
                    String Mobile = jsonObject2.getString("Mobile");
                    String AadhaarId = jsonObject2.getString("AadhaarId");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cv_sso.setVisibility(View.GONE);
                            cv_details.setVisibility(View.VISIBLE);
                            et_name.setText(FirstName);
                            et_mobile.setText(Mobile);
                            et_aadhaar.setText(AadhaarId);
                        }
                    });
                }
            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };
        webServiceHandler.getssodetails(ssoid);
    }

    public void registerSuccessDialog(){
        Dialog customdialog = new Dialog (BeneficiaryRegistrationActivity.this ,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                Intent i = new Intent(BeneficiaryRegistrationActivity.this,LogInActivity.class);
                startActivity(i);
                finish();
            }
        });

        customdialog.show();
    }



    public void registeralreadyDialog(String msg){

        Dialog customdialog = new Dialog (BeneficiaryRegistrationActivity.this );
        customdialog.setContentView (R.layout.registerdialog2);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);

        bodymsg.setText(msg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BeneficiaryRegistrationActivity.this,SpleshNextActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();
            }
        });

        customdialog.show();


    }



    public void showDialog(){
        Dialog janaahardetilsDialog = new Dialog (BeneficiaryRegistrationActivity.this ,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        janaahardetilsDialog.setContentView (R.layout.dialogscreen);

        iv_close = janaahardetilsDialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                janaahardetilsDialog.cancel();
            }
        });
        RecyclerView recyclerView = janaahardetilsDialog.findViewById(R.id.rv_allviewadepter);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        districtadepter = new DistrictViewAdepter(getApplicationContext(),arraydistrict);
        recyclerView.setAdapter(districtadepter);
        districtadepter.myClickListeners = new DistrictViewAdepter.MyClickListeners() {
            @Override
            public void onItemClick(int position) {
                  //Log.e("RECIVE",arraydistrict.get(position).getDistrictID());
                  districtid = arraydistrict.get(position).getDistrictID();
                  district.setText(arraydistrict.get(position).getDistrictname());
                janaahardetilsDialog.cancel();
            }
        };
        janaahardetilsDialog.show();
    }

    public void fatchDetails(String ssoid){
        WebServiceHandler webServiceHandler =new WebServiceHandler(BeneficiaryRegistrationActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("RESP",response);
                JSONObject jsonObject1 = new JSONObject(response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //showDialog();
                    }
                });

            }

            @Override
            public void onFailer(String failure) throws JSONException {

            }
        };
        webServiceHandler.fatchDetils(ssoid);
    }
}