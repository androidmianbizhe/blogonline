package com.example.lihao.blogeronline.data;

import com.example.lihao.blogeronline.data.model.UploadArticle;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lihao on 17-11-27.
 */

public interface UploadArticleDao {

    //uploadArticle
    Observable<List<UploadArticle>> insert(List<UploadArticle> articleList);

    Observable<UploadArticle> update(UploadArticle article);

    Observable<List<UploadArticle>> delete(List<UploadArticle> articles);

    Observable<List<UploadArticle>> select(int size);

}
