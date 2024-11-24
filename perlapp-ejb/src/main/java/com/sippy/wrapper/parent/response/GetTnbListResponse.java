package com.sippy.wrapper.parent.response;

import com.sippy.wrapper.parent.database.dao.TnbDao;

import java.util.List;

public class GetTnbListResponse {
    private String faultCode;
    private String faultString;
    private List<TnbDao> data;

    public GetTnbListResponse() {}

    public GetTnbListResponse(String faultCode, String faultString, List<TnbDao> data) {
        this.faultCode = faultCode;
        this.faultString = faultString;
        this.data = data;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public void setFaultString(String faultString) {
        this.faultString = faultString;
    }

    public List<TnbDao> getData() {
        return data;
    }

    public void setData(List<TnbDao> data) {
        this.data = data;
    }
}
