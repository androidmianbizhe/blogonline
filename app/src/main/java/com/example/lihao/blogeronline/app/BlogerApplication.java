package com.example.lihao.blogeronline.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;


/**
 * Created by lihao on 17-11-14.
 */

public class BlogerApplication extends Application {

    private static Context context;

    private static Handler handler = new Handler();

    private static int threadId;

    @Override
    public void onCreate() {
        super.onCreate();


        context = this;

        threadId = Process.myTid();

        WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL,
                Constants.SCOPE));

    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getThreadId(){
        return threadId;
    }


}
