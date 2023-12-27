package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.gov.doitc.genericfacerd.model.Districtpojo;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EmitraKioskRegisterActivity extends AppCompatActivity {
   // String[] splitArray;
    CardView cv_sso;
   String kioskSSOID;
    CheckBox checkBox;
   String straadhaar,Txn;
    Button btn_registerkiosk,btn_genotp,btn_authotp;
    EditText ssoid;
    EditText et_kioskname,et_aadhaar,et_mobile,et_kioskcode,et_aadhaarotp;
    Button fetchkiosk;
    String strssoid;
    //Spinner kioskspinner;
    String[] courses = { "C", "Data structures",
            "Interview prep", "Algorithms",
            "DSA with java", "OS" };
    CardView cv_details;
    ImageView iv_back;
    LinearLayout ll_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emitra_kiosk_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmitraKioskRegisterActivity.this,LogInActivity.class);
                startActivity(i);
                finish();
            }
        });

        ll_otp = findViewById(R.id.ll_otp);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setText("I hereby state that i have no objection in authenticating myself with AADHAAR based authentication system and consent to providing my AADHAAR Number, and OTP for AADHAAR based Authentication. I also give my explicit consent for accessing the mobile number and email address from AADHAAR System. \n" +
                "मैं एतद्द्वारा कहता हूं कि मुझे आधार आधारित प्रमाणीकरण प्रणाली के साथ खुद को प्रमाणित करने में कोई आपत्ति नहीं है और मैं आधार आधारित प्रमाणीकरण के लिए अपना आधार नंबर और ओटीपी प्रदान करने के लिए सहमत हूं। मैं आधार प्रणाली से मोबाइल नंबर और ईमेल पते तक पहुंचने के लिए भी अपनी स्पष्ट सहमति देता हूं।");

        et_kioskname =findViewById(R.id.et_kioskname);
        et_aadhaar = findViewById(R.id.et_aadhaar);
        et_mobile = findViewById(R.id.et_mobile);

        //kioskspinner = findViewById(R.id.spi_kioskcode);
        cv_details = findViewById(R.id.cv_details);
        cv_sso = findViewById(R.id.cv_sso);

        ssoid = findViewById(R.id.et_ssoid);
        et_kioskcode = findViewById(R.id.et_kioskcode);

        fetchkiosk = findViewById(R.id.btn_fetch);
        fetchkiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strssoid = ssoid.getText().toString().trim();


                if(strssoid.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter SSOID",Toast.LENGTH_LONG).show();
                }
                else {
                    et_kioskcode.setText("");
                    et_kioskname.setText("");
                    et_mobile.setText("");
                    et_aadhaar.setText("");

                    getSSOUSerDetails(strssoid);
                    getKioskDetails(strssoid);
                }
            }
        });


        btn_registerkiosk = findViewById(R.id.btn_registerkiosk);
        btn_registerkiosk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strkioskcode = et_kioskcode.getText().toString().trim();
                String strkioskname = et_kioskname.getText().toString().trim();
                String strkioskaadhaar = et_aadhaar.getText().toString().trim();
                String strkioskmobile = et_mobile.getText().toString().trim();

                if(strkioskcode.equals("")){

                }

                else if(strkioskname.equals("")){

                }
                else if(strkioskaadhaar.equals("")){

                }
                else if(strkioskmobile.equals("")){

                }
                else {
                    registeremitrakiosk(strkioskcode,strkioskname,strkioskmobile,strkioskaadhaar,kioskSSOID);
                }


            }
        });

        btn_genotp = findViewById(R.id.btn_genotp);
        btn_genotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                straadhaar = et_aadhaar.getText().toString().trim();
                if(straadhaar.equals("")){
                    Toast.makeText(EmitraKioskRegisterActivity.this, "Please update aadhaar number in SSO profile", Toast.LENGTH_SHORT).show();
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

    public void getKioskDetails(String ssoid1){

            WebServiceHandler webServiceHandler =new WebServiceHandler(EmitraKioskRegisterActivity.this);
            webServiceHandler.serviceListener = new WebServiceListener() {
                @Override
                public void onResponse(String response) throws JSONException {
                    Log.e("KIOSKRESP",response);
                    JSONObject jsonObject1 = new JSONObject(response);
                    String REQUESTSTATUSCODE = jsonObject1.getString("REQUESTSTATUSCODE");
                    if(REQUESTSTATUSCODE.equals("200")){
                        kioskSSOID = jsonObject1.getString("SSOID");
                        String MERCHANTCODE = jsonObject1.getString("MERCHANTCODE");
                        String KIOSKCODE = jsonObject1.getString("KIOSKCODE");
                        String KIOSKNAME = jsonObject1.getString("KIOSKNAME");
                        String DISTRICT = jsonObject1.getString("DISTRICT");
                        String DISTRICTCD = jsonObject1.getString("DISTRICTCD");

                        String MOBILE = jsonObject1.getString("MOBILE");
                        String EMAIL = jsonObject1.getString("EMAIL");




                        String OLDKIOSKCODE = jsonObject1.getString("OLDKIOSKCODE");
                        Log.e("OLDKIOSKCODE",OLDKIOSKCODE);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cv_sso.setVisibility(View.GONE);
                                cv_details.setVisibility(View.VISIBLE);


                                //Drope Down code

//                                splitArray = KIOSKCODE.split("\\|");
//                                ArrayAdapter ad = new ArrayAdapter(EmitraKioskRegisterActivity.this, android.R.layout.simple_spinner_item, splitArray);
//                                ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                kioskspinner.setAdapter(ad);
//                                kioskspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });

                                et_kioskcode.setText(KIOSKCODE);
                                et_kioskname.setText(KIOSKNAME);


                                et_aadhaar.setText("444043414999844");


                                et_mobile.setText(MOBILE);

                            }
                        });
                    }


                }

                @Override
                public void onFailer(String failure) throws JSONException {

                }
            };
            webServiceHandler.getKioskDetails(ssoid1);

        }

    public void getSSOUSerDetails(String ssoid){
        WebServiceHandler webServiceHandler =new WebServiceHandler(EmitraKioskRegisterActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {
                Log.e("ssoRES",response);
                JSONObject jsonObject1 = new JSONObject(response);
                String IsSuccess = jsonObject1.getString("IsSuccess");

                if(IsSuccess.equals("true")){

                    JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                    //String UserType = jsonObject2.getString("UserType");
                    //String SSOID = jsonObject2.getString("SSOID");
                    //String FirstName = jsonObject2.getString("FirstName");
                    //String LastName = jsonObject2.getString("LastName");
                    //String Mobile = jsonObject2.getString("Mobile");
                    String AadhaarId = jsonObject2.getString("AadhaarId");


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //et_aadhaar.setText(AadhaarId);
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


    public void genOTP(String aadhaar){
        WebServiceHandler webServiceHandler =new WebServiceHandler(EmitraKioskRegisterActivity.this);
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
                                Toast.makeText(EmitraKioskRegisterActivity.this, Message, Toast.LENGTH_SHORT).show();
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
        WebServiceHandler webServiceHandler =new WebServiceHandler(EmitraKioskRegisterActivity.this);
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
                                Toast.makeText(EmitraKioskRegisterActivity.this, Message, Toast.LENGTH_SHORT).show();
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

    public void registeremitrakiosk(String strkioskcode,String strkioskname,String strkioskmobile,String strkioskaadhaar,String kioskSSOID){
        WebServiceHandler webServiceHandler =new WebServiceHandler(EmitraKioskRegisterActivity.this);
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
                            if(IsSuccess.equals("true")) {
                                String Message = jsonObject1.getString("Message");
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                                String SUBAUA_NAME = jsonObject2.getString("SUBAUA_NAME");
                                String MOBILE_NO = jsonObject2.getString("MOBILE_NO");
                                String NAME = jsonObject2.getString("NAME");
                                String SSOID = jsonObject2.getString("SSOID");
                                registerSuccessDialog(Message);
                            }

                            else {
                                String Message = jsonObject1.getString("Message");
                                //Toast.makeText(EmitraKioskRegisterActivity.this, Message, Toast.LENGTH_SHORT).show();
                                registeralreadyDialog(Message);
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
        webServiceHandler.registeremitrakiosk(strkioskcode,strkioskname,strkioskmobile,strkioskaadhaar,kioskSSOID);
    }

    public void registerSuccessDialog(String msg){
        Dialog customdialog = new Dialog (EmitraKioskRegisterActivity.this ,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customdialog.dismiss();
                Intent i = new Intent(EmitraKioskRegisterActivity.this,LogInActivity.class);
                startActivity(i);
                finish();
            }
        });

        customdialog.show();

    }

    public void registeralreadyDialog(String msg){
        Dialog customdialog = new Dialog (EmitraKioskRegisterActivity.this );
        customdialog.setContentView (R.layout.registerdialog2);
        customdialog.setCancelable(false);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);

        bodymsg.setText(msg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EmitraKioskRegisterActivity.this,SpleshNextActivity.class);
                startActivity(i);
                finish();
                customdialog.dismiss();
            }
        });

        customdialog.show();
    }

    public void successAadhaarAuthDialog(){
        Dialog customdialog = new Dialog (EmitraKioskRegisterActivity.this );
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
                btn_registerkiosk.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor1));

                btn_registerkiosk.setEnabled(true);

                btn_authotp.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor2));
                btn_authotp.setEnabled(false);




            }
        });

        customdialog.show();

//        AlertDialog alertDialog = new AlertDialog.Builder(EmitraKioskRegisterActivity.this).create();
//        alertDialog.setTitle("OTP Authentication");
//        alertDialog.setMessage("Aadhaar OTP authentication Success");
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialog.dismiss();
//                et_aadhaarotp.setEnabled(false);
//                btn_registerkiosk.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor1));
//
//                btn_registerkiosk.setEnabled(true);
//
//                btn_authotp.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor2));
//                btn_authotp.setEnabled(false);
//
//
//               // btn_registerkiosk.isEnabled();
//
//            }
//        });
//
//        alertDialog.show();
    }

    public void successOTPDialog(){
        Dialog customdialog = new Dialog (EmitraKioskRegisterActivity.this );
        customdialog.setContentView (R.layout.registerdialog);
        customdialog.setCancelable(false);
        ImageView icon = customdialog.findViewById(R.id.iv_icon);
        icon.setImageResource(R.drawable.check);
        Button btn = customdialog.findViewById(R.id.btn_dilog);
        TextView titlemsg = customdialog.findViewById(R.id.tv_titlemsg);
        TextView bodymsg = customdialog.findViewById(R.id.tv_bodymsg);
        titlemsg.setText("Aadhaar OTP");
        bodymsg.setText("Aadhaar OTP Generated Successfully");
        btn.setText("ok");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_otp.setVisibility(View.VISIBLE);
                customdialog.dismiss();

                btn_genotp.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor2));
                btn_genotp.setEnabled(false);

            }
        });

        customdialog.show();

//        AlertDialog alertDialog = new AlertDialog.Builder(EmitraKioskRegisterActivity.this).create();
//        alertDialog.setTitle("Aadhaar OTP");
//        alertDialog.setMessage("Aadhaar OTP Generate Success");
//        alertDialog.setIcon(R.drawable.aadhaaricon);
//        alertDialog.setCancelable(false);
//
//        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                ll_otp.setVisibility(View.VISIBLE);
//                alertDialog.dismiss();
//
//                btn_genotp.setBackgroundColor(ContextCompat.getColor(EmitraKioskRegisterActivity.this,R.color.buttoncolor2));
//                btn_genotp.setEnabled(false);
//                // btn_registerkiosk.isEnabled();
//
//            }
//        });
//
//        alertDialog.show();
    }

}