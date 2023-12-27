package com.gov.doitc.genericfacerd.utils;

import static com.gov.doitc.genericfacerd.utils.WebUrls.AUTHOTP;
import static com.gov.doitc.genericfacerd.utils.WebUrls.AuthenticationOperator;
import static com.gov.doitc.genericfacerd.utils.WebUrls.BENEFICARY;
import static com.gov.doitc.genericfacerd.utils.WebUrls.DASHBOARDURL;
import static com.gov.doitc.genericfacerd.utils.WebUrls.EMITRA;
import static com.gov.doitc.genericfacerd.utils.WebUrls.FACEAUTHURL;
import static com.gov.doitc.genericfacerd.utils.WebUrls.GENOTP;

import static com.gov.doitc.genericfacerd.utils.WebUrls.LOGINSTATUSURL;
import static com.gov.doitc.genericfacerd.utils.WebUrls.LOGINURL;
import static com.gov.doitc.genericfacerd.utils.WebUrls.MERCHANTCODE;
import static com.gov.doitc.genericfacerd.utils.WebUrls.NONE;
import static com.gov.doitc.genericfacerd.utils.WebUrls.PENDINGURL;

import static com.gov.doitc.genericfacerd.utils.WebUrls.getssodetils;
import static com.gov.doitc.genericfacerd.utils.WebUrls.getssodetilsnew;

import static com.gov.doitc.genericfacerd.utils.WebUrls.kioskdetails;
import static com.gov.doitc.genericfacerd.utils.WebUrls.registerURL;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 6/7/2017.
 */

public class WebServiceHandler {

    private OkHttpClient okHttpClient;
    private OkHttpClient client = new OkHttpClient();
    private RequestBody requestBody;
    private Request request;
    private Context context;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private ProgressDialog pDialog;
    public WebServiceListener serviceListener;
    public static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");



    public WebServiceHandler(Context context) {
        this.context = context;


        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @SuppressLint("BadHostnameVerifier")
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("please wait..");
            // pDialog = new ProgressDialog (context,SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getKioskDetails(String ssoid){

        RequestBody formBody = new FormBody.Builder()
                .addEncoded("SSOID", ssoid)
                .addEncoded("MERCHANTCODE", MERCHANTCODE)
                .build();
        pDialog.show();
        request = new Request.Builder()
                .url(kioskdetails)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fatchDetils(String ssoid){
        RequestBody formBody = new FormBody.Builder()
                .add("SSOID", ssoid)
                .build();
        pDialog.show();
        request = new Request.Builder()
                .url(getssodetils)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getssodetails(String ssoname){
       // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("SSOId", ssoname);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(getssodetilsnew)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void registerBeneficiary(String name,String aadhaar,String mobile,String sSSOID){
        // Log.e("111",ssoname);

        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
       // params.put("UserId", "1");
        params.put("SSOID", sSSOID);
        params.put("Name", name);
        params.put("OperatorType", String.valueOf(NONE));
        params.put("OperatorCode", "null");
        params.put("SubauaCode", "null");
        params.put("SubauaName", "null");
        params.put("MobileNo", mobile);
       // params.put("LicenseKey", "null");
        params.put("AadhaarRefNo", aadhaar);
        params.put("UserType", String.valueOf(BENEFICARY));

        Log.e("111", String.valueOf(params));
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());


        request = new Request.Builder()
                .url(registerURL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getOTP(String aadhaarno){
        // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("AadhaarRefNo", aadhaarno);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(GENOTP)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void authOTP(String aadhaarnum,String txn,String otp){
        // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("AadhaarRefNo", aadhaarnum);
        params.put("TxnNo", txn);
        params.put("Otp", otp);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(AUTHOTP)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registeremitrakiosk(String strkioskcode,String strkioskname,String strkioskmobile,String strkioskaadhaar,String kioskSSOID){
        // Log.e("111",ssoname);

        pDialog.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("SSOID", kioskSSOID);
        params.put("Name", strkioskname);
        params.put("OperatorType", String.valueOf(EMITRA));
        params.put("OperatorCode", strkioskcode);
        params.put("SubauaCode", "0000440000");
        params.put("SubauaName", "EMITRA DOIT and C");
        //params.put("MobileNo", strkioskmobile);
        params.put("MobileNo", "8949291168");
        //params.put("LicenseKey", "MKcrm4YjHSvsq8ZHdPCOeiBXRNnmMeRJDz4xfru-sr-Pp9W4Es8_wy4");
        params.put("AadhaarRefNo", strkioskaadhaar);
        params.put("UserType", String.valueOf(AuthenticationOperator));

        Log.e("111", String.valueOf(params));
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());


        request = new Request.Builder()
                .url(registerURL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loginfun(String username,String password){
        // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("UserName", username);
        params.put("Password", password);
        params.put("Application", "");
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(LOGINURL)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getLoginStatus(String ssoname, String jwttoken){

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("SSOID", ssoname);
        params.put("Token", jwttoken);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(LOGINSTATUSURL)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getDashboardCount(String ssoLoginTokan){
        // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        //params.put("UserId", UserId);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(DASHBOARDURL)
                .post(body)
                .addHeader("Authorization","Bearer " + ssoLoginTokan)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getPendingData(String statusID,String pendingUserId,String ssoLoginTokan){
        // Log.e("111",ssoname);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("StatusId", statusID);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(PENDINGURL)
                .post(body)
                .addHeader("Authorization","Bearer " + ssoLoginTokan)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sendPidDeta(String PID,String aadhaarrefno,String refno,String ssoLoginTokan){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();
        params.put("AadhaarRefNo", aadhaarrefno);
        params.put("PidData", PID);
        params.put("RequestId",refno);
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        pDialog.show();
        request = new Request.Builder()
                .url(FACEAUTHURL)
                .post(body)
                .addHeader("Authorization","Bearer " + ssoLoginTokan)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                try {
                    serviceListener.onResponse(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}




