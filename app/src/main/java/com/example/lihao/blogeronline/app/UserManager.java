package com.example.lihao.blogeronline.app;

import android.content.Context;

import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.UIUtils;

/**
 * Created by lihao on 17-10-22.
 */

public class UserManager {

    private static Context context;

    private static UserManager userManager;

    private UserManager(Context context){
        this.context = context;
    }

    public static UserManager create(Context context) {
        if(userManager == null){
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager(context);
                }
            }
        }
        return userManager;
    }

    public void setUid(long uid){
        SPUtils.setSharedlongData(context,"uid",uid);
    }

    public long getUid(){
        return SPUtils.getSharedlongData(context,"uid",0);
    }

    public void setToken(String token) {
        SPUtils.setSharedStringData(UIUtils.getContext(),"token",token);
    }

    public String getToken() {
       return SPUtils.getSharedStringData(context,"token","");
    }

    public void setDeadline(){
        SPUtils.setSharedlongData(context,"deadline", System.currentTimeMillis()+7*24*60*60*1000);
    }

    public long getDeadline(){
        return SPUtils.getSharedlongData(context,"deadline", 0);
    }

    public void setISLogin(){
        SPUtils.setSharedBooleanData(context,"isLogin",true);
    }

    public boolean getIsLogin(){
        return SPUtils.getSharedBooleanData(context,"isLogin",false);
    }

    public void clearUser() {
        SPUtils.setSharedlongData(context,"uid", 0);
        SPUtils.setSharedStringData(context,"token","");
        SPUtils.setSharedlongData(context,"deadline",0);
        SPUtils.setSharedBooleanData(context,"isLogin",false);
    }

}
