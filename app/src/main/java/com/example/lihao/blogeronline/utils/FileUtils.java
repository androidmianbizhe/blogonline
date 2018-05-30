package com.example.lihao.blogeronline.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by lihao on 17-11-15.
 */

public class FileUtils {


    public static String getSplashDir(Context context) {
        String dir = context.getFilesDir() + "/splash/";
        return mkdirs(dir);
    }


    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }



}
