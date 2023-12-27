package com.gov.doitc.genericfacerd.utils;

import org.json.JSONException;

/**
 * Author       :   Amit Chodhary
 * Designation  :   Android Developer
 * E-mail       :   amit.chodhary007@gmail.com
 * Date         :   1/30/2016
 * Company      :   Atishay Ltd
 * Purpose      :   This class
 * Description  :   Description.
 */
public interface WebServiceListener {
    public void onResponse(String response) throws JSONException;
    public void onFailer(String failure) throws JSONException;
}
