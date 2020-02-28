package com.foghome.v1.represents;

import java.util.ArrayList;

public class Response {
    private String resultKey, status;
    private ArrayList<String> list;

    public Response() {
    }

    public Response(String resultKey, String status, ArrayList<String> list) {
        this.resultKey = resultKey;
        this.status = status;
        this.list = list;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
