package com.gov.doitc.genericfacerd.utils;

public class WebUrls {

    public static final String MERCHANTCODE = "AADHARAUTH";
    public static final int NONE = 0;
    public static final int FPS = 1;
    public static final int EMITRA = 2;
    public static final int  BENEFICARY = 1;
    public static final int  AuthenticationOperator = 2;




    public static final String kioskdetails ="https://emitraapp.rajasthan.gov.in/webServicesRepository/getKioskDetailsJSON";
    public static final String getssodetils ="https://ssotest.rajasthan.gov.in:4443/SSOREST/GetUserDetailJSON";

    public static final String getssodetilsnew ="http://103.203.136.78/GenericFaceAuthApi/api/User/GetSSODetails";

    public static final String registerURL ="http://103.203.136.78/GenericFaceAuthApi/api/User/Register";

    public static final String GENOTP ="http://103.203.136.78/GenericFaceAuthApi/api/User/SendOtp";

    public static final String AUTHOTP="http://103.203.136.78/GenericFaceAuthApi/api/User/VerifyOtp";

    public static final String LOGINURL="http://103.203.136.78/GenericFaceAuthApi/api/User/Login";

    public static final String DASHBOARDURL="http://103.203.136.78/GenericFaceAuthApi/api/User/Dashboard";

    public static final String PENDINGURL="http://103.203.136.78/GenericFaceAuthApi/api/User/RequestByStatus";

    public static final String FACEAUTHURL="http://103.203.136.78/GenericFaceAuthApi/api/User/FaceAuthRequest";

    public static final String LOGINSTATUSURL="http://103.203.136.78/GenericFaceAuthApi/api/User/CheckLoginStatus";









}
