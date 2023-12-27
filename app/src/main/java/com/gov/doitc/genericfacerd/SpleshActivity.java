package com.gov.doitc.genericfacerd;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.material.snackbar.Snackbar;
import com.gov.doitc.genericfacerd.utils.WebServiceHandler;
import com.gov.doitc.genericfacerd.utils.WebServiceListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SpleshActivity extends AppCompatActivity {

    TextView tv_name;
    String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);

        tv_name = findViewById(R.id.tv_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        versionName = BuildConfig.VERSION_NAME;
        Log.e("VersionName",versionName);

        spleshfunction();

//        Thread timer = new Thread(){
//            public void run(){
//                try{
//                    sleep(3000);
////                  sleep(100);
//                }
//                catch(InterruptedException e){
//                    e.printStackTrace();
//                } finally {
//                    Intent openMain = new Intent(SpleshActivity.this, SpleshNextActivity.class);
//                    startActivity(openMain);
//                    finish();
//                }
//            }
//        };
//        timer.start();
    }

    public void spleshfunction() {
        Boolean networkcheck = isNetworkAvailable();
        if (networkcheck) {
            SharedPreferences sharedPreferencesdepartment = getApplicationContext().getSharedPreferences("GenericFaceAuthLogInTokan0010", MODE_PRIVATE);
            String ssoLoginTokan = sharedPreferencesdepartment.getString("ssoLoginTokan", "");
            String SSOID = sharedPreferencesdepartment.getString("ssoid", "");
            Log.e("tokan@@", ssoLoginTokan + " " + SSOID);

            getloginStatus(SSOID,ssoLoginTokan);


//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            sleep(3000);
//                        } catch (Exception e) {
//
//                        } finally {
//                            if(ssoLoginTokan.equals("null")) {
//                                Intent intent = new Intent(SpleshActivity.this, SpleshNextActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                            else {
//                                Intent intent = new Intent(SpleshActivity.this, DashboardActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//
//                        }
//
//                    }
//
//                };
//                thread.start();




        } else {
            Snackbar snackbar = Snackbar
                    .make(tv_name, "No internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getloginStatus(String ssid, String tokan){
        WebServiceHandler webServiceHandler = new WebServiceHandler(SpleshActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) throws JSONException {

                Log.e("GetLoginStatus01", response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(response);
                            String IsSuccess = json.getString("IsSuccess");
                            JSONObject jsonObject = json.getJSONObject("Data");

                            String appVersion = jsonObject.getString("AppVersion");
                            //String appVersion = "1.0";
                            if (appVersion.equals(versionName)) {
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(3000);
                                        } catch (Exception e) {

                                        } finally {
                                            if(IsSuccess.equals("true")) {
                                                Intent intent = new Intent(SpleshActivity.this, DashboardActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Intent intent = new Intent(SpleshActivity.this, SpleshNextActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }

                                    }

                                };
                                thread.start();

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog();
                                        //Toast.makeText(SpleshScreen.this, "update application playstore", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailer(String failure) throws JSONException {
                Toast.makeText(SpleshActivity.this, failure, Toast.LENGTH_LONG).show();
            }
        };
        webServiceHandler.getLoginStatus(ssid, tokan);
    }

    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.applogo)
                .setTitle("High Priority")
                .setMessage("download the new version of the application")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        //Log.e("packagename",appPackageName);
                        //String appPackageName = "com.dreamplug.androidapp";
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }

                })
                .show();

    }

}