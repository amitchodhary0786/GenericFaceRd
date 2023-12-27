package com.gov.doitc.genericfacerd.model;

public class TransactionModel {
    private String AadhaarRefNo;
    private String Purpose;
    private String RequestDate;
    private String RequestId;
    private String RequestType;
    private String SubauaName;

    public String getAadhaarRefNo() {
        return AadhaarRefNo;
    }

    public void setAadhaarRefNo(String aadhaarRefNo) {
        AadhaarRefNo = aadhaarRefNo;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getSubauaName() {
        return SubauaName;
    }

    public void setSubauaName(String subauaName) {
        SubauaName = subauaName;
    }
}
