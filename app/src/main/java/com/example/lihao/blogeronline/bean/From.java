package com.example.lihao.blogeronline.bean;

/**
 * Created by Administrator on 2018/4/16.
 */

public class From {


    /**
     * fid : 1
     * f_name : Csdn
     * f_url : https://blog.csdn.net/
     * f_page_max_item : 30
     */

    private int fid;
    private String f_name;
    private String f_url;
    private int f_page_max_item;

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getF_url() {
        return f_url;
    }

    public void setF_url(String f_url) {
        this.f_url = f_url;
    }

    public int getF_page_max_item() {
        return f_page_max_item;
    }

    public void setF_page_max_item(int f_page_max_item) {
        this.f_page_max_item = f_page_max_item;
    }
}
