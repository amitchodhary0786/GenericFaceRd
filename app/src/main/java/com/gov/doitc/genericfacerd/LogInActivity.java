package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.gov.doitc.genericfacerd.encreaprion.AES;
import com.gov.doitc.genericfacerd.utils.NetworkUtils;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    EditText userid,password;
    Button submit;
    String str_email,str_password,enc_pass;
    private NetworkUtils newtworkutils;
    TextView tv_beniregister,tv_emitraoperator;


    @Override
    protected void onStart() {
        super.onStart();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        newtworkutils = new NetworkUtils();
        if (newtworkutils.haveNetworkConnection(LogInActivity.this)) {

        }
        else {
            Snackbar snackbar = Snackbar
                    .make(userid, "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userid = (EditText)findViewById(R.id.login_edtUsername);
        password = (EditText)findViewById(R.id.login_edtPassword);
        submit = (Button) findViewById(R.id.btn_loginsso);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    public void checkValidation(){
        str_email = userid.getText().toString().trim();
        str_password = password.getText().toString().trim();
        if(str_email.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter SSOID",Toast.LENGTH_LONG).show();
        }
        else if(str_password.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_LONG).show();
        }

        else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        enc_pass = passwordencreaption(str_password);
                        logInFunction(str_email,enc_pass);
                        // apiCall();

                    } catch (Exception e) {

                    }
                }
            });
        }
    }

    public String passwordencreaption(String str_password){

        AES aes = new AES();
        String password = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            password = aes.encrypt(str_password,"R@j$S0@02{09}19#");
        }

        //      Log.e("encreaptttt", aes.encrypt(str_password,"R@j$S0@02{09}19#"));

        return password;
    }

    public void logInFunction(String str_email,String enc_pass1){
        WebServiceHandler webServiceHandler =new WebServiceHandler(LogInActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {

                Log.e("loginRESP",response);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            String IsSuccess = json.getString("IsSuccess");
                            String Message = json.getString("Message");
                            if(IsSuccess.equals("true")){
                                JSONObject jsonObject = json.getJSONObject("Data");
                                String UserType = jsonObject.getString("UserType");
                                String SSOID =jsonObject.getString("SSOID");
                                String FirstName =jsonObject.getString("FirstName");
                                String LastName= jsonObject.getString("LastName");
                                String Mobile =jsonObject.getString("Mobile");
                                String Valid =jsonObject.getString("Valid");
                                String Token= jsonObject.getString("Token");
                                String UserId = jsonObject.getString("UserId");

                                SharedPreferences sharedPreferencesdepartment = getApplicationContext().getSharedPreferences("GenericFaceAuthLogInTokan0010",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferencesdepartment.edit();
                                editor.putString("ssoLoginTokan",Token);
                                editor.putString("ssoid",SSOID);
                                editor.putString("UserId",UserId);
                                editor.commit();


                                Intent i = new Intent(LogInActivity.this,DashboardActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Snackbar snackbar = Snackbar
                                        .make(userid, Message, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailer(String failure) throws JSONException {
                Log.e("SSOResponcefailure",failure);
            }
        };
        webServiceHandler.loginfun(str_email,enc_pass1);

    }
}