package com.example.lihao.blogeronline.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class SPUtils {

    private static final String THEME_PREFERNCE = "theme_preference";

    private static SharedPreferences sp;

    private static void init(Context context){
        if(sp == null){
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void setSharedIntData(Context context,String key,int value){
        if(sp == null){
            init(context);
        }
        sp.edit().putInt(key,value).commit();
    }

    public static int getSharedIntData(Context context,String key,int def){
        if(sp == null){
            init(context);
        }
        return sp.getInt(key,def);
    }

    public static void setSharedlongData(Context context, String key, long value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putLong(key, value).commit();
    }

    public static long getSharedlongData(Context context, String key,long def) {
        if (sp == null) {
            init(context);
        }
        return sp.getLong(key, def);
    }

    public static void setSharedFloatData(Context context, String key,
                                          float value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putFloat(key, value).commit();
    }

    public static Float getSharedFloatData(Context context, String key,float def) {
        if (sp == null) {
            init(context);
        }
        return sp.getFloat(key, def);
    }

    public static void setSharedBooleanData(Context context, String key,
                                            boolean value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static Boolean getSharedBooleanData(Context context, String key,boolean def) {
        if (sp == null) {
            init(context);
        }
        return sp.getBoolean(key, def);
    }

    public static void setSharedStringData(Context context, String key, String value) {
        if (sp == null) {
            init(context);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getSharedStringData(Context context, String key,String def) {
        if (sp == null) {
            init(context);
        }
        return sp.getString(key, def);
    }

    public static int getTheme(Context context) {
        return getSharedIntData(context,THEME_PREFERNCE, 1);
    }

    public static void setTheme(Context context,int theme){
        setSharedIntData(context,THEME_PREFERNCE,theme);
    }

}
