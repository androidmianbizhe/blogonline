package com.example.lihao.blogeronline.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-11-2.
 */

public class RequestUtils {

    public static RequestBody toRequestBody(String value){
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),value);
        return body;
    }

}
