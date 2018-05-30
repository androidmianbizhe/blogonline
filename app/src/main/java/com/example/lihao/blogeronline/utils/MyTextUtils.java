package com.example.lihao.blogeronline.utils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lihao on 17-10-22.
 */

public class MyTextUtils {

    public static boolean isNumber(String text){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(text);
        if(isNum.matches() ){
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(Collection c) {
        if (null == c || c.isEmpty()) {
            return true;
        }
        return false;
    }


}
