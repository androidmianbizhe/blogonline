package com.example.lihao.blogeronline.http;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/4/16.
 */

public interface Api {

    @GET()
    Observable<String> getBlogerDetail(@Url String url);

    @GET()
    Observable<String> getInfoAtPage(@Url String url, @Query("page") int page);
}
