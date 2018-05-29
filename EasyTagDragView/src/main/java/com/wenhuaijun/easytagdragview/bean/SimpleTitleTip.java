package com.wenhuaijun.easytagdragview.bean;

import android.os.Parcelable;

import java.io.Serializable;

public class SimpleTitleTip implements Serializable {
    private int id;
    private int from_id;
    private String tip;
    private String url;

    public SimpleTitleTip(int id,String tip,String url,int from_id){
        this.id = id;
        this.tip = tip;
        this.url = url;
        this.from_id = from_id;
    }

    public SimpleTitleTip(){

    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "tip:"+ tip;
    }
}
