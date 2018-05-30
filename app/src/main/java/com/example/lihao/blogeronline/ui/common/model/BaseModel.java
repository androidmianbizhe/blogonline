package com.example.lihao.blogeronline.ui.common.model;

import com.example.lihao.blogeronline.bean.Article;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by lihao on 17-11-21.
 */

public interface BaseModel {

    Observable<ArrayList<Article>> getRecInfoFromPage(int page,int from_id);
    Observable<ArrayList<Article>> getInfoFromPages(String url,int page);

}
