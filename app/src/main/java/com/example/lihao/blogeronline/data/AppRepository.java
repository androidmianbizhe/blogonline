package com.example.lihao.blogeronline.data;

import com.example.lihao.blogeronline.data.db.LiteOrmHelper;
import com.example.lihao.blogeronline.data.model.UploadArticle;
import com.example.lihao.blogeronline.utils.UIUtils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lihao on 17-12-1.
 */

public class AppRepository implements UploadArticleDao {

    private static volatile AppRepository sInstance;

    UploadArticleDaoImple uploadArticleDaoImple;

    private AppRepository(){

        uploadArticleDaoImple =
                new UploadArticleDaoImple(UIUtils.getContext(), LiteOrmHelper.getInstance());

    }

    public static AppRepository getInstance(){

        if(sInstance == null){
            synchronized (AppRepository.class){
                if(sInstance == null){
                    sInstance = new AppRepository();
                }
            }
        }
        return sInstance;
    }

    @Override
    public Observable<List<UploadArticle>> insert(List<UploadArticle> articleList) {
        return uploadArticleDaoImple.insert(articleList);
    }

    @Override
    public Observable<UploadArticle> update(UploadArticle article) {
        return uploadArticleDaoImple.update(article);
    }

    @Override
    public Observable<List<UploadArticle>> delete(List<UploadArticle> articles) {
        return uploadArticleDaoImple.delete(articles);
    }

    @Override
    public Observable<List<UploadArticle>> select(int size) {
        return uploadArticleDaoImple.select(size);
    }
}
