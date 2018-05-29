package com.example.lihao.blogeronline.bean;

/**
 * Created by lihao on 17-10-22.
 */

public class LoginInfo {


    /**
     * returnCode : 1
     * uid : 123460
     * token : eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ==.eyJ1aWQiOm51bGwsIm5pY2tuYW1lIjpudWxsLCJkZWFkbGluZSI6MjExNDU4NzgzNX0=
     */

    private int returnCode;
    private long uid;
    private String token;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
